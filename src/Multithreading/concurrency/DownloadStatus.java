package Multithreading.concurrency;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DownloadStatus {
    // Synchronized:
    private int totalBytes = 0;
    Object totalBytesLock = new Object();

    public int getTotalBytes() {
        return totalBytes;
    }

    /* // Considered as bad practice, because you can add another synchronized block later, and they all will be blocked
    public void incrementTotalBytes() {
        synchronized (this) {
            totalBytes++;
        }
    }
     */

    /* // OR:
    public void incrementTotalBytes(){
        synchronized (totalBytesLock) {
            totalBytes++;
        }
    }
     */

    // OR: BUT! also a bad practice, because it works same as 'synchronized (this) {...}' and locks on current object.
    public synchronized void incrementTotalBytes(){
        totalBytes++;
    }

    // Lock:
    /*
    private int totalBytes = 0;
    private Lock lock = new ReentrantLock();
    public int getTotalBytes() {
        return totalBytes;
    }

    public void incrementTotalBytes(){
        lock.lock();
        try {
            totalBytes++;
        } finally {
            lock.unlock();
        }
    }
    */

    // Atomic object:
    /*
    private AtomicInteger totalBytes = new AtomicInteger(0);

    public int getTotalBytes() {
        return totalBytes.get();
    }

    public void incrementTotalBytes(){
        totalBytes.incrementAndGet();
    }
    */
}
