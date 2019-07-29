package com.study.lock.aqs;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/** 自己写的AQS */
public class AQSdemo {
    // 同步资源状态
    volatile AtomicInteger state = new AtomicInteger(0);
    // 当前锁的拥有者
    protected volatile AtomicReference<Thread> owner = new AtomicReference<>();
    // java q 线程安全
    public volatile LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    // 独占
    public void acquire() {
        // 塞到等待锁的集合中
        waiters.offer(Thread.currentThread());
        while (!tryAcquire()) {
            // 挂起这个线程
            LockSupport.park();
        }
        // 后续，等待其他线程释放锁，收到通知之后继续循环
        waiters.remove(Thread.currentThread());
    }

    public void release() {
        // cas 修改 owner 拥有者
        if (tryRelease()) {
            Thread waiter = waiters.peek();
            LockSupport.unpark(waiter); // 唤醒线程继续 抢锁
        }
    }

    public boolean tryAcquire() {
        throw new UnsupportedOperationException();
    }

    public boolean tryRelease() {
        throw new UnsupportedOperationException();
    }


    // 共享资源获取
    public void acquireShared() {
        // 塞到等待锁的集合中
        waiters.offer(Thread.currentThread());
        while (tryAcquireShared() < 0) {
            // 挂起这个线程
            LockSupport.park();
        }
        // 后续，等待其他线程释放锁，收到通知之后继续循环
        waiters.remove(Thread.currentThread());
    }

    // 共享资源的释放
    public void releaseShared() {
        // cas 修改 owner 拥有者
        if (tryReleaseShared()) {
            Thread waiter = waiters.peek();
            LockSupport.unpark(waiter); // 唤醒线程继续 抢锁
        }
    }


    public int tryAcquireShared() {
        throw new UnsupportedOperationException();
    }

    public boolean tryReleaseShared() {
        throw new UnsupportedOperationException();
    }

    public AtomicInteger getState() {
        return state;
    }

    public void setState(AtomicInteger state) {
        this.state = state;
    }
}
