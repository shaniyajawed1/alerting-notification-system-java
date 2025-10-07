package com.alerting.repository;

import com.alerting.model.NotificationDelivery;
import java.util.*;

public class NotificationDeliveryRepository {
    private final List<NotificationDelivery> deliveries = new ArrayList<>();

    public void saveDelivery(NotificationDelivery delivery) {
        deliveries.add(delivery);
    }

    public List<NotificationDelivery> findByUser(String userId) {
        List<NotificationDelivery> result = new ArrayList<>();
        for (NotificationDelivery delivery : deliveries) {
            if (delivery.getUserId().equals(userId)) {
                result.add(delivery);
            }
        }
        return result;
    }

    public List<NotificationDelivery> findByAlert(String alertId) {
        List<NotificationDelivery> result = new ArrayList<>();
        for (NotificationDelivery delivery : deliveries) {
            if (delivery.getAlertId().equals(alertId)) {
                result.add(delivery);
            }
        }
        return result;
    }

    public int getDeliveryCountForAlert(String alertId) {
        return findByAlert(alertId).size();
    }
}