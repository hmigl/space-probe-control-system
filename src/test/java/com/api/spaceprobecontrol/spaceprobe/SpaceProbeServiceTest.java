package com.api.spaceprobecontrol.spaceprobe;

import com.api.spaceprobecontrol.planet.Planet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SpaceProbeServiceTest  {
    @Mock
    private SpaceProbeRepository spaceProbeRepository;
    @InjectMocks
    private SpaceProbeService spaceProbeService;

    @Test
    @DisplayName("Shouldn't land due to repeated new coordinates")
    void shouldNotLandDueToRepeatedNewCoordinates() {
        var planet = new Planet(5, 5);
        List<LandSpaceProbeRequest.LandState> aspirantProbes = List.of(
                new LandSpaceProbeRequest.LandState(1, 1, Orientation.NORTH),
                new LandSpaceProbeRequest.LandState(2, 2, Orientation.SOUTH),
                new LandSpaceProbeRequest.LandState(2, 2, Orientation.EAST)
        );

        assertFalse(spaceProbeService.allCanLand(aspirantProbes, planet));
    }

    @Test
    @DisplayName("Shouldn't land due to unsuitable coordinates")
    void shouldNotLandDueToUnsuitableCoordinates() {
        var planet = new Planet(1, 1);
        List<LandSpaceProbeRequest.LandState> aspirantProbes = List.of(
                new LandSpaceProbeRequest.LandState(1, 1, Orientation.NORTH),
                new LandSpaceProbeRequest.LandState(10, 10, Orientation.SOUTH)
        );

        assertFalse(spaceProbeService.allCanLand(aspirantProbes, planet));
    }

    @Test
    @DisplayName("Should land without conflicts")
    void shouldLand() {
        var planet = new Planet(5, 5);
        List<LandSpaceProbeRequest.LandState> aspirantProbes = List.of(
                new LandSpaceProbeRequest.LandState(1, 1, Orientation.NORTH),
                new LandSpaceProbeRequest.LandState(2, 2, Orientation.SOUTH),
                new LandSpaceProbeRequest.LandState(3, 3, Orientation.EAST),
                new LandSpaceProbeRequest.LandState(4, 4, Orientation.WEST),
                new LandSpaceProbeRequest.LandState(5, 5, Orientation.NORTH)
        );

        assertTrue(spaceProbeService.allCanLand(aspirantProbes, planet));
    }

    @Test
    @DisplayName("Should move space probes")
    void shouldMoveSpaceProbes() {
        var planet = new Planet(5, 5);
        List<MoveSpaceProbeRequest.MovementDemand> instructions = List.of(
                new MoveSpaceProbeRequest.MovementDemand(1L, "LMLMLMLMM"),
                new MoveSpaceProbeRequest.MovementDemand(2L, "MMRMMRMRRML")
        );

        given(spaceProbeRepository.findById(1L))
                .willReturn(Optional.of(new SpaceProbe(new Point(1, 2), Orientation.NORTH, planet)));
        given(spaceProbeRepository.findById(2L))
                .willReturn(Optional.of(new SpaceProbe(new Point(3, 3), Orientation.EAST, planet)));

        List<SpaceProbe> repositionedSpaceProbes = spaceProbeService.processInstructions(instructions);
        // verify their final positions
        assertEquals(new Point(1, 3), repositionedSpaceProbes.get(0).getCoordinate());
        assertEquals(Orientation.NORTH, repositionedSpaceProbes.get(0).getPointsTo());
        assertEquals(new Point(5, 1), repositionedSpaceProbes.get(1).getCoordinate());
        assertEquals(Orientation.NORTH, repositionedSpaceProbes.get(1).getPointsTo());

        verify(spaceProbeRepository, times(2)).findById(any());
    }

    @Test
    @DisplayName("Shouldn't move when collision is imminent")
    void shouldNotMoveWhenImminentCollision() {
        var spaceProbe = new SpaceProbe(new Point(2, 2), Orientation.WEST, new Planet(3, 3));

        spaceProbe.move("M", List.of(new Point(1, 2)));
        assertEquals(new Point(2, 2), spaceProbe.getCoordinate());
    }
}
