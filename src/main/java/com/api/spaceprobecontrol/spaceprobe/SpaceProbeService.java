package com.api.spaceprobecontrol.spaceprobe;

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
public class SpaceProbeService {

    private final SpaceProbeRepository spaceProbeRepository;

    public SpaceProbeService(SpaceProbeRepository spaceProbeRepository) {
        this.spaceProbeRepository = spaceProbeRepository;
    }

    private boolean allWontClash(List<LandSpaceProbeRequest.LandState> aspirantProbes, Planet planet) {
        List<Point> possibleNewCoordinates = aspirantProbes
                .stream()
                .map(coordinate -> new Point(coordinate.getxAxis(), coordinate.getyAxis()))
                .collect(Collectors.toList());

        // List may have duplicates, compare their size to found out
        Set<Point> uniquePossibleNewCoords = new HashSet<>(possibleNewCoordinates);
        if (uniquePossibleNewCoords.size() != possibleNewCoordinates.size()) return false;

        // Mutate 'existingCoordinates' to check for possible conflicts
        List<Point> existingCoordinates = planet.accessBusyCoordinates();
        return !existingCoordinates.removeAll(possibleNewCoordinates);
    }

    public boolean allCanLand(List<LandSpaceProbeRequest.LandState> aspirantProbes, Planet planet) {
        return planet.hasSuitableBorders(aspirantProbes) && allWontClash(aspirantProbes, planet);
    }

    private Optional<SpaceProbe> relocateToNewPosition(Long id, String command) {
        return spaceProbeRepository.findById(id).map(probe -> {
            /*
            * The space probe has to only worry about other probes' coordinates, so remove
            * its own coordinates to avoid confusion
            * */
            List<Point> existingCoordinatesButItsOwn = probe.getPlanet().accessBusyCoordinates();
            existingCoordinatesButItsOwn.remove(probe.getCoordinate());

            probe.move(command, existingCoordinatesButItsOwn);
            return Optional.of(probe);
        }).orElse(Optional.empty());
    }

    public List<SpaceProbe> processInstructions(List<MoveSpaceProbeRequest.MovementDemand> instructions) {
        return instructions
                .stream()
                .map(probe -> relocateToNewPosition(probe.getProbeId(), probe.getCommand()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<SpaceProbe> saveAll(Iterable<SpaceProbe> entities) {
        return spaceProbeRepository.saveAll(entities);
    }

    public Optional<SpaceProbe> findById(Long id) {
        return spaceProbeRepository.findById(id);
    }

    public List<SpaceProbe> findAll() {
        return spaceProbeRepository.findAll();
    }

    @Transactional
    public void deleteAll() {
        spaceProbeRepository.deleteAll();
    }

    @Transactional
    public void delete(SpaceProbe probe) {
        spaceProbeRepository.delete(probe);
    }
}
