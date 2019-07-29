package com.study.lock.aqs;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

// 模板方法模式 -- 抽象体现 AQS
public abstract class Abstract {
    AtomicInteger state = null; // 资源数量 -- 共享资源状态 -- 可以被一定数量线程
    // 锁的拥有者
    AtomicReference<Thread> owner = new AtomicReference<>(); // 独享锁 -- 资源只能被一个线程占有
    // 需要锁池
    LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    public void acquire(){ // 获取独占资源
        // 进入等待列表
        waiters.add(Thread.currentThread());
        // TODO 获取资源
        while(tryAcquire()) { // CDL,Lock...区别:真正获取资源的步骤是不同
            // 没有获取到资源的后续流程都是一样的
            LockSupport.park(); // 挂起线程
        }
        waiters.remove(Thread.currentThread());
    }

    public void release() {
        if (tryRelease()) {
            // 释放锁之后，要唤醒其他等待的线程
            for (Thread waiter : waiters) {
                LockSupport.unpark(waiter);
            }
        }
    }

    /** 释放资源 */
    protected abstract boolean tryRelease();

    /** 这个方法的实现,应该交给具体的使用者 */
    public abstract boolean tryAcquire();

    public void acquireShare(){ // 获取共享资源
        // 进入等待列表
        waiters.add(Thread.currentThread());
        // TODO 获取资源
        waiters.remove(Thread.currentThread());
    }
}
