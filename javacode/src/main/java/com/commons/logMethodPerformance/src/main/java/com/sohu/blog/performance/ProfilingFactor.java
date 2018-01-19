package com.sohu.blog.performance;

import java.io.Serializable;

/**
 * @author 金海民
 */
public class ProfilingFactor implements Serializable {
    private static final long serialVersionUID = 1117422298826366786L;
    
    private long executionCount;
    private long executionTime;
    
    public ProfilingFactor(long executionCount, long executionTime) {
        this.executionCount = executionCount;
        this.executionTime = executionTime;
    }
    
    public long getExecutionCount() {
        return executionCount;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void addExecutionCount() {
        ++executionCount;
    }
    
    public void addExecutionTime(long executionTime) {
        this.executionTime +=  executionTime;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
            .append("[Execution Count: ")
            .append(executionCount)
            .append(", Average Execution Time: ")
            .append(executionCount == 0L ? 0 : executionTime / executionCount)
            .append("ms]");
        return sb.toString();
    }
}
