package com.api.spaceprobecontrol.spaceprobe;

import com.api.spaceprobecontrol.planet.Planet;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.Point;
import java.util.List;

@Entity
@Table(name = "space_probe")
public class SpaceProbe implements Movement {
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
    private Directions pointsTo;

    @ManyToOne
    @JoinColumn(name = "planet_id_fk", referencedColumnName = "planet_id")
    private Planet planet;

    /**
     *
     * @deprecated default constructor required by hibernate
     */
    public SpaceProbe() {
    }

    public SpaceProbe(Point coordinate, Directions pointsTo, Planet planet) {
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

    public Directions getPointsTo() {
        return pointsTo;
    }

    @JsonIgnore
    public Planet getPlanet() {
        return planet;
    }

    public void move(String command, List<Point> existingCoordinatesButItsOwn) {
        for (char c : command.toCharArray()) {
            if (c == 'L' || c == 'R')
                this.pointsTo = this.pointsTo.nextSide(c);
            else carefullyRelocate(pointsTo, existingCoordinatesButItsOwn);
        }
    }

    private void carefullyRelocate(Directions pointsTo, List<Point> existingCoordinatesButItsOwn) {
        if (pointsTo == Directions.NORTH) // immutable x, y += 1
            moveTowardsNorth(existingCoordinatesButItsOwn);
        else if (pointsTo == Directions.SOUTH) // immutable x, y -= 1
            moveTowardsSouth(existingCoordinatesButItsOwn);
        else if (pointsTo == Directions.EAST) // x += 1, immutable y
            moveTowardsEast(existingCoordinatesButItsOwn);
        else // x -= 1, immutable y
            moveTowardsWest(existingCoordinatesButItsOwn);
    }

    @Override
    public void moveTowardsNorth(List<Point> existingCoordinatesButItsOwn) {
        int y = getCoordinate().y == getPlanet().getyAxis() ? 1 : getCoordinate().y + 1;

        Point nextStep = new Point(getCoordinate().x, y);
        if (existingCoordinatesButItsOwn.contains(nextStep))
            return;
        this.coordinate.setLocation(nextStep);
    }

    @Override
    public void moveTowardsSouth(List<Point> existingCoordinatesButItsOwn) {
        int y = getCoordinate().y == 1 ? getPlanet().getyAxis() : getCoordinate().y - 1;

        Point nextStep = new Point(getCoordinate().x, y);
        if (existingCoordinatesButItsOwn.contains(nextStep))
            return;
        this.coordinate.setLocation(nextStep);
    }

    @Override
    public void moveTowardsEast(List<Point> existingCoordinatesButItsOwn) {
        int x = getCoordinate().x == getPlanet().getxAxis() ? 1 : getCoordinate().x + 1;

        Point nextStep = new Point(x, getCoordinate().y);
        if (existingCoordinatesButItsOwn.contains(nextStep))
            return;
        this.coordinate.setLocation(nextStep);
    }

    @Override
    public void moveTowardsWest(List<Point> existingCoordinatesButItsOwn) {
        int x = getCoordinate().x == 1 ? getPlanet().getxAxis() : getCoordinate().x - 1;

        Point nextStep = new Point(x, getCoordinate().y);
        if (existingCoordinatesButItsOwn.contains(nextStep))
            return;
        this.coordinate.setLocation(nextStep);
    }
}
