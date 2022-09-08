package com.api.spaceprobecontrol.spaceprobe;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.Valid;
import java.util.List;

public class DesignationSpaceProbeRequest {
    @Valid
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
}
