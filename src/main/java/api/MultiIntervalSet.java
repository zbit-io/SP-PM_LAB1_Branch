package api;

import entity.Interval;

import java.util.Set;

public interface MultiIntervalSet<L> extends IntervalSet<L> {
    // 创建一个非空对象，使用 initial 中包含的数据创建非空对象
    MultiIntervalSet<L> from(IntervalSet<L> initial);
    // 在当前对象中插入新的时间段和标签
    void insert(long start, long end, L label);
    // 从当前对象中移除某个标签所关联的所有时间段
    boolean removeAll(L label);
    // 从当前对象中获取与某个标签所关联的所有时间段
    Set<Interval> intervals(L label);
}
