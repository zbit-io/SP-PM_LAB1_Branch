package specification;
import api.imp.CommonMultiIntervalSet;
import entity.Interval;
import entity.Process;
import lombok.Data;

import java.util.*;
@Data
public class ProcessScheduleManager {
    private CommonMultiIntervalSet<Process> schedule;
    private Set<Process> processes;

    public ProcessScheduleManager() {
        this.schedule = new CommonMultiIntervalSet<>();
        this.processes = new HashSet<>();
    }

    public void addProcess(Process process) {
        processes.add(process);
    }

    public void assignSchedule(Process process, long start, long end) {
        schedule.insert(start, end, process);
    }

    public void autoAssignSchedules() {
        // 获取当前时间
        long currentTime = System.currentTimeMillis();
        // 每个进程的执行时间为1000毫秒
        long duration = 1000;

        for (Process process : processes) {
            if (schedule.labels().contains(process)) {
                // 如果进程已经有了排班，则跳过
                continue;
            }
            // 为进程分配一个固定长度的时间段
            long start = currentTime;
            long end = start + duration;

            // 调用assignSchedule方法来实际添加时间段到schedule中
            assignSchedule(process, start, end);

            // 更新当前时间，为下一个进程的排班做准备
            currentTime = end;
        }
    }

    public void displaySchedule() {
        // 遍历schedule并打印每个进程的调度信息
        for (Process process : schedule.labels()) {
            Set<Interval> intervals = schedule.intervals(process);
            for (Interval interval : intervals) {
                System.out.println("Process " + process.getId() + " is executing from " +
                        new Date(interval.getStart()) + " to " + new Date(interval.getEnd()));
            }
        }
    }



    // 开始进程调度模拟
    public void startSimulation() {
        long currentTime = 0;
        // 获取迭代器便于遍历
        Iterator<Process> iterator = processes.iterator();
        while (iterator.hasNext()) {
            // 选择进程
            Process selectedProcess = selectProcess(iterator);
            long executionTime = determineExecutionTime(selectedProcess);
            currentTime += executionTime;
            // 记录进程的执行时间段
            schedule.insert(currentTime - executionTime, currentTime, selectedProcess);
            // 检查进程是否执行结束
            if (isProcessFinished(selectedProcess, executionTime)) {
                // 从迭代器中删除已完成的进程，避免再次调度
                iterator.remove();
            }
        }
    }

    // 随机选择进程的方法
    private Process selectProcess(Iterator<Process> iterator) {
        Random random = new Random();
        int index = random.nextInt(processes.size());
        int count = 0;
        while (iterator.hasNext()) {
            Process process = iterator.next();
            if (count == index) {
                return process;
            }
            count++;
        }
        // 如果有null值或计算错误，返回null
        return null;
    }

    /**
     确定进程执行时间的方法
     *
      */
    private long determineExecutionTime(Process process) {
        Random random = new Random();
        return random.nextInt((int) (process.getMaxExecutionTime() - process.getMinExecutionTime()) + 1) + process.getMinExecutionTime();
    }

    // 检查进程是否执行结束的方法
    private boolean isProcessFinished(Process process, long executionTime) {
        long potentialTotalExecutionTime = (executionTime >= 0) ? (process.getMinExecutionTime() - executionTime) : process.getMinExecutionTime();
        return potentialTotalExecutionTime <= 0;
    }

    public boolean isProcessScheduled(Process process) {
        // 检查进程是否在schedule中
        return schedule.labels().contains(process);
    }


}

