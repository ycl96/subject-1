package com.study.lock.sync;

// 锁 方法(静态/非静态),代码块(对象/类)
public class ObjectSyncDemo1 {

    static Object temp = new Object();

    public void test1() { // 方法上面：锁的对象 是 类的一个实例
        synchronized (this) { // 类锁(class对象，静态方法)，实例锁(this，普通方法)
            try {
                System.out.println(Thread.currentThread() + " 我开始执行");
                Thread.sleep(3000L);
                System.out.println(Thread.currentThread() + " 我执行结束");
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            new ObjectSyncDemo1().test1();
        }).start();

        Thread.sleep(1000L); // 等1秒钟,让前一个线程启动起来
        new Thread(() -> {
            new ObjectSyncDemo1().test1();
        }).start();
    }
}
