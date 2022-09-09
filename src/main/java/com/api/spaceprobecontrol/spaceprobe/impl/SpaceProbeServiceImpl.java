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

    private final PlanetRepository repository;

    public SpaceProbeServiceImpl(PlanetRepository repository) {
        this.repository = repository;
    }

    private boolean allWithinPlanetBorders(List<SpaceProbeRequest> uncommonSpaceProbes, Long id) {
        Optional<Planet> possiblePlanet = repository.findById(id);

        return possiblePlanet.map(planet -> {
            Predicate<SpaceProbeRequest> respectsX = p -> p.getState().getxAxis() <= planet.getxAxis();
            Predicate<SpaceProbeRequest> respectsY = p -> p.getState().getyAxis() <= planet.getyAxis();

            return uncommonSpaceProbes
                    .stream()
                    .allMatch(respectsX.and(respectsY));
        }).orElse(false);
    }

    @Override
    public boolean allCanLand(List<SpaceProbeRequest> request, Long id) {
        return allWithinPlanetBorders(request, id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
