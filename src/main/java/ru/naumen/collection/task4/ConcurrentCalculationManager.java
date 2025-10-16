package ru.naumen.collection.task4;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Класс управления расчётами
 */
public class ConcurrentCalculationManager<T> {

    /**
     * Здесь используется LinkedBlockingQueue.
     * Это потокобезопасная блокирующая очередь на основе связного списка.
     * Она позволяет безопасно добавлять и извлекать элементы из разных потоков одновременно.
     * При этом операции вставки и удаления выполняются за O(1), а блокировка потоков
     * происходит только если очередь пуста при извлечении или полна при вставке.
     * Это делает её удобной для параллельной обработки задач, потому что
     * она сама управляет синхронизацией и упрощает реализацию FIFO.
     *
     * CompletableFuture используется для запуска задач асинхронно
     * и получения их результата без ручного управления потоками.
     */
    private final BlockingQueue<CompletableFuture<T>> results = new LinkedBlockingQueue<>();

    /**
     * Добавить задачу на параллельное вычисление
     */
    public void addTask(Supplier<T> task) {
        CompletableFuture<T> future = CompletableFuture.supplyAsync(task);
        results.add(future);
    }

    /**
     * Получить результат вычисления.
     * Возвращает результаты в том порядке, в котором добавлялись задачи.
     */
    public T getResult() throws InterruptedException {
        CompletableFuture<T> future = results.take();
        return future.join();
    }
}
