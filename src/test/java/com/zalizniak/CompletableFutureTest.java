package com.zalizniak;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CompletableFutureTest {

    @Test
    public void test() {
        List<String> list =
                IntStream.range(0, 10)
                        .boxed()
                        .map(this::generateTask)
                        .collect(Collectors.toList());
        System.out.println(list.toString());
    }

    @Test
    public void test1() throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newFixedThreadPool(8);

        List<CompletableFuture<String>> futures = IntStream.range(0, 10)
                .boxed()
                .map(i -> this.generateTask(i, executor)
                        .exceptionally(Throwable::getMessage))
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                .thenAccept(System.out::println)
                .get();

        //Thread.sleep(1000);
    }

    private CompletableFuture<String> generateTask(int i, ExecutorService executorService) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i == 5) {
                throw new RuntimeException("Run, it is a 5!");
            }
            return i + "-" + "test";
        }, executorService);
    }

    private String generateTask(int i) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i + "-" + "test";
    }
}
