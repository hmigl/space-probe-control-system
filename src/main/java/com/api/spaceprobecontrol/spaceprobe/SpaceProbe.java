package com.api.spaceprobecontrol.spaceprobe;

import com.api.spaceprobecontrol.planet.Planet;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.util.List;

@Entity
@Table(name = "space_probe")
public class SpaceProbe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sp_id")
    private Long id;
    @NotNull
    @Column(name = "coordinate")
    private Point coordinate;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "points_to")
    private Orientation pointsTo;

    @ManyToOne
    @JoinColumn(name = "planet_id_fk", referencedColumnName = "planet_id")
    private Planet planet;

    /**
     *
     * @deprecated default constructor required by hibernate
     */
    public SpaceProbe() {
    }

    public SpaceProbe(Point coordinate, Orientation pointsTo, Planet planet) {
        this.coordinate = coordinate;
        this.pointsTo = pointsTo;
        this.planet = planet;
    }

    public Long getId() {
        return id;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public Orientation getPointsTo() {
        return pointsTo;
    }

    @JsonIgnore
    public Planet getPlanet() {
        return planet;
    }

    public void move(String command, List<Point> unavailableCoordinates) {
        for (char c : command.toCharArray()) {
            if (c == 'L' || c == 'R')
                this.pointsTo = pointsTo.alterOrientation(c);
            else if (!reachCoordinate(pointsTo.estimateCoordinate(coordinate, planet), unavailableCoordinates)) break;
        }
    }

    private boolean reachCoordinate(Point possibleNewCoordinate, List<Point> unavailableCoordinates) {
        if (unavailableCoordinates.contains(possibleNewCoordinate))
            return false;
        this.coordinate.setLocation(possibleNewCoordinate);
        return true;
    }
}
