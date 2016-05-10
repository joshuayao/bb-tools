package com.intel.bb.tool.log.dto;

import java.util.List;

/**
 * Created by yaoy on 5/9/2016.
 */
public class SparkAppStages {

    private String status;
    private String stageId;
    private String attemptId;
    private int numActiveTasks;
    private int numCompleteTasks;
    private int numFailedTasks;
    private int executorRunTime;
    private String submissionTime;
    private String firstTaskLaunchedTime;
    private String completionTime;
    private long inputBytes;
    private long inputRecords;
    private long outputBytes;
    private long outputRecords;
    private long shuffleReadBytes;
    private long shuffleReadRecords;
    private long shuffleWriteBytes;
    private long shuffleWriteRecords;
    private long memoryBytesSpilled;
    private long diskBytesSpilled;
    private String name;
    private String details;
    private String schedulingPool;
    private List<AccumulatorUpdates> accumulatorUpdates;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(String attemptId) {
        this.attemptId = attemptId;
    }

    public int getNumActiveTasks() {
        return numActiveTasks;
    }

    public void setNumActiveTasks(int numActiveTasks) {
        this.numActiveTasks = numActiveTasks;
    }

    public int getNumCompleteTasks() {
        return numCompleteTasks;
    }

    public void setNumCompleteTasks(int numCompleteTasks) {
        this.numCompleteTasks = numCompleteTasks;
    }

    public int getNumFailedTasks() {
        return numFailedTasks;
    }

    public void setNumFailedTasks(int numFailedTasks) {
        this.numFailedTasks = numFailedTasks;
    }

    public int getExecutorRunTime() {
        return executorRunTime;
    }

    public void setExecutorRunTime(int executorRunTime) {
        this.executorRunTime = executorRunTime;
    }

    public String getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(String submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getFirstTaskLaunchedTime() {
        return firstTaskLaunchedTime;
    }

    public void setFirstTaskLaunchedTime(String firstTaskLaunchedTime) {
        this.firstTaskLaunchedTime = firstTaskLaunchedTime;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }

    public long getInputBytes() {
        return inputBytes;
    }

    public void setInputBytes(long inputBytes) {
        this.inputBytes = inputBytes;
    }

    public long getInputRecords() {
        return inputRecords;
    }

    public void setInputRecords(long inputRecords) {
        this.inputRecords = inputRecords;
    }

    public long getOutputBytes() {
        return outputBytes;
    }

    public void setOutputBytes(long outputBytes) {
        this.outputBytes = outputBytes;
    }

    public long getOutputRecords() {
        return outputRecords;
    }

    public void setOutputRecords(long outputRecords) {
        this.outputRecords = outputRecords;
    }

    public long getShuffleReadBytes() {
        return shuffleReadBytes;
    }

    public void setShuffleReadBytes(long shuffleReadBytes) {
        this.shuffleReadBytes = shuffleReadBytes;
    }

    public long getShuffleReadRecords() {
        return shuffleReadRecords;
    }

    public void setShuffleReadRecords(long shuffleReadRecords) {
        this.shuffleReadRecords = shuffleReadRecords;
    }

    public long getShuffleWriteBytes() {
        return shuffleWriteBytes;
    }

    public void setShuffleWriteBytes(long shuffleWriteBytes) {
        this.shuffleWriteBytes = shuffleWriteBytes;
    }

    public long getShuffleWriteRecords() {
        return shuffleWriteRecords;
    }

    public void setShuffleWriteRecords(long shuffleWriteRecords) {
        this.shuffleWriteRecords = shuffleWriteRecords;
    }

    public long getMemoryBytesSpilled() {
        return memoryBytesSpilled;
    }

    public void setMemoryBytesSpilled(long memoryBytesSpilled) {
        this.memoryBytesSpilled = memoryBytesSpilled;
    }

    public long getDiskBytesSpilled() {
        return diskBytesSpilled;
    }

    public void setDiskBytesSpilled(long diskBytesSpilled) {
        this.diskBytesSpilled = diskBytesSpilled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSchedulingPool() {
        return schedulingPool;
    }

    public void setSchedulingPool(String schedulingPool) {
        this.schedulingPool = schedulingPool;
    }

    public List<AccumulatorUpdates> getAccumulatorUpdates() {
        return accumulatorUpdates;
    }

    public void setAccumulatorUpdates(List<AccumulatorUpdates> accumulatorUpdates) {
        this.accumulatorUpdates = accumulatorUpdates;
    }

    public static class AccumulatorUpdates {
        private String id;
        private String name;
        private long value;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }
    }

}
