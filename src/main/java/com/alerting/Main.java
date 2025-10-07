package com.alerting;

import com.alerting.controller.AlertController;
import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        // Configure Spark
        port(4567);
        staticFiles.location("/public");

        // Enable CORS
        enableCORS();

        System.out.println("🚀 Alerting System Starting...");
        System.out.println("📡 Server running on: http://localhost:4567");

        // Initialize controllers
        new AlertController();

        // Add welcome route
        get("/", (req, res) -> {
            return "🎯 Alerting System API is running!\n\n" +
                    "Available Endpoints:\n" +
                    "POST /admin/alerts - Create alert\n" +
                    "GET /admin/alerts - List all alerts\n" +
                    "GET /users/{userId}/alerts - Get user alerts\n" +
                    "POST /users/{userId}/alerts/{alertId}/read - Mark as read\n" +
                    "POST /users/{userId}/alerts/{alertId}/snooze - Snooze alert\n" +
                    "GET /analytics/system - Get analytics\n" +
                    "POST /admin/alerts/{alertId}/disable - Disable alert";
        });

        System.out.println("✅ Alerting System Ready!");
    }

    private static void enableCORS() {
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
            response.type("application/json");
        });
    }
}