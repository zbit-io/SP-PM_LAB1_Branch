package api.imp;

import api.IntervalSet;
import entity.Interval;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class CommonIntervalSet<L> implements IntervalSet<L> {
    // 使用 HashMap 存储标签和它们对应的时间段
    private final Map<L, Interval> intervalMap;
    public boolean isCoveredAt(long time) {
        for (Interval interval :  intervalMap.values()) {
            if (interval.getStart() <= time && interval.getEnd() >= time) {
                return true;
            }
        }
        return false;
    }


    // 构造函数初始化 intervalMap
    public CommonIntervalSet() {
        intervalMap = new HashMap<>();
    }

    // 创建一个空对象的实现
    @Override
    public IntervalSet<L> empty() {
        return new CommonIntervalSet<>();
    }

    // 插入新的时间段和标签的实现
    @Override
    public void insert(long start, long end, L label) {
        if (label == null || start < 0 || end < 0 || start > end) {
            throw new IllegalArgumentException("Invalid arguments for interval or label.");
        }
        if (intervalMap.containsKey(label)) {
            throw new IllegalArgumentException("Label already exists.");
        }
        intervalMap.put(label, new Interval(start, end));
    }

    // 获得当前对象中的标签集合的实现
    @Override
    public Set<L> labels() {
        return Collections.unmodifiableSet(intervalMap.keySet());
    }

    // 从当前对象中移除某个标签所关联的时间段的实现
    @Override
    public boolean remove(L label) {
        return intervalMap.remove(label) != null;
    }

    // 返回某个标签对应的时间段的开始时间的实现
    @Override
    public long start(L label) {
        Interval interval = intervalMap.get(label);
        if (interval == null) {
            throw new NoSuchElementException("Label not found: " + label);
        }
        return interval.getStart();
    }

    // 返回某个标签对应的时间段的结束时间的实现
    @Override
    public long end(L label) {
        Interval interval = intervalMap.get(label);
        if (interval == null) {
            throw new NoSuchElementException("Label not found: " + label);
        }
        return interval.getEnd();
    }

    // 提供对象内容的字符串表示形式
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IntervalSet{");
        for (Map.Entry<L, Interval> entry : intervalMap.entrySet()) {
            sb.append(entry.getKey()).append(": [").append(entry.getValue().getStart())
                    .append(", ").append(entry.getValue().getEnd()).append("], ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("}");
        return sb.toString();
    }

}
