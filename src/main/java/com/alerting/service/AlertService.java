package com.alerting.service;

import com.alerting.model.*;
import com.alerting.repository.*;
import java.time.LocalDateTime;
import java.util.*;

public class AlertService {
    private final AlertRepository alertRepository;
    private final UserRepository userRepository;
    private final UserAlertStatusRepository statusRepository;
    private final NotificationDeliveryRepository deliveryRepository;
    private final TeamRepository teamRepository;

    public AlertService() {
        this.alertRepository = new AlertRepository();
        this.userRepository = new UserRepository();
        this.statusRepository = new UserAlertStatusRepository();
        this.deliveryRepository = new NotificationDeliveryRepository();
        this.teamRepository = new TeamRepository(); // Added missing repository
    }

    public Alert createAlert(String title, String message, Alert.Severity severity) {
        String alertId = "alert_" + System.currentTimeMillis();
        Alert alert = new Alert(alertId, title, message, severity);
        alertRepository.saveAlert(alert);
        return alert;
    }

    public void setAlertVisibility(String alertId, boolean targetAllUsers,
                                   List<String> targetUsers, List<String> targetTeams) {
        Alert alert = alertRepository.findById(alertId);
        if (alert != null) {
            alert.setTargetAllUsers(targetAllUsers);
            if (targetUsers != null) {
                for (String userId : targetUsers) {
                    alert.addTargetUser(userId);
                }
            }
            if (targetTeams != null) {
                for (String teamId : targetTeams) {
                    alert.addTargetTeam(teamId);
                }
            }
            alertRepository.updateAlert(alert);
        }
    }

    public List<Alert> getAlertsForUser(String userId) {
        User user = userRepository.findById(userId);
        if (user == null) return new ArrayList<>();

        List<Alert> relevantAlerts = new ArrayList<>();
        List<Alert> activeAlerts = alertRepository.findActiveAlerts();

        for (Alert alert : activeAlerts) {
            if (isAlertRelevantForUser(alert, user)) {
                relevantAlerts.add(alert);

                // Initialize user status if not exists
                statusRepository.findOrCreateStatus(userId, alert.getId());
            }
        }

        return relevantAlerts;
    }

    private boolean isAlertRelevantForUser(Alert alert, User user) {
        if (alert.isTargetAllUsers()) {
            return true;
        }
        if (alert.getTargetUsers().contains(user.getId())) {
            return true;
        }
        if (alert.getTargetTeams().contains(user.getTeamId())) {
            return true;
        }
        return false;
    }

    public void markAlertAsRead(String userId, String alertId) {
        UserAlertStatus status = statusRepository.findOrCreateStatus(userId, alertId);
        status.markRead();
        statusRepository.saveStatus(status);
    }

    public void snoozeAlert(String userId, String alertId) {
        UserAlertStatus status = statusRepository.findOrCreateStatus(userId, alertId);
        status.snoozeForToday();
        statusRepository.saveStatus(status);
    }

    public void processReminders() {
        List<Alert> activeAlerts = alertRepository.findActiveAlerts();
        List<User> allUsers = userRepository.findAll();

        for (Alert alert : activeAlerts) {
            if (!alert.isRemindersEnabled()) continue;

            for (User user : allUsers) {
                if (isAlertRelevantForUser(alert, user)) {
                    UserAlertStatus status = statusRepository.findOrCreateStatus(user.getId(), alert.getId());

                    if (status.shouldSendReminder()) {
                        // Send reminder
                        sendInAppNotification(user.getId(), alert.getId());
                        status.updateLastReminder();
                        statusRepository.saveStatus(status);
                    }
                }
            }
        }
    }

    private void sendInAppNotification(String userId, String alertId) {
        String deliveryId = "delivery_" + System.currentTimeMillis();
        NotificationDelivery delivery = new NotificationDelivery(
                deliveryId, userId, alertId, NotificationDelivery.DeliveryChannel.IN_APP
        );
        deliveryRepository.saveDelivery(delivery);
        System.out.println("Sent reminder to user " + userId + " for alert " + alertId);
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public void updateAlertExpiry(String alertId, LocalDateTime expiryTime) {
        Alert alert = alertRepository.findById(alertId);
        if (alert != null) {
            alert.setExpiryTime(expiryTime);
            alertRepository.updateAlert(alert);
        }
    }

    public void disableAlert(String alertId) {
        Alert alert = alertRepository.findById(alertId);
        if (alert != null) {
            alert.setActive(false);
            alertRepository.updateAlert(alert);
        }
    }
}