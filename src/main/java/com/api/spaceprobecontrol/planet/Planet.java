package com.api.spaceprobecontrol.planet;

import com.api.spaceprobecontrol.spaceprobe.LandSpaceProbeRequest;
import com.api.spaceprobecontrol.spaceprobe.SpaceProbe;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Entity
@Table(name = "planet")
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "planet_id")
    private Long id;
    @Positive
    @NotNull
    @Column(name = "x_axis", nullable = false)
    private int xAxis;
    @Positive
    @NotNull
    @Column(name = "y_axis", nullable = false)
    private int yAxis;

    @OneToMany(mappedBy = "planet", cascade = CascadeType.PERSIST)
    List<SpaceProbe> spaceProbes = new ArrayList<>();

    /**
     *
     * @deprecated default constructor required by hibernate
    */
    @Deprecated
    public Planet() {
    }

    public Planet(int xAxis, int yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public Long getId() {
        return id;
    }

    public int getxAxis() {
        return xAxis;
    }

    public int getyAxis() {
        return yAxis;
    }

    public List<SpaceProbe> getSpaceProbes() {
        return spaceProbes;
    }

    @Override
    public String toString() {
        return "Planet{" +
                "id=" + id +
                ", xAxis=" + xAxis +
                ", yAxis=" + yAxis +
                '}';
    }

    public boolean hasSuitableBorders(List<LandSpaceProbeRequest.LandState> aspirantProbes) {
        Predicate<LandSpaceProbeRequest.LandState> respectsXAxis = aspirantProbe -> aspirantProbe.getxAxis() <= this.xAxis;
        Predicate<LandSpaceProbeRequest.LandState> respectsYAxis = aspirantProbe -> aspirantProbe.getyAxis() <= this.yAxis;

        return aspirantProbes
                .stream()
                .allMatch(respectsXAxis.and(respectsYAxis));
    }

    public List<Point> accessBusyCoordinates() {
        return this.spaceProbes
                .stream()
                .map(SpaceProbe::getCoordinate)
                .collect(Collectors.toList());
    }

    public boolean hasSpaceProbes() {
        return !spaceProbes.isEmpty();
    }

    public void reshape(RegisterPlanetRequest request) {
        this.xAxis = request.getxAxis();
        this.yAxis = request.getyAxis();
    }
}
