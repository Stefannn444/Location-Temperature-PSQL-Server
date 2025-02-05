package org.example.data_source.model;

import jakarta.persistence.*;
import lombok.Data;

//import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "location")
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double latitude;
    private double longitude;
    @Column(name="today_temp")
    private double todayTemp;
    @Column(name="tomorrow_temp")
    private double tomorrowTemp;
    @Column(name="day_after_tomorrow_temp")
    private double dayAfterTomorrowTemp;

    /*@OneToMany(mappedBy = "location")
    private List<User> users;*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;
        return Double.compare(location.latitude, latitude) == 0 &&
                Double.compare(location.longitude, longitude) == 0 &&
                Double.compare(location.todayTemp, todayTemp) == 0 &&
                Double.compare(location.tomorrowTemp, tomorrowTemp) == 0 &&
                Double.compare(location.dayAfterTomorrowTemp, dayAfterTomorrowTemp) == 0 &&
                Objects.equals(id, location.id) &&
                Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, latitude, longitude, todayTemp, tomorrowTemp, dayAfterTomorrowTemp);
    }

}
