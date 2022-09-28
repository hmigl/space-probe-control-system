package com.api.spaceprobecontrol.spaceprobe;

import com.api.spaceprobecontrol.planet.Planet;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.*;
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
                this.pointsTo = pointsTo.alterOrientation(c);
            else if (!carefullyRelocate(pointsTo, existingCoordinatesButItsOwn)) break;
        }
    }

    private boolean carefullyRelocate(Directions pointsTo, List<Point> existingCoordinatesButItsOwn) {
        if (pointsTo == Directions.NORTH) // immutable x, y += 1
            return moveTowardsNorth(existingCoordinatesButItsOwn);
        else if (pointsTo == Directions.SOUTH) // immutable x, y -= 1
            return moveTowardsSouth(existingCoordinatesButItsOwn);
        else if (pointsTo == Directions.EAST) // x += 1, immutable y
            return moveTowardsEast(existingCoordinatesButItsOwn);
        else // x -= 1, immutable y
            return moveTowardsWest(existingCoordinatesButItsOwn);
    }

    private boolean canAccomplishMove(Point possibleNextCoordinate, List<Point> existingCoordinatesButItsOwn) {
        if (existingCoordinatesButItsOwn.contains(possibleNextCoordinate)) return false;
        this.coordinate.setLocation(possibleNextCoordinate);
        return true;
    }

    @Override
    public boolean moveTowardsNorth(List<Point> existingCoordinatesButItsOwn) {
        int y = coordinate.y == planet.getyAxis() ? 1 : coordinate.y + 1;
        return canAccomplishMove(new Point(coordinate.x, y), existingCoordinatesButItsOwn);
    }

    @Override
    public boolean moveTowardsSouth(List<Point> existingCoordinatesButItsOwn) {
        int y = coordinate.y == 1 ? planet.getyAxis() : coordinate.y - 1;
        return canAccomplishMove(new Point(coordinate.x, y), existingCoordinatesButItsOwn);
    }

    @Override
    public boolean moveTowardsEast(List<Point> existingCoordinatesButItsOwn) {
        int x = coordinate.x == planet.getxAxis() ? 1 : coordinate.x + 1;
        return canAccomplishMove(new Point(x, coordinate.y), existingCoordinatesButItsOwn);
    }

    @Override
    public boolean moveTowardsWest(List<Point> existingCoordinatesButItsOwn) {
        int x = coordinate.x == 1 ? planet.getxAxis() : coordinate.x - 1;
        return canAccomplishMove(new Point(x, coordinate.y), existingCoordinatesButItsOwn);
    }
}
