package com.alerting.model;

public class User {
    private String id;
    private String name;
    private String teamId;

    public User(String id, String name, String teamId) {
        this.id = id;
        this.name = name;
        this.teamId = teamId;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getTeamId() { return teamId; }

    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name + "', teamId='" + teamId + "'}";
    }
}