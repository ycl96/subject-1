package com.study.lock.lock;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// 两个线程，对 i 变量进行递增操作
public class LockDemo1 {
    volatile int i = 0;

    // 自己写 --
    DemoLock lock = new DemoLock(); // 基于sync关键字的原理来手写

    public void add() { // 方法栈帧~ 局部变量
        // TODO xx00
        lock.lock(); // 如果一个线程拿到锁，其他线程会等待
        try {
            i++; // 三次操作,字节码太难懂
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws IOException {
        LockDemo1 ld = new LockDemo1();

        for (int i = 0; i < 2; i++) { // 2w相加，20000
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    ld.add();
                }
            }).start();
        }
        System.in.read(); // 输入任意键退出
        System.out.println(ld.i);
    }
}
