package practice;

import java.nio.file.Path;
import java.nio.file.Paths;


public final class Main {
    private Main() {}

    public static void main(String[] args) {
        String path = args[0];
        runTest(OneThreadMD5.getInstance(), path);
        runTest(ExecutorServiceMD5.getInstance(), path);
        runTest(ForkJoinMD5.getInstance(), path);

        runTest(OneThreadMD5.getInstance(), path);
        runTest(ExecutorServiceMD5.getInstance(), path);
        runTest(ForkJoinMD5.getInstance(), path);


        runTest(OneThreadMD5.getInstance(), path);
        runTest(ExecutorServiceMD5.getInstance(), path);
        runTest(ForkJoinMD5.getInstance(), path);
    }

    public static void runTest(MD5Manager md5Manager, String filePath) {
        long start = System.currentTimeMillis();
        Path path = Paths.get(filePath);
        String value = md5Manager.getMD5Checksum(path);
        long end = System.currentTimeMillis();

        System.out.println(md5Manager.getClass().getSimpleName());
        System.out.printf("File name: %s%n", filePath);
        System.out.printf("md5: %s%n", value);
        System.out.printf("time: %d ms%n", end - start);
        System.out.println();
    }

}
