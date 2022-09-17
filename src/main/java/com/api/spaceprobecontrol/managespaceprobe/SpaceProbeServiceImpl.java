package com.api.spaceprobecontrol.managespaceprobe;

import com.api.spaceprobecontrol.planet.Planet;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SpaceProbeServiceImpl implements SpaceProbeService {

    private final SpaceProbeRepository spaceProbeRepository;

    public SpaceProbeServiceImpl(SpaceProbeRepository spaceProbeRepository) {
        this.spaceProbeRepository = spaceProbeRepository;
    }

    private List<Point> getExistingCoordinates(Planet planet) {
        return planet
                .getSpaceProbes()
                .stream()
                .map(SpaceProbe::getCoordinate)
                .collect(Collectors.toList());
    }

    private boolean allWontClash(List<LandSpaceProbeRequest.LandState> aspirantProbes, Planet planet) {
        List<Point> existingCoordinates = getExistingCoordinates(planet);

        List<Point> possibleNewCoordinates = aspirantProbes
                .stream()
                .map(coordinate -> new Point(coordinate.getxAxis(), coordinate.getyAxis()))
                .collect(Collectors.toList());

        // List may have duplicates, compare their size to found out
        Set<Point> uniquePossibleNewCoords = new HashSet<>(possibleNewCoordinates);
        if (uniquePossibleNewCoords.size() != possibleNewCoordinates.size()) return false;

        // Mutate 'existingCoordinates' to check for possible conflicts
        return !existingCoordinates.removeAll(possibleNewCoordinates);
    }

    @Override
    public boolean allCanLand(List<LandSpaceProbeRequest.LandState> aspirantProbes, Planet planet) {
        return planet.hasSuitableBorders(aspirantProbes) && allWontClash(aspirantProbes, planet);
    }

    @Override
    @Transactional
    public List<SpaceProbe> saveAll(Iterable<SpaceProbe> entities) {
        return spaceProbeRepository.saveAll(entities);
    }

    private Optional<SpaceProbe> relocateToNewPosition(Long id, String command) {
        return spaceProbeRepository.findById(id).map(probe -> {
            /*
            * The space probe has to only worry about other probes' coordinates, so remove
            * its own coordinates to avoid confusion
            * */
            List<Point> existingCoordinatesButItsOwn = getExistingCoordinates(probe.getPlanet());
            existingCoordinatesButItsOwn.remove(probe.getCoordinate());

            probe.move(command, existingCoordinatesButItsOwn);
            return Optional.of(probe);
        }).orElse(Optional.empty());
    }

    @Override
    public List<SpaceProbe> processInstructions(List<MoveSpaceProbeRequest.MovementDemand> instructions, Planet planet) {
        return instructions
                .stream()
                .map(probe -> relocateToNewPosition(probe.getProbeId(), probe.getCommand()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
