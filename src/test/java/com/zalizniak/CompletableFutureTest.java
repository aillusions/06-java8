package com.zalizniak;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CompletableFutureTest {

    public Future<String> calculateAsync() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(500);
            completableFuture.complete("Hello");
            return null;
        });

        return completableFuture;
    }

    @Test
    public void testAsync() throws ExecutionException, InterruptedException {
        Assert.assertEquals("Hello", calculateAsync().get());
    }


    public Future<String> supplyAsync(String arg) {
        return CompletableFuture.supplyAsync(() -> arg + "-" + arg);
    }

    @Test
    public void testSupply() throws ExecutionException, InterruptedException {
        Assert.assertEquals("aa-aa", supplyAsync("aa").get());
    }

    @Test
    public void testComplete() throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete("123");

        Assert.assertEquals("123", future.get());
    }

    @Test
    public void testExceptionally() throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (true) {
                throw new RuntimeException("Hello error.");
            }
            return "All good";
        }).exceptionally((arg) -> "Ooops, error:" + arg);

        System.out.println(future.get());
    }

    @Test
    public void testWhenComplete() throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println("step1");
                    return "step1";
                })
                .whenComplete((result, error) -> System.out.println(result)).whenComplete((ok, err) -> System.out.println("end"));


    }

    @Test
    public void test1() throws InterruptedException, ExecutionException {

        List<CompletableFuture<String>> futures = IntStream.range(0, 10)
                .boxed()
                .map(i -> this.generateTask(i)
                        .exceptionally(Throwable::getMessage))
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                .thenAccept(System.out::println)
                .get();
    }

    private CompletableFuture<String> generateTask(int i) {
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
        });
    }
}
