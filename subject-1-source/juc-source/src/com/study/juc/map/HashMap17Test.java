package com.study.juc.map;

import java.lang.reflect.Field;
import java.util.HashMap;

// 制定1.7版本运行
public class HashMap17Test {
    public static void main(String[] args) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        Field tableField = HashMap.class.getDeclaredField("table");
        tableField.setAccessible(true);

        // 重点 - 将Hash值计算出来的index相同的数据插入到Map中去。
        for (int i = 0; i < 1000; i++) {
            String key = "a" + i;
            // hash值
            int h = key.hashCode();
            int hash = h ^ (h >>> 16);
            // hashmap数组长度
            Object[] table = ((Object[]) tableField.get(map));
            int tableSize = 16;
            if (table != null) {
                tableSize = ((Object[]) tableField.get(map)).length;
            }
            // key对于的下标
            int index = ((tableSize - 1) & hash);
            // 只插入下标为10的数据
            if (index == 10) {
                map.put(key, "xxx");
                // 打印当前的table长度，看看是不是扩容了
                System.out.println(((Object[]) tableField.get(map)).length);
            }
        }
    }
}
