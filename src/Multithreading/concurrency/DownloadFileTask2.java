package Multithreading.concurrency;

public class DownloadFileTask2 implements Runnable{
    private DownloadStatus status;

    public DownloadFileTask2(DownloadStatus status) {
        this.status = status;
    }

    @Override
    public void run() {
        System.out.println("Downloading a file in a thread: " + Thread.currentThread().getName());

        for (int i = 0; i < 10_000; i++) {
            if (Thread.currentThread().isInterrupted()) return;
            status.incrementTotalBytes();
        }

        System.out.println("Download complete in thread: " + Thread.currentThread().getName());
    }
}
