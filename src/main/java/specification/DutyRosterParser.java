package specification;

import entity.Employee;
import entity.Interval;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;

public class DutyRosterParser {

    // 预编译的正则表达式
    private static final Pattern EMPLOYEE_BLOCK_PATTERN = Pattern.compile("Employee\\{(.*?)\\}", Pattern.DOTALL);
    private static final Pattern SINGLE_EMPLOYEE_PATTERN = Pattern.compile("\\w+\\{([^}]+)\\}",Pattern.DOTALL);
    private static final Pattern PERIOD_PATTERN = Pattern.compile("Period\\{([^,]+),([^}]+)\\}");
    private static final Pattern ROSTER_BLOCK_PATTERN = Pattern.compile("Roster\\{(.*?)\\}", Pattern.MULTILINE);
    private static final Pattern SINGLE_ROSTER_PATTERN = Pattern.compile("\\w+\\{([^,]+),([^}]+)\\}");

    // 解析文本文件并构建排班表
    public static DutyRoster parseDutyRoster(String filePath) throws IOException, ParseException {
        String content = readFile(filePath);
        Map<String, Employee> employees = parseEmployees(content);
        Map.Entry<Long, Long> period = parsePeriod(content);
        Map<String, Interval> roster = parseRoster(content, employees);

        DutyRoster dutyRoster = new DutyRoster(period.getKey(), period.getValue());
        for (Map.Entry<String, Interval> entry : roster.entrySet()) {
            dutyRoster.addEmployee(new Employee(entry.getKey(), "Position", "Phone"));
            dutyRoster.assignShift(employees.get(entry.getKey()), entry.getValue().getStart(), entry.getValue().getEnd());
        }

        return dutyRoster;
    }

    // 读取文件内容
    private static String readFile(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                contentBuilder.append(currentLine).append("\n");
            }
        }
        return contentBuilder.toString();
    }

    // 解析员工信息
    private static Map<String, Employee> parseEmployees(String content) {
        Map<String, Employee> employees = new HashMap<>();
        Matcher employeeMatcher = EMPLOYEE_BLOCK_PATTERN.matcher(content);
        while (employeeMatcher.find()) {
            String employeesBlock = employeeMatcher.group(1);
            Matcher singleEmployeeMatcher = SINGLE_EMPLOYEE_PATTERN.matcher(employeesBlock);
            while (singleEmployeeMatcher.find()) {
                String name = singleEmployeeMatcher.group();
                String[] details = singleEmployeeMatcher.group(1).split(",");
                employees.put(name, new Employee(name, details[0].trim(), details[1].trim()));
            }
        }
        return employees;
    }

    // 解析排班时间段
    private static Map.Entry<Long, Long> parsePeriod(String content) {
        Matcher periodMatcher = PERIOD_PATTERN.matcher(content);
        if (periodMatcher.find()) {
            long start = parseDate(periodMatcher.group(1));
            long end = parseDate(periodMatcher.group(2));
            return new AbstractMap.SimpleEntry<>(start, end);
        }
        throw new IllegalArgumentException("Period information is missing or malformed.");
    }

    // 解析排班信息
    private static Map<String, Interval> parseRoster(String content, Map<String, Employee> employees) {
        Map<String, Interval> roster = new HashMap<>();
        Matcher rosterMatcher = ROSTER_BLOCK_PATTERN.matcher(content);
        if (rosterMatcher.find()) {
            String rosterBlock = rosterMatcher.group(1);
            Matcher singleRosterMatcher = SINGLE_ROSTER_PATTERN.matcher(rosterBlock);
            while (singleRosterMatcher.find()) {
                String name = singleRosterMatcher.group(1);
                String startDateStr = singleRosterMatcher.group(2);
                String endDateStr = singleRosterMatcher.group(3);
                long start = parseDate(startDateStr);
                long end = parseDate(endDateStr);
                roster.put(name, new Interval(start, end));
            }
        }
        return roster;
    }

    // 解析日期字符串
    private static long parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateString).getTime();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Date is malformed: " + dateString);
        }
    }

    // 内部类 Employee 和 Interval


    public static void main(String[] args) {
        try {
            DutyRoster dutyRoster = parseDutyRoster("/Users/echos/Desktop/CODE/JAVA/MYSELF/ADT-Course/src/main/resources/dutyRoster.txt");
            System.out.println(dutyRoster.intervalSet.labels());
            // 打印排班表或进行其他操作
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}

//public class TimeConflictCalculator {
//
//    public static <L> double calcConflictRatio(MultiIntervalSet<L> set) {
//        long totalConflictTime = 0;
//        long totalTime = 0;
//
//        for (L label1 : set.labels()) {
//            for (L label2 : set.labels()) {
//                if (!label1.equals(label2)) {
//                    for (Interval interval1 : set.intervals(label1)) {
//                        for (Interval interval2 : set.intervals(label2)) {
//                            long overlap = Math.min(interval1.getEnd(), interval2.getEnd()) - Math.max(interval1.getStart(), interval2.getStart());
//                            if (overlap > 0) {
//                                totalConflictTime += overlap;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        for (L label : set.labels()) {
//            for (Interval interval : set.intervals(label)) {
//                totalTime += interval.getEnd() - interval.getStart();
//            }
//        }
//
//        return totalTime == 0 ? 0 : (double) totalConflictTime / totalTime;
//    }
//}
//
//
//
//public class FreeTimeCalculator {
//
//    public static <L> double calcFreeTimeRatio(IntervalSet<L> set, long startTime, long endTime) {
//        List<Interval> intervals = new ArrayList<>();
//        for (L label : set.labels()) {
//            intervals.add(new Interval(set.start(label), set.end(label)));
//        }
//
//        intervals.sort((a, b) -> Long.compare(a.getStart(), b.getStart()));
//
//        long totalFreeTime = 0;
//        long previousEnd = startTime;
//
//        for (Interval interval : intervals) {
//            if (interval.getStart() > previousEnd) {
//                totalFreeTime += interval.getStart() - previousEnd;
//            }
//            previousEnd = Math.max(previousEnd, interval.getEnd());
//        }
//
//        if (previousEnd < endTime) {
//            totalFreeTime += endTime - previousEnd;
//        }
//
//        long totalTime = endTime - startTime;
//
//        return totalTime == 0 ? 0 : (double) totalFreeTime / totalTime;
//    }
//}
//
