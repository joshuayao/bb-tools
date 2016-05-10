package com.intel.bb.tool.log.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yaoy on 5/6/2016.
 */
public class MRJobCounter {

    private JobCounters jobCounters;

    public JobCounters getJobCounters() {
        return jobCounters;
    }

    public void setJobCounters(JobCounters jobCounters) {
        this.jobCounters = jobCounters;
    }

    public static class CounterGroup implements Serializable {

        private String counterGroupName;
        private List<Counter> counter;

        public CounterGroup() {}

        public String getCounterGroupName() {
            return counterGroupName;
        }

        public void setCounterGroupName(String counterGroupName) {
            this.counterGroupName = counterGroupName;
        }

        public List<Counter> getCounter() {
            return counter;
        }

        public void setCounter(List<Counter> counter) {
            this.counter = counter;
        }
    }

    public static class Counter implements Serializable {

        private String name;
        private long mapCounterValue;
        private long reduceCounterValue;
        private long totalCounterValue;

        public Counter() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getMapCounterValue() {
            return mapCounterValue;
        }

        public void setMapCounterValue(long mapCounterValue) {
            this.mapCounterValue = mapCounterValue;
        }

        public long getReduceCounterValue() {
            return reduceCounterValue;
        }

        public void setReduceCounterValue(long reduceCounterValue) {
            this.reduceCounterValue = reduceCounterValue;
        }

        public long getTotalCounterValue() {
            return totalCounterValue;
        }

        public void setTotalCounterValue(long totalCounterValue) {
            this.totalCounterValue = totalCounterValue;
        }
    }

    public static class JobCounters implements Serializable {

        private String id;
        private List<CounterGroup> counterGroup;

        public JobCounters() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<CounterGroup> getCounterGroup() {
            return counterGroup;
        }

        public void setCounterGroup(List<CounterGroup> counterGroup) {
            this.counterGroup = counterGroup;
        }
    }
}
