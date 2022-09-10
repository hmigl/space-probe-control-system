package com.api.spaceprobecontrol.spaceprobe;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class SpaceProbeRequest {
    @Valid
    private SpaceProbeState state;

    public SpaceProbeState getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpaceProbeRequest that = (SpaceProbeRequest) o;
        return state.equals(that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }

    @Override
    public String toString() {
        return "SpaceProbeRequest{" +
                "state=" + state +
                '}';
    }

    public class SpaceProbeState {
        @Positive
        @NotNull
        private Integer xAxis;
        @Positive
        @NotNull
        private Integer yAxis;
        @NotNull
        private Directions pointsTo;

        public Integer getxAxis() {
            return xAxis;
        }

        public Integer getyAxis() {
            return yAxis;
        }

        public Directions getPointsTo() {
            return pointsTo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SpaceProbeState that = (SpaceProbeState) o;
            return xAxis.equals(that.xAxis) && yAxis.equals(that.yAxis) && pointsTo == that.pointsTo;
        }

        @Override
        public int hashCode() {
            return Objects.hash(xAxis, yAxis, pointsTo);
        }

        @Override
        public String toString() {
            return "SpaceProbeState{" +
                    "xAxis=" + xAxis +
                    ", yAxis=" + yAxis +
                    ", pointsTo=" + pointsTo +
                    '}';
        }
    }
}
