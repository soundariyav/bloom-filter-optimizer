package com.cache.bloomfilter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class CheckResponse {
    private String username;
    private boolean exists;
    private String method;
    private double durationMs;
    private boolean databaseQueried;
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean isExists() { return exists; }
    public void setExists(boolean exists) { this.exists = exists; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public double getDurationMs() { return durationMs; }
    public void setDurationMs(double durationMs) { this.durationMs = durationMs; }

    public boolean isDatabaseQueried() { return databaseQueried; }
    public void setDatabaseQueried(boolean databaseQueried) { this.databaseQueried = databaseQueried; }

}
