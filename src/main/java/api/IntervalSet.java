package api;

import java.util.Set;

public interface IntervalSet<L> {
    // 创建一个空对象
    IntervalSet<L> empty();
    // 在当前对象中插入新的时间段和标签
    void insert(long start, long end, L label);
    // 获得当前对象中的标签集合
    Set<L> labels();
    // 从当前对象中移除某个标签所关联的时间段
    boolean remove(L label);
    // 返回某个标签对应的时间段的开始时间
    long start(L label);
    // 返回某个标签对应的时间段的结束时间
    long end(L label);
}
