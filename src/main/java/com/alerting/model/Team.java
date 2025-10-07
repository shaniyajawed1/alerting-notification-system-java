package com.alerting.model;

public class Team {
    private String id;
    private String name;

    public Team(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "Team{id='" + id + "', name='" + name + "'}";
    }
}