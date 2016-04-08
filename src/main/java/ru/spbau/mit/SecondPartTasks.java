package ru.spbau.mit;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {
    }

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.parallelStream()
                .flatMap(path -> {
                            try {
                                return Files.readAllLines(
                                        Paths.get(path),
                                        Charset.defaultCharset()
                                ).parallelStream();
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        }
                )
                .filter(s -> s.contains(sequence))
                .collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать,
    // какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        Random randomGenerator = new Random();
        final int experimentCount = 10000;
        final double radius = 0.5;
        final double circleCenter = 0.5;
        final double squadPow = 2;
        long count = Stream.generate(() ->
                        Math.sqrt(Math.pow(randomGenerator.nextDouble() - circleCenter, squadPow)
                                + Math.pow(randomGenerator.nextDouble() - circleCenter, squadPow)) <= radius
        ).limit(experimentCount).filter(aBoolean -> aBoolean).count();
        return count / (double) experimentCount;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        Optional<Map.Entry<String, List<String>>> max = compositions.entrySet().stream().max((o1, o2) ->
                        stringsCommonLength(o1.getValue().parallelStream())
                                - stringsCommonLength(o2.getValue().parallelStream())
        );
        if (max.isPresent()) {
            return max.get().getKey();
        }
        throw new NullPointerException("Compositions is empty!");
    }

    private static int stringsCommonLength(Stream<String> stringStream) {
        return stringStream.mapToInt(String::length).sum();
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream().flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(
                                Map.Entry::getKey,
                                Collectors.summingInt(Map.Entry::getValue))
                );
    }
}
