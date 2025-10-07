package com.alerting.repository;

import com.alerting.model.UserAlertStatus;
import java.util.*;

public class UserAlertStatusRepository {
    private final Map<String, UserAlertStatus> statusMap = new HashMap<>();

    private String generateKey(String userId, String alertId) {
        return userId + ":" + alertId;
    }

    public void saveStatus(UserAlertStatus status) {
        String key = generateKey(status.getUserId(), status.getAlertId());
        statusMap.put(key, status);
    }

    public UserAlertStatus findStatus(String userId, String alertId) {
        String key = generateKey(userId, alertId);
        return statusMap.get(key);
    }

    public UserAlertStatus findOrCreateStatus(String userId, String alertId) {
        UserAlertStatus status = findStatus(userId, alertId);
        if (status == null) {
            status = new UserAlertStatus(userId, alertId);
            saveStatus(status);
        }
        return status;
    }

    public List<UserAlertStatus> findStatusesByUser(String userId) {
        List<UserAlertStatus> result = new ArrayList<>();
        for (UserAlertStatus status : statusMap.values()) {
            if (status.getUserId().equals(userId)) {
                result.add(status);
            }
        }
        return result;
    }

    public List<UserAlertStatus> findStatusesByAlert(String alertId) {
        List<UserAlertStatus> result = new ArrayList<>();
        for (UserAlertStatus status : statusMap.values()) {
            if (status.getAlertId().equals(alertId)) {
                result.add(status);
            }
        }
        return result;
    }
}