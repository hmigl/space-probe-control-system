package com.api.spaceprobecontrol.spaceprobe;

import java.util.List;

public interface SpaceProbeService {
    boolean allCanLand(List<SpaceProbeRequest> request, Long id);
    boolean existsById(Long id);
}
