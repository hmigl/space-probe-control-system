package com.api.spaceprobecontrol.planet;

import com.api.spaceprobecontrol.spaceprobe.SpaceProbe;
import com.api.spaceprobecontrol.spaceprobe.SpaceProbeRequest;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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

    public boolean hasSuitableBorders(List<SpaceProbeRequest> aspirantProbes) {
        Predicate<SpaceProbeRequest> respectsXAxis = aspirantProbe -> aspirantProbe.getState().getxAxis() <= this.xAxis;
        Predicate<SpaceProbeRequest> respectsYAxis = aspirantProbe -> aspirantProbe.getState().getyAxis() <= this.yAxis;

        return aspirantProbes
                .stream()
                .allMatch(respectsXAxis.and(respectsYAxis));
    }
}
