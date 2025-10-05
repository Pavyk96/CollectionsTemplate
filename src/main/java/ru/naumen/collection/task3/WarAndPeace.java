package ru.naumen.collection.task3;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Написать консольное приложение, которое принимает на вход произвольный текстовый файл в формате txt.
 * Нужно собрать все встречающийся слова и посчитать для каждого из них количество раз, сколько слово встретилось.
 * Морфологию не учитываем.</p>
 * <p>Вывести на экран наиболее используемые (TOP) 10 слов и наименее используемые (LAST) 10 слов</p>
 * <p>Проверить работу на романе Льва Толстого “Война и мир”</p>
 *
 * @author vpyzhyanov
 * @since 19.10.2023
 */
public class WarAndPeace
{

    private static final Path WAR_AND_PEACE_FILE_PATH = Path.of("src/main/resources",
            "Лев_Толстой_Война_и_мир_Том_1,_2,_3,_4_(UTF-8).txt");

    /**
     * Алгоритм работает в среднем за O(n + u * log(u)), где n - длинна списка слов,
     * u * log(u) - сортировка уникальных слов, важно заметить, что
     * u << n, потому что уникальных слов во много раз меньше чем всех слов
     */
    public static void main(String[] args) {

        /**
         * Использую СвязнуюХешМапу, чтобы по ней можно было быстро итерироваться, вставка в такую мапу O(1),
         * благодаря хеш функции
         */
        Map<String, Integer> wordCount = new LinkedHashMap<>();

        new WordParser(WAR_AND_PEACE_FILE_PATH)
                .forEachWord(word -> {
                    wordCount.merge(word, 1, Integer::sum);
                });

        /**
         * Использую СвязнуюХешМапу, потому что она сохраняет порядок вставки
         */
        LinkedHashMap<String, Integer> sortedByFrequency = wordCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        /**
         * Использую стримы для быстрой итерации по отсортированной хеш мапе
         */
        System.out.println("\nTOP 10 наиболее используемых слов:");
        sortedByFrequency.entrySet().stream().limit(10)
                .forEach(entry ->
                        System.out.printf("%s - %d раз(а)%n", entry.getKey(), entry.getValue())
                );

        System.out.println("\nLAST 10 наименее используемых слов:");
        sortedByFrequency.entrySet().stream()
                .skip(Math.max(0, sortedByFrequency.size() - 10))
                .forEach(entry ->
                        System.out.printf("%s - %d раз(а)%n", entry.getKey(), entry.getValue())
                );
    }

}
