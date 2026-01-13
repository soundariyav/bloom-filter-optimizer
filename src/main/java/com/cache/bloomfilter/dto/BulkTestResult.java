package com.cache.bloomfilter.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class BulkTestResult {
    private int totalQueries;
    private int withFilterDbQueries;
    private int withoutFilterDbQueries;
    private double withFilterTotalTimeMs;
    private double withoutFilterTotalTimeMs;
    private double queriesSavedPercentage;
    private double timeSavedPercentage;
    private int falsePositives;
    // Constructors
    public BulkTestResult() {}

    // Getters and Setters
    public int getTotalQueries() { return totalQueries; }
    public void setTotalQueries(int totalQueries) { this.totalQueries = totalQueries; }

    public int getWithFilterDbQueries() { return withFilterDbQueries; }
    public void setWithFilterDbQueries(int withFilterDbQueries) { this.withFilterDbQueries = withFilterDbQueries; }

    public int getWithoutFilterDbQueries() { return withoutFilterDbQueries; }
    public void setWithoutFilterDbQueries(int withoutFilterDbQueries) { this.withoutFilterDbQueries = withoutFilterDbQueries; }

    public double getWithFilterTotalTimeMs() { return withFilterTotalTimeMs; }
    public void setWithFilterTotalTimeMs(double withFilterTotalTimeMs) { this.withFilterTotalTimeMs = withFilterTotalTimeMs; }

    public double getWithoutFilterTotalTimeMs() { return withoutFilterTotalTimeMs; }
    public void setWithoutFilterTotalTimeMs(double withoutFilterTotalTimeMs) { this.withoutFilterTotalTimeMs = withoutFilterTotalTimeMs; }

    public double getQueriesSavedPercentage() { return queriesSavedPercentage; }
    public void setQueriesSavedPercentage(double queriesSavedPercentage) { this.queriesSavedPercentage = queriesSavedPercentage; }

    public double getTimeSavedPercentage() { return timeSavedPercentage; }
    public void setTimeSavedPercentage(double timeSavedPercentage) { this.timeSavedPercentage = timeSavedPercentage; }

    public int getFalsePositives() { return falsePositives; }
    public void setFalsePositives(int falsePositives) { this.falsePositives = falsePositives; }
}
