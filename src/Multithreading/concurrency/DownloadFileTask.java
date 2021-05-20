package Multithreading.concurrency;

public class DownloadFileTask implements Runnable{
    @Override
    public void run() {
        System.out.println("Downloading a file in a thread: " + Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted!");
            return;
        }
        System.out.println("Download complete in thread: " + Thread.currentThread().getName());
    }
}
