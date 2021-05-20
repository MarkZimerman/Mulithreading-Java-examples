package Multithreading.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadDemo {
    public static void show() {
        System.out.println("Current thread name: " + Thread.currentThread().getName());

        // Parallel run example (invisible work of Thread scheduler: 10 threads on 4 cores):
        /*
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new DownloadFileTask());
            thread.start();
        }
        */

        // One thread waits until another finish:
        /*
        Thread myThread = new Thread(new DownloadFileTask());
        myThread.start();
        try {
            myThread.join();    // 'main' thread will wait for completion of thread 'myThread'
                                // so it will block 'main' thread until 'myThread' has finished
                                // in this case UI will freeze
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("File was downloaded and is ready to be scanned");
        */

        // Interrupting a thread:
        /*
        Thread myThread = new Thread(new DownloadFileTask());
        myThread.start();

        try {
            Thread.sleep(500);        // Pausing current thread 'main'
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        myThread.interrupt();               // Sends an interrupt signal to a thread 'myThread'
        */

        // Race condition:
        /*
        * Strategies to solve race condition:
        * - Confinement: all threads will work work with theirs own 'status' object, then combine them to get total,
        *                but for implementing this we need to add List<DownloadFileTask> and status getter inside task.
        * - Use Immutability of objects
        * - Locks: add a Lock (ReentrantLock impl) for common operation, to make other threads waiting (finally: unlock)
        * - Synchronization: 'synchronized (this) { i++ }' - is a bad practice, because if you have another method,
        *                     which should be synchronized, you will block both of them. It will also reduce throughput.
        *                     Use Object totalBytesLock = new Object(); to lock on it: synchronized(totalBytesLock){...}
        *                     Synchronization is BAD: code runs sequentially, deadlocks, bugs, loose concurrency.
        * - Volatile: Solves visibility problem but not the race condition. Value became visible across threads.
        * - Atomic objects: AtomicInteger or LongAdder <- has Integer, Float, etc inside.
        * - Partitioning (one thread can access only one segment on an collection) ConcurrentHashMap<K, V>(); (faster)
        * - SynchronizedCollection: Uses locks inside. Collections.synchronizedCollection(new ArrayList<>()); (slower)
        * */
        var status = new DownloadStatus();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            var thread = new Thread(new DownloadFileTask2(status));
            thread.start();
            threads.add(thread);
        }

        // We are waiting of all threads, joining all of them
        for (var thread: threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Total bytes: " + status.getTotalBytes());

    }

    /*
    Wait/Notify:
    Runnable.wait() makes this object/thread sleep until receiving 'notify' signal. Should be in synchronized block.
    Runnable.notify() - sends signal for wake up to waited thread. Must be synchronized by same object.
    Runnable.notify() - sends signal for wake up to all waiting threads.Runnable.notify() - sends signal for wake up to waited thread.
     */
}
