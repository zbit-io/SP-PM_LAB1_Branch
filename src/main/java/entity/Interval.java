package entity;

import java.util.Objects;

public class Interval {
    final long start; // 时间段的开始
    final long end;   // 时间段的结束

    public Interval(long start, long end) {
        this.start = start;
        this.end = end;
    }

    // Getter 方法
    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Interval interval = (Interval) o;
        return start == interval.start && end == interval.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }


    @Override
    public String toString() {
        return "(" + start + ", " + end + ")";
    }
}
