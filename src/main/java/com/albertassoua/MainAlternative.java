package com.albertassoua;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class MainAlternative {
    public static void main(String[] args) {
        System.out.println(
                "/* ---------------------------- Main Thread---------------------------------------------- */");
        System.out.println(
                "/* ----------------------------" + Thread.currentThread().getName() + "------------------------ */");
        System.out.println("/* -------------------------------------------------------------------------- */");
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        IntStream.rangeClosed(1, 10).forEach(num -> {
            CompletableFuture<Void> x = CompletableFuture.runAsync(() -> {
                if (num % 2 != 0) {
                    System.out.println("Thread Name " + Thread.currentThread().getName() + " : " + num);
                    System.out.println("Paused: " + Thread.currentThread().getName() + " for 1.5s");
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, executorService);
            x.join();
            CompletableFuture<Void> y = CompletableFuture.runAsync(() -> {
                if (num % 2 == 0) {
                    System.out.println("Thread Name " + Thread.currentThread().getName() + " : " + num);
                    System.out.println("Paused: " + Thread.currentThread().getName() + " for 2s");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, executorService);
            y.join();
        });
    }
}
