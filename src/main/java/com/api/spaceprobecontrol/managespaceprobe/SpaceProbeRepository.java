package com.api.spaceprobecontrol.managespaceprobe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceProbeRepository extends JpaRepository<SpaceProbe, Long> {
}
