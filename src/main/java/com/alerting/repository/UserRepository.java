package com.alerting.repository;

import com.alerting.model.User;
import java.util.*;

public class UserRepository {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, List<User>> teamUsers = new HashMap<>();

    public UserRepository() {
        // Seed data
        initializeSeedData();
    }

    private void initializeSeedData() {
        // Create teams
        saveUser(new User("user1", "Alice", "team1"));
        saveUser(new User("user2", "Bob", "team1"));
        saveUser(new User("user3", "Charlie", "team2"));
        saveUser(new User("user4", "Diana", "team2"));
        saveUser(new User("admin1", "Admin User", "admin"));
    }

    public void saveUser(User user) {
        users.put(user.getId(), user);
        teamUsers.computeIfAbsent(user.getTeamId(), k -> new ArrayList<>()).add(user);
    }

    public User findById(String userId) {
        return users.get(userId);
    }

    public List<User> findByTeam(String teamId) {
        return teamUsers.getOrDefault(teamId, new ArrayList<>());
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public List<User> findUsersInTeams(List<String> teamIds) {
        List<User> result = new ArrayList<>();
        for (String teamId : teamIds) {
            result.addAll(findByTeam(teamId));
        }
        return result;
    }
}