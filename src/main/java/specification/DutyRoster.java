package specification;
import api.imp.CommonIntervalSet;
import entity.Employee;

import java.text.SimpleDateFormat;
import java.util.*;

import java.util.HashSet;
import java.util.Set;

public class DutyRoster {
    public CommonIntervalSet<Employee> intervalSet;
    public Set<Employee> employees;
    private long startDate;
    private long endDate;

    public DutyRoster(long startDate, long endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.intervalSet = new CommonIntervalSet<>();
        this.employees = new HashSet<>();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public boolean removeEmployee(Employee employee) {
        // 只有当员工没有被安排值班时，才能从系统中删除
        if (intervalSet.remove(employee)) {
            employees.remove(employee);
            return true;
        }
        return false;
    }

    public void assignShift(Employee employee, long start, long end) {
        // 为员工安排值班
        intervalSet.insert(start, end, employee);
    }

    public boolean isScheduleFull() {
        if (intervalSet.labels() == null || intervalSet.labels().isEmpty()) {
            return false;
        }
        Iterator<Employee> iterator = intervalSet.labels().iterator();
        // 获取第一个员工
        Employee previousEmployee = iterator.next();
        while (iterator.hasNext()) {
            Employee currentEmployee = iterator.next();
            // 检查当前员工的开始时间是否在前一个员工的结束时间之后或相等
            if (intervalSet.end(previousEmployee) < intervalSet.start(currentEmployee))
            {
                // 发现间隔，排班未满
                return false;
            }
            previousEmployee = currentEmployee;
        }
        // 最后检查最后一个员工的结束时间是否至少达到特定时间的结束时间
        return intervalSet.end(previousEmployee) >= endDate;
    }

    public void autoAssignShifts() {
        long duration = endDate - startDate;
        if (duration <= 0) {
            throw new IllegalStateException("End date must be after start date.");
        }
        // 员工数量
        int numEmployees = employees.size();
        if (numEmployees == 0) {
            return; // 如果没有员工，则直接返回
        }
        // 每个员工分配的时间段长度
        long intervalPerEmployee = duration / numEmployees;
        // 剩余时间段，用于分配给前几个员工以保证平均
        long remainingTime = duration % numEmployees;
        long shiftStart = startDate;
        Iterator<Employee> iterator = employees.iterator();

        for (int i = 0; i < numEmployees; i++) {
            Employee employee = iterator.next();

            // 计算当前员工的班次结束时间
            long shiftEnd = shiftStart + intervalPerEmployee;
            if (remainingTime > 0) {
                // 给当前员工增加一天
                shiftEnd += 1;
                remainingTime--;
            }
            // 插入班次
            intervalSet.insert(shiftStart, shiftEnd, employee);

            // 更新下一个班次的开始时间
            shiftStart = shiftEnd;
        }
    }

    public void displaySchedule() {
        // 将时间戳转换为天数
        long startDay = startDate / (24 * 60 * 60 * 1000);
        long endDay = endDate / (24 * 60 * 60 * 1000);

        // 实现排班表的可视化展示
        System.out.println("Duty Roster from " + new Date(startDate) + " to " + new Date(endDate));
        System.out.println("--------------------");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 用于格式化日期输出

        for (long day = startDay; day <= endDay; day++) {
            // 将天数转换回时间戳
            long dayTimestamp = day * (24 * 60 * 60 * 1000);
            String dayString = sdf.format(new Date(dayTimestamp)); // 获取格式化的日期字符串
            System.out.print("Day " + dayString + ": ");
            boolean hasShift = false;
            for (Employee employee : intervalSet.labels()) {
                // 使用天数进行判断
                if (intervalSet.start(employee) / (24 * 60 * 60 * 1000) <= day &&
                        intervalSet.end(employee) / (24 * 60 * 60 * 1000) > day) {
                    if (hasShift) {
                        System.out.print(", ");
                    }
                    System.out.print(employee);
                    hasShift = true;
                }
            }
            if (!hasShift) {
                System.out.print("No shifts scheduled");
            }
            System.out.println();
        }
    }

}


