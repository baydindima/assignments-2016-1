package practice08;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class ForkJoinMD5 implements MD5Manager {

    private static final ForkJoinMD5 instance = new ForkJoinMD5();

    public static ForkJoinMD5 getInstance() {
        return instance;
    }

    private static class ChecksumTask extends RecursiveTask<String> {
        private final Path path;

        private ChecksumTask(Path path) {
            this.path = path;
        }

        @Override
        protected String compute() {
            try {
                if (!Files.isDirectory(path)) {
                    InputStream is = new BufferedInputStream(Files.newInputStream(path));
                    return DigestUtils.md5Hex(is);
                } else {
                    List<Path> paths = Files.list(path).collect(Collectors.toList());
                    List<ChecksumTask> tasks = new ArrayList<>(paths.size());
                    for (Path path1 : paths) {
                        ChecksumTask checksumTask = new ChecksumTask(path1);
                        checksumTask.fork();
                        tasks.add(checksumTask);
                    }

                    StringBuilder builder = new StringBuilder(path.toString());
                    for (ChecksumTask task : tasks) {
                        builder.append(task.join());
                    }
                    return DigestUtils.md5Hex(builder.toString().getBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getMD5Checksum(Path path) {
        return new ForkJoinPool().invoke(new ChecksumTask(path));
    }
}
