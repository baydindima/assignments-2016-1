package practice08;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class OneThreadMD5 implements MD5Manager {
    private static final OneThreadMD5 instance = new OneThreadMD5();

    public static OneThreadMD5 getInstance() {
        return instance;
    }

    @Override
    public String getMD5Checksum(Path path) {
        try {
            if (!Files.isDirectory(path)) {
                InputStream is = new BufferedInputStream(Files.newInputStream(path));
                return DigestUtils.md5Hex(is);
            } else {
                StringBuilder builder = new StringBuilder(path.toString());
                Files.list(path).forEach(path1 -> builder.append(getMD5Checksum(path1)));
                return DigestUtils.md5Hex(builder.toString().getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
