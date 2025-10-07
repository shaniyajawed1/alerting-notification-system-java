package com.alerting.model;

import java.time.LocalDateTime;

public class UserAlertStatus {
    private String userId;
    private String alertId;
    private boolean read;
    private boolean snoozed;
    private LocalDateTime snoozedUntil;
    private LocalDateTime lastReminderSent;
    private LocalDateTime createdAt;

    public UserAlertStatus(String userId, String alertId) {
        this.userId = userId;
        this.alertId = alertId;
        this.read = false;
        this.snoozed = false;
        this.lastReminderSent = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public String getUserId() { return userId; }
    public String getAlertId() { return alertId; }
    public boolean isRead() { return read; }
    public boolean isSnoozed() { return snoozed; }
    public LocalDateTime getSnoozedUntil() { return snoozedUntil; }
    public LocalDateTime getLastReminderSent() { return lastReminderSent; }

    // Actions
    public void markRead() {
        this.read = true;
    }

    public void markUnread() {
        this.read = false;
    }

    public void snoozeForToday() {
        this.snoozed = true;
        this.snoozedUntil = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        this.lastReminderSent = LocalDateTime.now(); // Reset reminder timer
    }

    public void checkAndResetSnooze() {
        if (snoozed && LocalDateTime.now().isAfter(snoozedUntil)) {
            this.snoozed = false;
            this.snoozedUntil = null;
        }
    }

    public boolean shouldSendReminder() {
        checkAndResetSnooze();
        if (read || snoozed) return false;
        return LocalDateTime.now().isAfter(lastReminderSent.plusHours(2));
    }

    public void updateLastReminder() {
        this.lastReminderSent = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("UserAlertStatus{userId='%s', alertId='%s', read=%s, snoozed=%s}",
                userId, alertId, read, snoozed);
    }
}