package com.api.spaceprobecontrol.spaceprobe;

public interface SpaceProbeService {
    boolean allCanLand(DesignationSpaceProbeRequest request, Long id);
    boolean existsById(Long id);
}
