package com.alerting.controller;

import com.alerting.model.Alert;
import com.alerting.service.AlertService;
import com.alerting.service.AnalyticsService;
import com.google.gson.Gson;
import java.util.*;
import static spark.Spark.*;

public class AlertController {
    private final AlertService alertService;
    private final AnalyticsService analyticsService;
    private final Gson gson;

    public AlertController() {
        this.alertService = new AlertService();
        this.analyticsService = new AnalyticsService();
        this.gson = new Gson();
        setupRoutes();
    }

    private void setupRoutes() {
        // Admin: Create Alert
        post("/admin/alerts", (req, res) -> {
            try {
                Map<String, Object> request = gson.fromJson(req.body(), Map.class);
                String title = (String) request.get("title");
                String message = (String) request.get("message");
                String severityStr = (String) request.get("severity");

                Alert.Severity severity = Alert.Severity.valueOf(severityStr.toUpperCase());
                Alert alert = alertService.createAlert(title, message, severity);

                // Set visibility - handle null values safely
                Boolean targetAll = (Boolean) request.get("targetAllUsers");
                List<String> targetUsers = (List<String>) request.get("targetUsers");
                List<String> targetTeams = (List<String>) request.get("targetTeams");

                alertService.setAlertVisibility(alert.getId(),
                        targetAll != null ? targetAll : false,
                        targetUsers != null ? targetUsers : new ArrayList<>(),
                        targetTeams != null ? targetTeams : new ArrayList<>());

                res.type("application/json");
                return Map.of("success", true, "alertId", alert.getId());
            } catch (Exception e) {
                res.status(400);
                return Map.of("success", false, "error", e.getMessage());
            }
        }, gson::toJson);

        // Admin: List All Alerts - SIMPLIFIED
        get("/admin/alerts", (req, res) -> {
            try {
                List<Alert> alerts = alertService.getAllAlerts();
                res.type("application/json");

                // Create simple response without complex object serialization
                List<Map<String, Object>> simpleAlerts = new ArrayList<>();
                for (Alert alert : alerts) {
                    Map<String, Object> simpleAlert = new HashMap<>();
                    simpleAlert.put("id", alert.getId());
                    simpleAlert.put("title", alert.getTitle());
                    simpleAlert.put("message", alert.getMessage());
                    simpleAlert.put("severity", alert.getSeverity().toString());
                    simpleAlert.put("isActive", alert.isActive());
                    simpleAlerts.add(simpleAlert);
                }

                return Map.of("success", true, "alerts", simpleAlerts);
            } catch (Exception e) {
                res.status(500);
                return Map.of("success", false, "error", e.getMessage());
            }
        }, gson::toJson);

        // User: Get My Alerts - SIMPLIFIED
        get("/users/:userId/alerts", (req, res) -> {
            try {
                String userId = req.params(":userId");
                List<Alert> alerts = alertService.getAlertsForUser(userId);
                res.type("application/json");

                // Create simple response
                List<Map<String, Object>> simpleAlerts = new ArrayList<>();
                for (Alert alert : alerts) {
                    Map<String, Object> simpleAlert = new HashMap<>();
                    simpleAlert.put("id", alert.getId());
                    simpleAlert.put("title", alert.getTitle());
                    simpleAlert.put("message", alert.getMessage());
                    simpleAlert.put("severity", alert.getSeverity().toString());
                    simpleAlert.put("isActive", alert.isActive());
                    simpleAlerts.add(simpleAlert);
                }

                return Map.of("success", true, "alerts", simpleAlerts);
            } catch (Exception e) {
                res.status(500);
                return Map.of("success", false, "error", e.getMessage());
            }
        }, gson::toJson);

        // User: Mark Alert as Read
        post("/users/:userId/alerts/:alertId/read", (req, res) -> {
            try {
                String userId = req.params(":userId");
                String alertId = req.params(":alertId");
                alertService.markAlertAsRead(userId, alertId);
                res.type("application/json");
                return Map.of("success", true, "message", "Alert marked as read");
            } catch (Exception e) {
                res.status(500);
                return Map.of("success", false, "error", e.getMessage());
            }
        }, gson::toJson);

        // User: Snooze Alert
        post("/users/:userId/alerts/:alertId/snooze", (req, res) -> {
            try {
                String userId = req.params(":userId");
                String alertId = req.params(":alertId");
                alertService.snoozeAlert(userId, alertId);
                res.type("application/json");
                return Map.of("success", true, "message", "Alert snoozed for today");
            } catch (Exception e) {
                res.status(500);
                return Map.of("success", false, "error", e.getMessage());
            }
        }, gson::toJson);

        // Analytics
        get("/analytics/system", (req, res) -> {
            try {
                Map<String, Object> analytics = analyticsService.getSystemAnalytics();
                res.type("application/json");
                return Map.of("success", true, "analytics", analytics);
            } catch (Exception e) {
                res.status(500);
                return Map.of("success", false, "error", e.getMessage());
            }
        }, gson::toJson);

        // Admin: Disable Alert
        post("/admin/alerts/:alertId/disable", (req, res) -> {
            try {
                String alertId = req.params(":alertId");
                alertService.disableAlert(alertId);
                res.type("application/json");
                return Map.of("success", true, "message", "Alert disabled");
            } catch (Exception e) {
                res.status(500);
                return Map.of("success", false, "error", e.getMessage());
            }
        }, gson::toJson);
    }
}