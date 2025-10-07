package com.alerting.model;

import java.time.LocalDateTime;
import java.util.*;

public class Alert {
    private String id;
    private String title;
    private String message;
    private Severity severity;
    private LocalDateTime createdAt;
    private LocalDateTime startTime;
    private LocalDateTime expiryTime;
    private boolean isActive;
    private boolean remindersEnabled;
    private Set<String> targetUsers;
    private Set<String> targetTeams;
    private boolean targetAllUsers;

    public enum Severity {
        INFO, WARNING, CRITICAL
    }

    public Alert(String id, String title, String message, Severity severity) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.severity = severity;
        this.createdAt = LocalDateTime.now();
        this.startTime = LocalDateTime.now();
        this.expiryTime = LocalDateTime.now().plusDays(7);
        this.isActive = true;
        this.remindersEnabled = true;
        this.targetUsers = new HashSet<>();
        this.targetTeams = new HashSet<>();
        this.targetAllUsers = false;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public Severity getSeverity() { return severity; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getExpiryTime() { return expiryTime; }
    public boolean isActive() { return isActive; }
    public boolean isRemindersEnabled() { return remindersEnabled; }
    public Set<String> getTargetUsers() { return targetUsers; }
    public Set<String> getTargetTeams() { return targetTeams; }
    public boolean isTargetAllUsers() { return targetAllUsers; }

    // Setters
    public void setExpiryTime(LocalDateTime expiryTime) { this.expiryTime = expiryTime; }
    public void setActive(boolean active) { isActive = active; }
    public void setRemindersEnabled(boolean remindersEnabled) { this.remindersEnabled = remindersEnabled; }
    public void setTargetAllUsers(boolean targetAllUsers) { this.targetAllUsers = targetAllUsers; }

    // Helper methods
    public void addTargetUser(String userId) { targetUsers.add(userId); }
    public void addTargetTeam(String teamId) { targetTeams.add(teamId); }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }

    public boolean shouldBeActive() {
        return isActive &&
                LocalDateTime.now().isAfter(startTime) &&
                !isExpired();
    }
}