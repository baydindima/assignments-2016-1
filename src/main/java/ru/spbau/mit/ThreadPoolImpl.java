package ru.spbau.mit;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadPoolImpl implements ThreadPool {
    private final Thread[] threads;
    private final Queue<LightFutureImpl> taskQueue = new LinkedList<>();

    public ThreadPoolImpl(int n) {
        threads = new Thread[n];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new PoolThread(taskQueue);
        }
        Arrays.stream(threads)
                .forEach(Thread::start);
    }

    @Override
    public <R> LightFuture<R> submit(Supplier<R> supplier) {
        LightFutureImpl<R> future = new LightFutureImpl<>(this, supplier);
        addFuture(future);
        return future;
    }

    private <R> void addFuture(LightFutureImpl<R> future) {
        synchronized (taskQueue) {
            taskQueue.add(future);
            taskQueue.notify();
        }
    }

    @Override
    public void shutdown() {
        Arrays.stream(threads)
                .forEach(Thread::interrupt);
        synchronized (taskQueue) {
            taskQueue.stream().forEach(t -> t.setException(new InterruptedException()));
        }
    }

    private static final class PoolThread extends Thread {
        private final Queue<LightFutureImpl> taskQueue;

        private PoolThread(Queue<LightFutureImpl> queue) {
            taskQueue = queue;
        }

        @Override
        public void run() {
            while (!this.isInterrupted()) {
                synchronized (taskQueue) {
                    while (taskQueue.isEmpty()) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException ignored) {
                        }
                        if (isInterrupted()) {
                            return;
                        }
                    }
                }
                LightFutureImpl<?> future = taskQueue.poll();
                try {
                    future.runnable.run();
                } catch (Throwable e) {
                    future.throwable = e;
                }
                future.updateDependent();
                future.finished = true;
                synchronized (future.lock) {
                    future.lock.notifyAll();
                }
            }
        }
    }

    private static final class LightFutureImpl<R> implements LightFuture<R> {
        private final ThreadPoolImpl threadPool;
        private final List<LightFutureImpl> dependentFutures = new LinkedList<>();
        private final Object lock = new Object();
        private final Runnable runnable;

        private volatile boolean finished = false;

        private volatile Throwable throwable;
        private volatile R result;

        private LightFutureImpl(ThreadPoolImpl threadPool, Supplier<R> supplier) {
            this.threadPool = threadPool;
            this.runnable = () -> result = supplier.get();
        }

        private LightFutureImpl(Throwable e) {
            throwable = e;
            finished = true;
            threadPool = null;
            runnable = null;
        }

        @Override
        public boolean isReady() {
            return finished;
        }

        @Override
        public R get() throws LightExecutionException, InterruptedException {
            if (!isReady()) {
                synchronized (lock) {
                    while (!isReady()) {
                        lock.wait();
                    }
                }
            }
            if (throwable == null) {
                return result;
            } else {
                throw new LightExecutionException(throwable);
            }
        }

        @Override
        public <U> LightFuture<U> thenApply(Function<? super R, ? extends U> f) {
            if (isReady()) {
                return applyReadyTask(f);
            }
            LightFutureImpl<U> future;
            synchronized (dependentFutures) {
                synchronized (lock) {
                    if (isReady()) {
                        return applyReadyTask(f);
                    }
                    future = new LightFutureImpl<>(threadPool, () -> f.apply(LightFutureImpl.this.result));
                        dependentFutures.add(future);
                    }
            }
            return future;
        }

        private <U> LightFuture<U> applyReadyTask(Function<? super R, ? extends U> f) {
            if (throwable != null) {
                return new LightFutureImpl<>(throwable);
            } else {
                return threadPool.submit(() -> f.apply(result));
            }
        }

        private void updateDependent() {
            if (throwable != null) {
                broadcastException();
            } else {
                addTaskToQueue();
            }
        }

        private void setException(Exception e) {
            synchronized (lock) {
                throwable = e;
                finished = true;
                lock.notifyAll();
            }
            broadcastException();
        }

        private void broadcastException() {
            synchronized (dependentFutures) {
                dependentFutures.parallelStream()
                        .forEach(lightFuture -> {
                            lightFuture.throwable = throwable;
                            lightFuture.finished = finished;
                            synchronized (lightFuture.lock) {
                                lightFuture.lock.notifyAll();
                            }
                            lightFuture.broadcastException();
                        });
                dependentFutures.clear();
            }
        }

        private void addTaskToQueue() {
            synchronized (dependentFutures) {
                dependentFutures.parallelStream()
                        .forEach(threadPool::addFuture);
                dependentFutures.clear();
            }
        }

    }
}
