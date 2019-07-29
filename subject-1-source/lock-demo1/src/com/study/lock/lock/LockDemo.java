package com.study.lock.lock;

import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;

// 两个线程，对 i 变量进行递增操作
public class LockDemo {
    volatile int i = 0;

    static Unsafe unsafe; // 不安全--强大---（修改对象、直接底层内存修改数组的内容）
    static long targetOffset;// 在内存中属性的相对位置（对象实例），提供给JVM 用于定位内存位置

    static {
        // 提供盾 --- JAVA 矛（黑科技 -- 反射）
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true); // 暴力
            unsafe = (Unsafe) theUnsafe.get(null);
            // 获取到 对象属性的偏移量
            targetOffset = unsafe.objectFieldOffset(LockDemo.class.getDeclaredField("i"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CAS无锁+ 循环  == 自旋锁
    // 保证原子性：资源发生变化之后，应该要保证操作的结果正确
    public void add() { // 方法栈帧~ 局部变量
        // i++; // 字节码的角度，三个步骤
        // 需要用 硬件层次的操作~ 直接操作内存(黑科技)
        int current;// 1. 读取当前值
        int n;
        do {
            current = i;
            n = current + 1;// 2. 计算
        } while (!unsafe.compareAndSwapInt(this, targetOffset, current, n));
//        // 比对一下 -- i的值没有变化 --------- 改为 底层CAS指令
////        if (i == current) {// 比较
////            i = n;// 3. 赋值
////        } else {
////            // ....这一次赋值失败.....待续
////        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        LockDemo ld = new LockDemo();

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
