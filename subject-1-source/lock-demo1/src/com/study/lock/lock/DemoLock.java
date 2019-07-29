package com.study.lock.lock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

// 优化--C++,运行时，只能推论
// sync对象监视器的原理 owner
public class DemoLock {
    // 锁的拥有者
    AtomicReference<Thread> owner = new AtomicReference<>();
    // 需要锁池
    LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    public void lock() {
        // CAS -- 此处直接CAS，是一种非公平的实现
        while (!owner.compareAndSet(null, Thread.currentThread())) {
            // TODO 没拿到锁，等待
            waiters.add(Thread.currentThread());
            LockSupport.park(); // 挂起，等待被唤醒...
        }
    }

    public void unlock() {
        if (owner.compareAndSet(Thread.currentThread(), null)) {
            // 释放锁之后，要唤醒一个线程
            Thread next = waiters.poll();
            LockSupport.unpark(next);
        }
    }
}
