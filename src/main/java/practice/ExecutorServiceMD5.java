package practice;


import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.*;

public class ExecutorServiceMD5 implements MD5Manager {

    private static final ExecutorServiceMD5 INSTANCE = new ExecutorServiceMD5();

    public static ExecutorServiceMD5 getInstance() {
        return INSTANCE;
    }

    @Override
    public String getMD5Checksum(Path path) {
        final ExecutorService service = Executors.newCachedThreadPool();
        try {
            String result = service.submit(() -> calc(path, service)).get();
            service.shutdown();
            return result;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private String calc(Path path, ExecutorService executorService) {
        try {
            if (!Files.isDirectory(path)) {
                InputStream is = new BufferedInputStream(Files.newInputStream(path));
                return DigestUtils.md5Hex(is);
            } else {
                StringBuilder builder = new StringBuilder(path.toString());
                Files.list(path)
                        .map(
                                path1 -> executorService.submit(
                                        () -> calc(path1, executorService)
                                )
                        ).map(stringFuture -> {
                            try {
                                return stringFuture.get();
                            } catch (InterruptedException | ExecutionException e) {
                                throw new RuntimeException(e);
                            }
                        }).forEach(builder::append);
                return DigestUtils.md5Hex(builder.toString().getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
