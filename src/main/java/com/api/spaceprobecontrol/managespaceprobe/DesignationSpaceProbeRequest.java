package com.api.spaceprobecontrol.managespaceprobe;

import com.api.spaceprobecontrol.planet.Planet;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.awt.Point;
import java.util.List;
import java.util.stream.Collectors;

public class DesignationSpaceProbeRequest {
    @Valid
    @NotNull
    @NotEmpty
    @UniqueElements
    List<SpaceProbeRequest> spaceProbes;

    @JsonCreator
    public DesignationSpaceProbeRequest(List<SpaceProbeRequest> spaceProbes) {
        this.spaceProbes = spaceProbes;
    }

    public List<SpaceProbeRequest> getSpaceProbes() {
        return spaceProbes;
    }

    @Override
    public String toString() {
        return "DesignateSpaceProbeListRequest{" +
                "spaceProbes=" + spaceProbes +
                '}';
    }

    public List<SpaceProbe> toModel(Planet planet) {
        return spaceProbes
                .stream()
                .map(probe -> new SpaceProbe(
                        new Point(probe.getState().getxAxis(), probe.getState().getyAxis()),
                        probe.getState().getPointsTo(),
                        planet)
                )
                .collect(Collectors.toList());
    }
}
