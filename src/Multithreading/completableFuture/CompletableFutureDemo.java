package Multithreading.completableFuture;

import Multithreading.executors.LongTask;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CompletableFutureDemo {

    public static void show(){
        // ForkJoinPool.commonPool();  // Default pool(common), used if another executer not provided.

        Supplier<Integer> task = () -> 1;
        var future = CompletableFuture.supplyAsync(task);

        /*
        Runnable task = () -> System.out.println("a");
        var future = CompletableFuture.runAsync(task);
         */

        try {
            var result = future.get(); // Will block code here
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Running Code on Completion:
    public static void show2(){
        var future = CompletableFuture.supplyAsync(() -> 1);
        future.thenRunAsync(() -> System.out.println("Done"));
        /*
        .thenRun(Runnable)
        .thenRunAsync(Runnable)
        .thenAccept(Consumer)
        .thenAcceptAsync(Consumer)
         */
    }

    // Handling exceptions:
    public static void show3(){
        var future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Getting the current weather");
            throw new IllegalStateException();  // Note! that exception doesn't leak from here
        });

        try {
            //future.get();                     // Exception can be cached only in method that throws (get() throws...)
            var temperature = future.exceptionally(ex -> 99).get(); // Exception was treated with default value '99'
            System.out.println("Last temperature: " + temperature);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();                // <- HERE
        }
    }

    // Transforming a Completable Future:
    public static void show4(){
        var future = CompletableFuture.supplyAsync(() -> 20);

        /*
        try {
            var result = future
                    .thenApply(CompletableFutureDemo::toFahrenheit)
                    .get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
         */
        // OR:
        future.thenApply(CompletableFutureDemo::toFahrenheit).thenAccept(System.out::println);
    }

    private static int toFahrenheit(int celsius){
        return (int)(celsius * 1.8) +  32;
    }

    public static CompletableFuture<String> getUserEmailAsync(){
        return CompletableFuture.supplyAsync(() -> "admin@mail.com");
    }

    public static CompletableFuture<String> getPlaylistAsync(String email){
        return CompletableFuture.supplyAsync(() -> ("Playlist for " + email));
    }

    // Start task on completion of another task
    public static void show5(){
        // id -> db: email
        // email -> playlist

        // CompletableFuture.supplyAsync(() -> "Query email from DB")
        // OR:
            getUserEmailAsync()
            // .thenCompose(email -> CompletableFuture.supplyAsync(() -> "playlist for user email"))
            // OR:
            .thenCompose(CompletableFutureDemo::getPlaylistAsync)
            .thenAccept(playlist -> System.out.println(playlist));
    }

    // Start several tasks in the same time and then combine results:
    public static void show6(){
        // 1. Get price (20 USD)
        // 2. Get exchange rate (0.9)

        var first = CompletableFuture
                .supplyAsync(() -> "20USD")
                .thenApply(str -> Integer.parseInt( str.replace("USD", "") ));
        var second = CompletableFuture.supplyAsync(() -> 0.9);

        // Makes a process pipe not blocking a code run
        first
            .thenCombine(second, (price, exchangeRate) -> price * exchangeRate)
            .thenAccept((result) -> System.out.println(result));
    }

    // Waiting for many tasks:
    public static void show7(){
        var first = CompletableFuture.supplyAsync(() -> 1);
        var second = CompletableFuture.supplyAsync(() -> 2);
        var third = CompletableFuture.supplyAsync(() -> 3);

        var all = CompletableFuture.allOf(first, second, third);
        all.thenRun(() -> {
            try {
                var res1 = first.get();    // Will not block the code, because executed async after others finished yet.
                System.out.println(res1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("All tasks completed!");
        });
    }

    // Waiting for the first task:
    public static void show8(){
        var first = CompletableFuture.supplyAsync(() -> {
            LongTask.simulate();
            return 20;
        });

        var second = CompletableFuture.supplyAsync(() -> 20);

        // Chooses first completed task:
        var fastest = CompletableFuture.anyOf(first, second);
        fastest.thenAccept(System.out::println);
    }

    // Handling timeouts:
    public static void show9(){
        var future = CompletableFuture.supplyAsync(() -> {
            LongTask.simulate();
            return 1;
        });

        // Will throw 'TimeoutException'
        /*
        try {
            var result = future.orTimeout(1, TimeUnit.SECONDS).get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
         */
        // Better approach is to give to an end user an alternative/default data instead of timeout:
        try {
            var result = future.completeOnTimeout(99,1, TimeUnit.SECONDS).get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void ex1(){

        long start = System.currentTimeMillis();
        var first = CompletableFuture.supplyAsync(() -> {
            System.out.println("Getting a quote from site 1...");
            LongTask.simulate();
            return 100;
        });
        var second = CompletableFuture.supplyAsync(() -> {
            System.out.println("Getting a quote from site 2...");
            LongTask.simulate();
            return 105;
        });
        var third = CompletableFuture.supplyAsync(() -> {
            System.out.println("Getting a quote from site 3...");
            LongTask.simulate();
            return 108;
        });

        CompletableFuture.allOf(first, second, third).thenRun(() -> {
            try {
                System.out.println("Quote{site='site1', price=" + first.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("Quote{site='site2', price=" + second.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("Quote{site='site3', price=" + third.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            System.out.println("Request time: " + (System.currentTimeMillis() - start) + "ms.");

        });
    }

    public static void ex1_2() {
        var service = new FlightService();
//        service.getQuote("site1").thenAccept(System.out::println);
        long start = System.currentTimeMillis();
        var futures = service
                .getQuotes()
                .map(future -> future.thenAccept(System.out::println))
                .collect(Collectors.toList());
        CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> System.out.println("Done in " + (System.currentTimeMillis() - start)/1000 + " seconds"));
    }
}
