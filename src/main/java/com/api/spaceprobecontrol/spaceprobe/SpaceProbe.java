package com.api.spaceprobecontrol.spaceprobe;

import com.api.spaceprobecontrol.planet.Planet;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.Point;
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
        }
    }
}
