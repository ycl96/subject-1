package com.study.juc.map;

import java.lang.reflect.Field;
import java.util.HashMap;

// 验证程序：JDK1.8
// 1、 正常情况下，size超过阈值才会进行扩容
// 2、 1.8的特殊情况，当链表元素超过8个，转换为红黑树的数据结构。 treeifyBin方法中会进行判断，如果table长度小于64，则扩容
// 导致的后果就是：如果前9个元素，都存储在同一个数组位置，则hashmap在未满12个元素时，就会进行扩容
public class HashMapTest {
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
