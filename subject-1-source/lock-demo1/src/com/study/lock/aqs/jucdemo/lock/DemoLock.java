package com.study.lock.aqs.jucdemo.lock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

// 优化--C++,运行时，只能推论
// sync对象监视器的原理 owner
public class DemoLock implements Lock {
    // 锁的拥有者
    AtomicReference<Thread> owner = new AtomicReference<>(); // 独享锁 -- 资源只能被一个线程占有
    // 需要锁池
    LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    public void lock() { // 没拿到锁的线程运行这个方法
        // TODO 拿到锁，等待
        waiters.add(Thread.currentThread());
        // CAS -- 此处直接CAS，是一种非公平的实现
        while (!owner.compareAndSet(null, Thread.currentThread())) {
            LockSupport.park(); // 挂起，等待被唤醒...
        }
        waiters.remove(Thread.currentThread());
    }

    public void unlock() { // 拿到锁的线程运行这个方法
        if (owner.compareAndSet(Thread.currentThread(), null)) {
            // 释放锁之后，要唤醒线程(所有 -- 惊群效应)
            for (Thread waiter : waiters) {
                LockSupport.unpark(waiter);
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
