package com.api.spaceprobecontrol.spaceprobe;

import com.api.spaceprobecontrol.planet.Planet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpaceProbeServiceImplTest {
    @MockBean
    private SpaceProbeRepository repository;
    private final SpaceProbeServiceImpl spaceProbeService = new SpaceProbeServiceImpl(repository);

    @Test
    @DisplayName("Shouldn't land due to repeated new coordinates")
    void shouldNotLandDueToRepeatedNewCoordinates() {
        var planet = new Planet(5, 5);
        List<LandSpaceProbeRequest.LandState> aspirantProbes = List.of(
                new LandSpaceProbeRequest.LandState(1, 1, Directions.NORTH),
                new LandSpaceProbeRequest.LandState(2, 2, Directions.SOUTH),
                new LandSpaceProbeRequest.LandState(2, 2, Directions.EAST)
        );

        assertFalse(spaceProbeService.allCanLand(aspirantProbes, planet));
    }

    @Test
    @DisplayName("Shouldn't land due to unsuitable coordinates")
    void shouldNotLandDueToUnsuitableCoordinates() {
        var planet = new Planet(1, 1);
        List<LandSpaceProbeRequest.LandState> aspirantProbes = List.of(
                new LandSpaceProbeRequest.LandState(1, 1, Directions.NORTH),
                new LandSpaceProbeRequest.LandState(10, 10, Directions.SOUTH)
        );

        assertFalse(spaceProbeService.allCanLand(aspirantProbes, planet));
    }

    @Test
    @DisplayName("Should land without conflicts")
    void shouldLand() {
        var planet = new Planet(5, 5);
        List<LandSpaceProbeRequest.LandState> aspirantProbes = List.of(
                new LandSpaceProbeRequest.LandState(1, 1, Directions.NORTH),
                new LandSpaceProbeRequest.LandState(2, 2, Directions.SOUTH),
                new LandSpaceProbeRequest.LandState(3, 3, Directions.EAST),
                new LandSpaceProbeRequest.LandState(4, 4, Directions.WEST),
                new LandSpaceProbeRequest.LandState(5, 5, Directions.NORTH)
        );

        assertTrue(spaceProbeService.allCanLand(aspirantProbes, planet));
    }
}
