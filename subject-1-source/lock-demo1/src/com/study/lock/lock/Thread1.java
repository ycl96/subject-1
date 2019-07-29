package com.study.lock.lock;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

public class Thread1 {
    // 需要锁池
    static LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                try {
                    waiters.add(Thread.currentThread());
                    LockSupport.park();
                    System.out.println("xxxooo");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        System.in.read();
        System.out.println("ooo");
    }
}
