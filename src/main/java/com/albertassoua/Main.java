package com.albertassoua;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        System.out.println(
                "/* ---------------------------- Main Thread---------------------------------------------- */");
        System.out.println(
                "/* ----------------------------" + Thread.currentThread().getName() + "------------------------ */");
        System.out.println("/* -------------------------------------------------------------------------- */");
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        IntStream.rangeClosed(1, 10).forEach(num -> {
            CompletionStage<Integer> x = CompletableFuture.supplyAsync(() -> {
                try {
                    /*
                     * CompletableFuture tasks are executed by default in the
                     * "ForkJoinPool.commonPool()" or a default executor if one is not explicitly
                     * provided. The common pool uses a pool of worker threads that are designed for
                     * parallelism and are typically separate from the main thread.
                     */
                    System.out.println("Paused: " + Thread.currentThread().getName() + " for 1s");
                    Thread.sleep(1000);
                    /**
                     * Or
                     * TimeUnit.SECONDS.sleep(2);
                     */
                    return num;
                } catch (Exception e) {
                    System.out.println("Thread was interrupted!");
                    Thread.currentThread().interrupt();
                }
                return num;
            }).thenComposeAsync(y -> {
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        if (y % 2 != 0) {
                            System.out.println("Paused: " + Thread.currentThread().getName() + " for 1.5s");
                            Thread.sleep(1500);
                            System.out.println("Thread Name " + Thread.currentThread().getName() + " : " + y);
                        }
                        // return num;
                        /**
                         * Or
                         * TimeUnit.SECONDS.sleep(2);
                         */
                    } catch (InterruptedException e) {
                        System.out.println("Thread was interrupted!" + Thread.currentThread().getName());
                        e.printStackTrace();
                    }
                    return y;
                });
            }, executorService);
            CompletableFuture<Integer> completableFutureX = x.toCompletableFuture();
            completableFutureX.join();
            CompletionStage<Integer> p = CompletableFuture.supplyAsync(() -> {
                try {
                    return num;
                } catch (Exception e) {
                    System.out.println("Thread was interrupted!");
                    Thread.currentThread().interrupt();
                }
                return num;
            }).thenComposeAsync(y -> {
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        if (y % 2 == 0) {
                            System.out.println("Pause: " + Thread.currentThread().getName() + " for 2s");
                            Thread.sleep(2000);
                            System.out.println("Thread Name " + Thread.currentThread().getName() + " : " + y);
                        }
                        return num;
                        /**
                         * Or
                         * TimeUnit.SECONDS.sleep(2);
                         */
                    } catch (InterruptedException e) {
                        System.out.println("Thread was interrupted!" + Thread.currentThread().getName());
                        e.printStackTrace();
                    }
                    return y;
                });
            }, executorService);
            CompletableFuture<Integer> completableFutureP = p.toCompletableFuture();
            completableFutureP.join();
        });
        executorService.shutdown();
    }
}