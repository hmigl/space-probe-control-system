package com.api.spaceprobecontrol.spaceprobe.impl;

import com.api.spaceprobecontrol.planet.Planet;
import com.api.spaceprobecontrol.planet.PlanetRepository;
import com.api.spaceprobecontrol.spaceprobe.SpaceProbeRequest;
import com.api.spaceprobecontrol.spaceprobe.SpaceProbeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class SpaceProbeServiceImpl implements SpaceProbeService {

    private final PlanetRepository planetRepository;

    public SpaceProbeServiceImpl(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
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

    @Override
    public boolean allCanLand(List<SpaceProbeRequest> request, Long id) {
        return allWithinPlanetBorders(request, id);
    }

    @Override
    public boolean planetExistsById(Long id) {
        return planetRepository.existsById(id);
    }
}
