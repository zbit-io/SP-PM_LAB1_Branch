package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Process {
    private int id;
    private String name;
    private long minExecutionTime;
    private long maxExecutionTime;
//    private long remainingExecutionTime;

//    public void decreaseRemainingTime(long timeElapsed) {
//        if (remainingExecutionTime > timeElapsed) {
//            remainingExecutionTime -= timeElapsed;
//        } else {
//            remainingExecutionTime = 0;
//        }
//    }


}
