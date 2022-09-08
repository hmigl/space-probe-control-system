package com.api.spaceprobecontrol.planet;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class RegisterPlanetRequest {
    @Positive
    @NotNull
    private int xAxis;
    @Positive
    @NotNull
    private int yAxis;

    public int getxAxis() {
        return xAxis;
    }

    public int getyAxis() {
        return yAxis;
    }
}
