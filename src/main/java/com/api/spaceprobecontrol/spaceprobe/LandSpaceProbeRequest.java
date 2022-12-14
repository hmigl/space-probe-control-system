package com.api.spaceprobecontrol.spaceprobe;

import com.api.spaceprobecontrol.planet.Planet;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LandSpaceProbeRequest {
    @Valid
    @NotNull
    @NotEmpty
    @UniqueElements
    private final List<LandState> spaceProbes;

    public LandSpaceProbeRequest(@JsonProperty("spaceProbes") List<LandState> spaceProbes) {
        this.spaceProbes = spaceProbes;
    }

    public List<LandState> getSpaceProbes() {
        return spaceProbes;
    }

    public List<SpaceProbe> toModel(Planet planet) {
        return spaceProbes
                .stream()
                .map(probe -> new SpaceProbe(new Point(probe.getxAxis(), probe.getyAxis()), Orientation.valueOf(probe.getPointsTo()), planet))
                .collect(Collectors.toList());
    }

    public static class LandState {
        @Positive
        @NotNull
        private final Integer xAxis;
        @Positive
        @NotNull
        private final Integer yAxis;
        @NotNull
        @Pattern(regexp = "NORTH|SOUTH|EAST|WEST")
        private final String pointsTo;

        public LandState(Integer xAxis, Integer yAxis, String pointsTo) {
            this.xAxis = xAxis;
            this.yAxis = yAxis;
            this.pointsTo = pointsTo;
        }

        public Integer getxAxis() {
            return xAxis;
        }

        public Integer getyAxis() {
            return yAxis;
        }

        public String getPointsTo() {
            return pointsTo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LandState landState = (LandState) o;
            return xAxis.equals(landState.xAxis) && yAxis.equals(landState.yAxis) && pointsTo.equals(landState.pointsTo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(xAxis, yAxis, pointsTo);
        }
    }
}
