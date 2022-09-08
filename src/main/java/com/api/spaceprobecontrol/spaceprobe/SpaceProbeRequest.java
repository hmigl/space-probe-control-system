package com.api.spaceprobecontrol.spaceprobe;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SpaceProbeRequest {
    @Valid
    private SpaceProbeState state;

    public SpaceProbeState getState() {
        return state;
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
        public String toString() {
            return "SpaceProbeState{" +
                    "xAxis=" + xAxis +
                    ", yAxis=" + yAxis +
                    ", pointsTo=" + pointsTo +
                    '}';
        }
    }
}
