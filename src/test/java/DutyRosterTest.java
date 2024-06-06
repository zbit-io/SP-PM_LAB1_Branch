import static org.junit.Assert.*;

import specification.DutyRoster;
import entity.Employee;
import org.junit.Before;
import org.junit.Test;

public class DutyRosterTest {
    private DutyRoster dutyRoster;
    private Employee employee1;
    private Employee employee2;

    @Before
    public void setUp() {
        dutyRoster = new DutyRoster(1681993600000L, 1687958400000L); // 假设的开始和结束日期
        employee1 = new Employee("Alice", "Manager", "1234567890");
        employee2 = new Employee("Bob", "Engineer", "0987654321");
        dutyRoster.addEmployee(employee1);
        dutyRoster.addEmployee(employee2);

    }

    @Test
    public void testAddEmployee() {
        // 测试员工添加
        assertEquals(2, dutyRoster.employees.size());


    }

    @Test
    public void testRemoveEmployee() {
        dutyRoster.assignShift(employee1, 1609459200000L, 1609545600000L); // 假设值班时间段
         // 测试员工删除
        assertTrue(dutyRoster.removeEmployee(employee1));
        assertFalse(dutyRoster.employees.contains(employee1)); // 假设 employees 是 public 或有公共方法可以访问
        assertFalse(dutyRoster.removeEmployee(employee1)); // 再次删除同一个员工应该返回 false
    }

    @Test
    public void testAssignShift() {
        // 测试为员工安排值班
        dutyRoster.assignShift(employee1, 1609459200000L, 1609545600000L); // 假设值班时间段
        assertTrue(dutyRoster.intervalSet.labels().contains(employee1)); // 假设 intervalSet 有 labels 方法
        dutyRoster.displaySchedule();
    }

    @Test
    public void testIsScheduleFull() {
        // 测试排班是否已满
        dutyRoster.autoAssignShifts();
        assertTrue(dutyRoster.isScheduleFull());
    }

    @Test
    public void testAutoAssignShifts() {
        // 测试自动排班
        dutyRoster.autoAssignShifts();
        for (Employee employee : dutyRoster.employees) {
            assertTrue(dutyRoster.intervalSet.labels().contains(employee)); // 员工应该有班次
        }
    }

}
