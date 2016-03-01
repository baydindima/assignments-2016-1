package practice02;

import java.io.*;

public class CP {
    private static final int MAX_MEMORY_USAGE = 1024;

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new RuntimeException("We need 2 arguments!");
        }

        String fromFileName = args[0];
        String toFileName = args[1];

        try (InputStream input = new FileInputStream(fromFileName);
             OutputStream output = new FileOutputStream(toFileName)) {
            byte[] buf = new byte[MAX_MEMORY_USAGE];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}