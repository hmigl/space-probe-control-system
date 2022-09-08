package com.api.spaceprobecontrol.planet;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class Planet {
    @Positive
    @NotNull
    private int xAxis;
    @Positive
    @NotNull
    private int yAxis;

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

    @Override
    public String toString() {
        return "Planet{" +
                "xAxis=" + xAxis +
                ", yAxis=" + yAxis +
                '}';
    }
}
