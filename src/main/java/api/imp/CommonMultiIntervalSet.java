package api.imp;
import api.IntervalSet;
import api.MultiIntervalSet;
import entity.Interval;


import java.util.*;

public class CommonMultiIntervalSet<L> implements MultiIntervalSet<L> {
    // 存储标签和与之关联的时间段集合的映射
    private final Map<L, Set<Interval>> labelToIntervalsMap;

    public CommonMultiIntervalSet() {
        labelToIntervalsMap = new HashMap<>();
    }

    @Override
    public MultiIntervalSet<L> empty() {
        return new CommonMultiIntervalSet<>();
    }

    @Override
    public MultiIntervalSet<L> from(IntervalSet<L> initial) {
        CommonMultiIntervalSet<L> set = new CommonMultiIntervalSet<>();
        for (L label : initial.labels()) {
            Set<Interval> intervals = new HashSet<>();
            long currentStart = initial.start(label);
            long currentEnd = initial.end(label);
            intervals.add(new Interval(currentStart, currentEnd));
            set.labelToIntervalsMap.put(label, intervals);
        }
        return set;
    }

    @Override
    public void insert(long start, long end, L label) {
        if (label == null || start > end) {
            throw new IllegalArgumentException("Invalid interval or label.");
        }
        ensureLabelSetExists(label);
        labelToIntervalsMap.get(label).add(new Interval(start, end));
    }

    @Override
    public Set<L> labels() {
        return Collections.unmodifiableSet(labelToIntervalsMap.keySet());
    }

    @Override
    public boolean remove(L label) {
        if (!labelToIntervalsMap.containsKey(label)) {
            return false;
        }
        labelToIntervalsMap.remove(label);
        return true;
    }

    @Override
    public long start(L label) {
        if (!labelToIntervalsMap.containsKey(label)) {
            throw new NoSuchElementException("Label not found: " + label);
        }
        // 寻找指定标签的最早时间段的开始时间
        Set<Interval> intervals = labelToIntervalsMap.get(label);
        return intervals.stream()
                .mapToLong(Interval::getStart)
                .min()
                .orElseThrow(() -> new NoSuchElementException("No intervals found for label: " + label));
    }

    @Override
    public long end(L label) {
        if (!labelToIntervalsMap.containsKey(label)) {
            throw new NoSuchElementException("Label not found: " + label);
        }
        // 寻找指定标签的最晚时间段的结束时间
        Set<Interval> intervals = labelToIntervalsMap.get(label);
        return intervals.stream()
                .mapToLong(Interval::getEnd)
                .max()
                .orElseThrow(() -> new NoSuchElementException("No intervals found for label: " + label));
    }

    @Override
    public boolean removeAll(L label) {
        if (label == null || !labelToIntervalsMap.containsKey(label)) {
            return false;
        }
        labelToIntervalsMap.get(label).clear();
        return true;
    }

    @Override
    public Set<Interval> intervals(L label) {
        Set<Interval> intervals = labelToIntervalsMap.getOrDefault(label, Collections.emptySet());
        return Collections.unmodifiableSet(intervals);
    }

    // 辅助方法，确保为每个标签都创建了时间段集合
    private void ensureLabelSetExists(L label) {
        labelToIntervalsMap.computeIfAbsent(label, k -> new HashSet<>());
    }


}
