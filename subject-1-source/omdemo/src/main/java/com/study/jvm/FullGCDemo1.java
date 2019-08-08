package com.study.jvm;

// 频繁调用system.gc导致fullgc次数过多
// 使用server模式运行 开启GC日志
// -Xmx512m -server -verbose:gc -XX:+PrintGCDetails -Xloggc:gc.log 
public class FullGCDemo1 {
    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 1000; i++) {
            byte[] tmp = new byte[1024 * 1024 * 256]; // 256兆
            // System.gc(); // 8G堆 128兆。full GC
            System.out.println("我GC一次了");
            Thread.sleep(2000L);
        }
    }
}
