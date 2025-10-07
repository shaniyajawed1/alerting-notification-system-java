package com.alerting.model;

import java.time.LocalDateTime;

public class NotificationDelivery {
    private String id;
    private String userId;
    private String alertId;
    private LocalDateTime deliveredAt;
    private DeliveryChannel channel;
    private boolean success;

    public enum DeliveryChannel {
        IN_APP
    }

    public NotificationDelivery(String id, String userId, String alertId, DeliveryChannel channel) {
        this.id = id;
        this.userId = userId;
        this.alertId = alertId;
        this.channel = channel;
        this.deliveredAt = LocalDateTime.now();
        this.success = true;
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getAlertId() { return alertId; }
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public DeliveryChannel getChannel() { return channel; }
    public boolean isSuccess() { return success; }
}