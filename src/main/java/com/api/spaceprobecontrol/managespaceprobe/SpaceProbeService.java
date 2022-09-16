package com.api.spaceprobecontrol.managespaceprobe;

import com.api.spaceprobecontrol.planet.Planet;

import java.util.List;

public interface SpaceProbeService {
    boolean allCanLand(List<SpaceProbeRequest> request, Planet planet);
    boolean planetExistsById(Long id);
    List<SpaceProbe> saveAll(Iterable<SpaceProbe> entities);
    List<SpaceProbe> processInstructions(List<MoveSpaceProbeRequest.MovementDemand> instructions, Planet planet);
}
