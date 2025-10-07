package com.alerting.repository;

import com.alerting.model.Team;
import java.util.*;

public class TeamRepository {
    private final Map<String, Team> teams = new HashMap<>();

    public TeamRepository() {
        initializeSeedData();
    }

    private void initializeSeedData() {
        saveTeam(new Team("team1", "Engineering"));
        saveTeam(new Team("team2", "Marketing"));
        saveTeam(new Team("admin", "Administration"));
    }

    public void saveTeam(Team team) {
        teams.put(team.getId(), team);
    }

    public Team findById(String teamId) {
        return teams.get(teamId);
    }

    public List<Team> findAll() {
        return new ArrayList<>(teams.values());
    }
}