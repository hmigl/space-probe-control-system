package com.api.spaceprobecontrol.spaceprobe;

import com.api.spaceprobecontrol.planet.Planet;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.Point;

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

    @ManyToOne
    @JoinColumn(name = "planet_id_fk", referencedColumnName = "planet_id")
    private Planet planet;

    public Long getId() {
        return id;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public Planet getPlanet() {
        return planet;
    }
}
