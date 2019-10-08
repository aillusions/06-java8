package com.zalizniak;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFuture_thenApply_vs_thenCompose {

    public String mapSync(int var) {
        return "var == " + var;
    }

    public CompletableFuture<String> mapAsync(int var) {
        return CompletableFuture.supplyAsync(() -> "var == " + var);
    }

    @Test
    public void testThenApply() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 123);
        CompletableFuture<String> future2 = future1.thenApplyAsync(this::mapSync);
        CompletableFuture<Void> future3 = future2.thenAcceptAsync((v) -> System.out.println("Done: " + v));
        future3.get();
    }

    @Test
    public void testThenCompose() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 567);
        CompletableFuture<String> future2 = future1.thenComposeAsync(this::mapAsync);
        CompletableFuture<Void> future3 = future2.thenAcceptAsync((v) -> System.out.println("Done: " + v));
        future3.get();
    }
}
