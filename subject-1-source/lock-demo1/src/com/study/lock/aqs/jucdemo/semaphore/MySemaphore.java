package com.study.lock.aqs.jucdemo.semaphore;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

// 自定义的信号量实现
public class MySemaphore {
    AtomicInteger count = null;
    // 需要锁池
    LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    public MySemaphore(int num) {
        this.count = new AtomicInteger(num); // 令牌数量 数值
    }

    public void acquire() { // 获取令牌,没有令牌就等待
        // 进入等待列表
        waiters.add(Thread.currentThread());
        for (; ; ) {
            int current = count.get();
            int n = current - 1; // 发出一个令牌
            if (current <= 0 || n < 0) {
                // 挂起线程
                LockSupport.park();
            }
            if (count.compareAndSet(current, n)) {
                break;
            }
        }
        waiters.remove(Thread.currentThread());
    }

    public void release() { // 释放令牌 -- 令牌数量+1
        if (this.count.incrementAndGet() > 0) {
            // 释放锁之后，要唤醒其他等待的线程
            for (Thread waiter : waiters) {
                LockSupport.unpark(waiter);
            }
        }
    }
}
