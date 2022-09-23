package com.api.spaceprobecontrol.spaceprobe;

import com.api.spaceprobecontrol.planet.Planet;

import java.util.List;
import java.util.Optional;

public interface SpaceProbeService {
    boolean allCanLand(List<LandSpaceProbeRequest.LandState> request, Planet planet);
    List<SpaceProbe> saveAll(Iterable<SpaceProbe> entities);
    List<SpaceProbe> processInstructions(List<MoveSpaceProbeRequest.MovementDemand> instructions);
    Optional<SpaceProbe> findById(Long id);
}
