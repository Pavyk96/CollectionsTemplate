package ru.naumen.collection.task3;

import java.nio.file.Path;
import java.util.*;

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
     * Алгоритм работает в среднем за O(n), где n - длинна списка слов,
     * В методе мы много раз бегаем по результирующему списку, но его размер ограниченный
     * поэтому он во много раз меньше чем n, следовательно его можно принять за константу.
     * Из этого как раз и следует что алгоритм за O(n)
     */
    public static void main(String[] args) {

        /**
         * Использую СвязнуюХешМапу, чтобы по ней можно было быстро итерироваться, вставка в такую мапу O(1),
         * благодаря хеш функции, которая определена в String
         */
        Map<String, Integer> wordCount = new LinkedHashMap<>();

        // Очередь для топ-10 — минимальный элемент
        PriorityQueue<Map.Entry<String, Integer>> topTenWords =
                new PriorityQueue<>(11, Comparator.comparingInt(Map.Entry::getValue));

        // Очередь для последних 10 — максимальный элемент
        PriorityQueue<Map.Entry<String, Integer>> lastTenWords =
                new PriorityQueue<>(11, (a, b) -> Integer.compare(b.getValue(), a.getValue()));

        new WordParser(WAR_AND_PEACE_FILE_PATH)
                .forEachWord(word -> {
                    wordCount.merge(word, 1, Integer::sum);
                });

        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            updateQueue(topTenWords, entry, 10);
            updateQueue(lastTenWords, entry, 10);
        }


        // переносим из очередей в LinkedList в обратном порядке (чтобы вывести по убыванию/возрастанию)
        LinkedList<Map.Entry<String, Integer>> topResult = new LinkedList<>();
        while (!topTenWords.isEmpty()) {
            topResult.addFirst(topTenWords.poll());
        }

        LinkedList<Map.Entry<String, Integer>> lastResult = new LinkedList<>();
        while (!lastTenWords.isEmpty()) {
            lastResult.addFirst(lastTenWords.poll());
        }

        System.out.println("\nTOP 10 наиболее используемых слов:");
        for (Map.Entry<String, Integer> e : topResult) {
            System.out.printf("%s - %d раз(а)%n", e.getKey(), e.getValue());
        }

        System.out.println("\nLAST 10 наименее используемых слов:");
        for (Map.Entry<String, Integer> e : lastResult) {
            System.out.printf("%s - %d раз(а)%n", e.getKey(), e.getValue());
        }

    }

    /**
     * Вытесняет текущий граничный элемент, если кандидат "лучше" по компаратору.
     */
    private static void updateQueue(PriorityQueue<Map.Entry<String, Integer>> queue,
                                    Map.Entry<String, Integer> candidate,
                                    int limit) {
        if (queue.size() < limit) {
            queue.offer(candidate);
            return;
        }
        Map.Entry<String, Integer> peek = queue.peek();
        if (peek == null) return;

        Comparator<? super Map.Entry<String, Integer>> cmp = queue.comparator();
        if (cmp == null) throw new IllegalStateException("PriorityQueue requires an explicit comparator");

        if (cmp.compare(candidate, peek) > 0) {
            queue.poll();
            queue.offer(candidate);
        }
    }
}
