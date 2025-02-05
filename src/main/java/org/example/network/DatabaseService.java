package org.example.network;

import jakarta.persistence.*;
import com.google.gson.Gson;
import org.example.data_source.dao.LocationDAO;
import org.example.data_source.dao.UserDAO;
import org.example.data_source.model.Location;
import org.example.data_source.model.User;

import java.util.Map;
import java.util.Optional;

public class DatabaseService {
    private final EntityManagerFactory emf;
    private final EntityManager em;
    private final UserDAO userDAO;
    private final LocationDAO locationDAO;
    private final Gson gson = new Gson();

    public DatabaseService() {
        this.emf = Persistence.createEntityManagerFactory("WeatherPU");
        this.em = emf.createEntityManager();
        this.userDAO = new UserDAO(em);
        this.locationDAO = new LocationDAO(em);
    }
    public String loginUser(String username) {
        try {
            User user = userDAO.findByUsername(username)
                    .orElseGet(() -> userDAO.createUser(username));
            return user.getId() == null ?
                    "New user created and logged in: " + username :
                    "Welcome back, " + username;
        } catch (Exception e) {
            return "Error during login: " + e.getMessage();
        }
    }
    public String addLocation(Map<String, Object> data) {
        try {
            String name = (String) data.get("name");
            double latitude = (Double) data.get("latitude");
            double longitude = (Double) data.get("longitude");
            Map<String, Object> weather = (Map<String, Object>) data.get("weather");

            locationDAO.addLocation(
                    name,
                    latitude,
                    longitude,
                    (Double) weather.get("day1"),
                    (Double) weather.get("day2"),
                    (Double) weather.get("day3")
            );

            return "Location added successfully";
        } catch (Exception e) {
            return "Error adding location: " + e.getMessage();
        }
    }

    public String updateUserLocation(String username, double latitude, double longitude) {
        try {
            return userDAO.findByUsername(username)
                    .map(user -> {
                        Location nearest = locationDAO.findNearestLocation(latitude, longitude);
                        userDAO.updateUserLocation(user, nearest);
                        return String.format("Location updated successfully to: %f, %f",
                                nearest.getLatitude(), nearest.getLongitude());
                    })
                    .orElse("Error: User not found");
        } catch (Exception e) {
            return "Error updating location: " + e.getMessage();
        }
    }

    public String getWeather(String username) {
        return userDAO.findByUsername(username)
                .flatMap(user -> Optional.ofNullable(user.getLocation()))
                .map(gson::toJson)
                .orElse("No location set for user");
    }
}