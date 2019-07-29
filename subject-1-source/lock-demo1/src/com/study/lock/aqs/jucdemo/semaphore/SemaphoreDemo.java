package com.study.lock.aqs.jucdemo.semaphore;

import com.study.lock.aqs.AQSdemo;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

// 信号量机制
public class SemaphoreDemo {
    public static void main(String[] args) {
        SemaphoreDemo semaphoreTest = new SemaphoreDemo();
        int N = 9;            // 客人数量
        MySemaphore semaphore = new MySemaphore(5); // 手牌数量，限制请求数量
        for (int i = 0; i < N; i++) {
            String vipNo = "vip-00" + i;
            new Thread(() -> {
                try {
                    semaphore.acquire(); // 获取令牌,没拿到的就等
                    System.out.println(semaphore.count);
                    semaphoreTest.service(vipNo); // 实现了怼service方法的限流

                    semaphore.release(); // 释放令牌,令牌数+1
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    // 限流 控制5个线程 同时访问
    public void service(String vipNo) throws InterruptedException {
        System.out.println("楼上出来迎接贵宾一位，贵宾编号" + vipNo + "，...");
        Thread.sleep(new Random().nextInt(3000));
        System.out.println("欢送贵宾出门，贵宾编号" + vipNo);
    }

}