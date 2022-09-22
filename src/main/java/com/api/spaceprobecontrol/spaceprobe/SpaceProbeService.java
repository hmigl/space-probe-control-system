package com.api.spaceprobecontrol.spaceprobe;

import com.api.spaceprobecontrol.planet.Planet;

import java.util.List;

public interface SpaceProbeService {
    boolean allCanLand(List<LandSpaceProbeRequest.LandState> request, Planet planet);
    List<SpaceProbe> saveAll(Iterable<SpaceProbe> entities);
    List<SpaceProbe> processInstructions(List<MoveSpaceProbeRequest.MovementDemand> instructions);
}
