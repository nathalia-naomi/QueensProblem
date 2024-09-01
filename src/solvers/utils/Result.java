package solvers.utils;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {
    private List<int[][]> solutions;
    private long executionTime;

    public Result(List<int[][]> solutions, long executionTime) {
        this.solutions = solutions;
        this.executionTime = executionTime;
    }

    public List<int[][]> getSolutions() {
        return solutions;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public boolean hasSolution() {
        return !solutions.isEmpty();
    }

    public int[][] getSolution() {
        return solutions.get(0);
    }
}
