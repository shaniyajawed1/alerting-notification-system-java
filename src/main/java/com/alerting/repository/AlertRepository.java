package com.alerting.repository;

import com.alerting.model.Alert;
import java.util.*;

public class AlertRepository {
    private final Map<String, Alert> alertsMap = new HashMap<>();
    private final List<Alert> alertsList = new ArrayList<>();

    public void saveAlert(Alert alert) {
        alertsMap.put(alert.getId(), alert);
        // Check if alert already exists in list to avoid duplicates
        boolean exists = false;
        for (int i = 0; i < alertsList.size(); i++) {
            if (alertsList.get(i).getId().equals(alert.getId())) {
                alertsList.set(i, alert);
                exists = true;
                break;
            }
        }
        if (!exists) {
            alertsList.add(alert);
        }
    }

    public Alert findById(String alertId) {
        return alertsMap.get(alertId);
    }

    public List<Alert> findAll() {
        return new ArrayList<>(alertsList); // Return copy of the list
    }

    public List<Alert> findActiveAlerts() {
        List<Alert> activeAlerts = new ArrayList<>();
        for (Alert alert : alertsList) {
            if (alert.shouldBeActive()) {
                activeAlerts.add(alert);
            }
        }
        return activeAlerts;
    }

    public void updateAlert(Alert alert) {
        alertsMap.put(alert.getId(), alert);
        // Update in list
        for (int i = 0; i < alertsList.size(); i++) {
            if (alertsList.get(i).getId().equals(alert.getId())) {
                alertsList.set(i, alert);
                break;
            }
        }
    }

    // Helper method to get total alert count
    public int getTotalAlertCount() {
        return alertsList.size();
    }
}