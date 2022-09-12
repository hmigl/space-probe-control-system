package com.api.spaceprobecontrol.spaceprobe.impl;

import com.api.spaceprobecontrol.planet.Planet;
import com.api.spaceprobecontrol.planet.PlanetRepository;
import com.api.spaceprobecontrol.spaceprobe.SpaceProbe;
import com.api.spaceprobecontrol.spaceprobe.SpaceProbeRepository;
import com.api.spaceprobecontrol.spaceprobe.SpaceProbeRequest;
import com.api.spaceprobecontrol.spaceprobe.SpaceProbeService;
import org.springframework.stereotype.Service;

import java.awt.Point;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class SpaceProbeServiceImpl implements SpaceProbeService {

    private final PlanetRepository planetRepository;
    private final SpaceProbeRepository spaceProbeRepository;

    public SpaceProbeServiceImpl(PlanetRepository planetRepository, SpaceProbeRepository spaceProbeRepository) {
        this.planetRepository = planetRepository;
        this.spaceProbeRepository = spaceProbeRepository;
    }

    private boolean allWithinPlanetBorders(List<SpaceProbeRequest> spaceProbes, Long id) {
        Optional<Planet> possiblePlanet = planetRepository.findById(id);

        return possiblePlanet.map(planet -> {
            Predicate<SpaceProbeRequest> respectsXAxis = p -> p.getState().getxAxis() <= planet.getxAxis();
            Predicate<SpaceProbeRequest> respectsYAxis = p -> p.getState().getyAxis() <= planet.getyAxis();

            return spaceProbes
                    .stream()
                    .allMatch(respectsXAxis.and(respectsYAxis));
        }).orElse(false);
    }

    private boolean allWontClash(List<SpaceProbeRequest> requests, Long id) {
        Optional<Planet> possiblePlanet = planetRepository.findById(id);

        return possiblePlanet.map(planet -> {
            List<Point> existingCoordinates = planet
                    .getSpaceProbes()
                    .stream()
                    .map(SpaceProbe::getCoordinate)
                    .collect(Collectors.toList());

            // TODO: solve duplicated coordinates issue (maybe using a Set<>?)
            List<Point> possibleCoordinates = requests
                    .stream()
                    .map(coordinate -> new Point(coordinate.getState().getxAxis(), coordinate.getState().getyAxis()))
                    .collect(Collectors.toList());

            return !existingCoordinates.removeAll(possibleCoordinates);
        }).orElse(false);
    }

    @Override
    public boolean allCanLand(List<SpaceProbeRequest> requests, Long id) {
        return allWithinPlanetBorders(requests, id) && allWontClash(requests, id);
    }

    @Override
    public boolean planetExistsById(Long id) {
        return planetRepository.existsById(id);
    }

    @Override
    public List<SpaceProbe> saveAll(Iterable<SpaceProbe> entities) {
        return spaceProbeRepository.saveAll(entities);
    }
}
