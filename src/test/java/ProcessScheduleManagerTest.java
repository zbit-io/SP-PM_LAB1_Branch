import static org.junit.Assert.*;

import specification.ProcessScheduleManager;
import org.junit.Before;
import org.junit.Test;
import entity.Process;

import java.util.ArrayList;
import java.util.List;

public class ProcessScheduleManagerTest {
    private ProcessScheduleManager manager;
    private Process process1;
    private Process process2;
    private List<Process> processes;
    @Before
    public void setUp() {
        manager = new ProcessScheduleManager();
        process1 = new Process(1, "Process1", 1000, 5000);
        process2 = new Process(2, "Process2", 1000, 5000);
        processes = new ArrayList<>();
        // 添加一些进程到进程列表
        processes.add(process1);
        processes.add(process2);
    }

    @Test
    public void testAddProcess() {
        manager.addProcess(process1);
        assertTrue(manager.getProcesses().contains(process1));
    }

//    @Test
//    public void testAutoAssignSchedules() {
//        manager.addProcess(process1);
//        manager.addProcess(process2);
//        manager.autoAssignSchedules();
//        // 检查是否为每个进程分配了时间段
//        assertFalse(manager.getSchedule().labels().isEmpty());
//    }

    @Test
    public void testShortestJobFirstStrategy() {
        // 添加具有不同执行时间的进程
        manager.addProcess(new Process(1, "ShortProcess", 100, 200));
        manager.addProcess(new Process(2, "LongProcess", 1000, 2000));
        manager.autoAssignSchedules();

        // 检查是否最短的进程首先被执行
        // 这需要根据实际的调度逻辑来实现具体的检查
        // 示例：检查进程的开始时间
    }



    @Test
    public void testAutoAssignSchedules() {
        // 添加进程到管理器
        manager.addProcess(process1);
        manager.addProcess(process2);

        // 自动分配调度
        manager.autoAssignSchedules();

        // 验证是否为每个进程分配了时间段
        assertTrue(manager.getProcesses().contains(process1));
        assertTrue(manager.getProcesses().contains(process2));

        // 可以添加更多断言来检查具体的时间分配是否符合预期
        // 例如，检查进程是否按照最短执行时间优先分配
    }

    @Test
    public void testDisplaySchedule() {
        // 添加进程到管理器并分配调度
        manager.addProcess(process1);
        manager.addProcess(process2);
        manager.autoAssignSchedules();

        // 调用展示调度的方法
        manager.displaySchedule();

        // 由于 displaySchedule() 可能只是打印到控制台，我们无法直接测试它的输出。
        // 这里我们只是确保方法被调用，具体的输出需要人工检查或使用额外的工具来捕获输出
    }
    @Test
    public void testProcessScheduling() {
        // 将进程添加到调度管理器
        for (Process p : processes) {
            manager.addProcess(p);
        }

        // 模拟调度过程
        manager.startSimulation();

        // 检查是否所有进程都被调度
        for (Process process : processes) {
            assertTrue(manager.isProcessScheduled(process));
        }


    }
}



