package org.example.data_source.dao;

import jakarta.persistence.EntityManager;
import org.example.data_source.model.Location;

public class LocationDAO {
    private final EntityManager em;

    public LocationDAO(EntityManager em) {
        this.em = em;
    }

    public Location addLocation(String name,double latitude, double longitude, double today, double tomorrow, double dayAfter) {
        Location location = new Location();
        location.setName(name);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setTodayTemp(today);
        location.setTomorrowTemp(tomorrow);
        location.setDayAfterTomorrowTemp(dayAfter);

        try {
            em.getTransaction().begin();
            em.persist(location);
            em.getTransaction().commit();
            return location;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    public Location findNearestLocation(double latitude, double longitude) {
        return em.createQuery(
                        "SELECT l FROM Location l ORDER BY (:lat - l.latitude) * (:lat - l.latitude) + (:lon - l.longitude) * (:lon - l.longitude) ASC",
                        Location.class)
                .setParameter("lat", latitude)
                .setParameter("lon", longitude)
                .setMaxResults(1)
                .getSingleResult();
    }

}