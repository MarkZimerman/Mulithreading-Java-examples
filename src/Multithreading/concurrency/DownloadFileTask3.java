package Multithreading.concurrency;

public class DownloadFileTask3 implements Runnable{
    @Override
    public void run() {
        System.out.println("Downloading a file in a thread: " + Thread.currentThread().getName());

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (Thread.currentThread().isInterrupted()) return;
            System.out.println("Downloading byte " + i);
        }

        System.out.println("Download complete in thread: " + Thread.currentThread().getName());
    }
}
