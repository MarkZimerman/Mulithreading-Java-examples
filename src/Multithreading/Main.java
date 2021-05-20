package Multithreading;

import Multithreading.completableFuture.CompletableFutureDemo;
import Multithreading.completableFuture.MailService;
import Multithreading.concurrency.ThreadDemo;
import Multithreading.executors.ExecutorsDemo;

public class Main {
    public static void main(String[] args) {
        System.out.println("Active threads: " + Thread.activeCount());
        System.out.println("Current thread name: " + Thread.currentThread().getName());
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Max memory: " + (Runtime.getRuntime().maxMemory() / 1024));
        System.out.println("-------------------");

//        ThreadDemo.show();
//        ExecutorsDemo.show();
//        CompletableFutureDemo.show();
//        CompletableFutureDemo.show3();
//        CompletableFutureDemo.show4();
//        CompletableFutureDemo.show5();
//        CompletableFutureDemo.show6();
//        CompletableFutureDemo.show7();
//        CompletableFutureDemo.show8();
//        CompletableFutureDemo.show9();
        CompletableFutureDemo.ex1_2();



//        /*
        var service = new MailService();
        //service.send();           // Will block code here
        service.sendAsync();        // Will run asynchronously
        System.out.println("12345---");
//        */

        // add sleep for prove that sendAsync() operated.
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

    }
}
