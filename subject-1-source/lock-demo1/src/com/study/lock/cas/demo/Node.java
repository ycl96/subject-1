package com.study.lock.cas.demo;

// 存储在栈里面元素 -- 对象
public class Node {
    public final String item;
    public Node next;

    public Node(String item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "item内容:" + this.item;
    }
}