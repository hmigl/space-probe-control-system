package com.api.spaceprobecontrol.spaceprobe.impl;

import com.api.spaceprobecontrol.planet.Planet;
import com.api.spaceprobecontrol.planet.PlanetRepository;
import com.api.spaceprobecontrol.spaceprobe.SpaceProbe;
import com.api.spaceprobecontrol.spaceprobe.SpaceProbeRepository;
import com.api.spaceprobecontrol.spaceprobe.SpaceProbeRequest;
import com.api.spaceprobecontrol.spaceprobe.SpaceProbeService;
import org.springframework.stereotype.Service;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SpaceProbeServiceImpl implements SpaceProbeService {

    private final PlanetRepository planetRepository;
    private final SpaceProbeRepository spaceProbeRepository;

    public SpaceProbeServiceImpl(PlanetRepository planetRepository, SpaceProbeRepository spaceProbeRepository) {
        this.planetRepository = planetRepository;
        this.spaceProbeRepository = spaceProbeRepository;
    }

    private boolean allWontClash(List<SpaceProbeRequest> aspirantProbes, Planet planet) {
        List<Point> existingCoordinates = planet
                .getSpaceProbes()
                .stream()
                .map(SpaceProbe::getCoordinate)
                .collect(Collectors.toList());

        List<Point> possibleNewCoordinates = aspirantProbes
                .stream()
                .map(coordinate -> new Point(coordinate.getState().getxAxis(), coordinate.getState().getyAxis()))
                .collect(Collectors.toList());

        // List may have duplicates, compare their size to found out
        Set<Point> uniquePossibleNewCoords = new HashSet<>(possibleNewCoordinates);
        if (uniquePossibleNewCoords.size() != possibleNewCoordinates.size()) return false;

        // Mutate 'existingCoordinates' to check for possible conflicts
        return !existingCoordinates.removeAll(possibleNewCoordinates);
    }

    @Override
    public boolean allCanLand(List<SpaceProbeRequest> aspirantProbes, Planet planet) {
        return planet.hasSuitableBorders(aspirantProbes) && allWontClash(aspirantProbes, planet);
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
