package ru.naumen.collection.task4;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Класс управления расчётами
 */
public class ConcurrentCalculationManager<T> {

    /**
     * Используем потокобезопасную мапу для хранения результатов по индексам.
     * Индексы позволяют выдавать результаты строго в порядке добавления задач, соблюдаем FIFO
     *
     * Вставка в такую структуру выполняется за O(1) по индексу
     * Сам алгоритм работает за O(n), где n - кол-во задач
     */
    private final ConcurrentHashMap<Integer, T> resultsMap = new ConcurrentHashMap<>();
    private final AtomicInteger currentIndex = new AtomicInteger(0);
    private final AtomicInteger nextResultIndex = new AtomicInteger(0);

    /**
     * Добавить задачу на параллельное вычисление
     */
    public void addTask(Supplier<T> task) {
        int taskIndex = currentIndex.getAndIncrement();
        T result = task.get();
        resultsMap.put(taskIndex, result);
    }

    /**
     * Получить результат вычисления.
     * Возвращает результаты в том порядке, в котором добавлялись задачи.
     */
    public T getResult() {
        int index = nextResultIndex.getAndIncrement();
        while (true) {
            T result = resultsMap.get(index);
            if (result != null) {
                resultsMap.remove(index);
                return result;
            }
            Thread.yield();
        }
    }
}
