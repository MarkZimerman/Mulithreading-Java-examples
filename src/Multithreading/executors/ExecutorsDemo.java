package Multithreading.executors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/*
Executors simplify threads manipulations
 */

public class ExecutorsDemo {
    public static void show() {
        // Executor executor = new ThreadPoolExecutor(..); // But simpler:
        var executor = Executors.newFixedThreadPool(2);

        try {
            // Set Runnable task to executor
            var future = executor.submit(() -> {
                System.out.println(Thread.currentThread().getName());
                LongTask.simulate();
                return 1;
            });

            System.out.println("Do work...");
            try {
                var result = future.get();
                System.out.println("Result: " + result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } finally {
            // To shutdown an executor, in other case it will wait for new tasks:
            executor.shutdown();    // Waits until all tasks finish
         // executor.shutdownAll(); // Forces stop.
        }
    }
}
