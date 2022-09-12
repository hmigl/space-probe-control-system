package com.api.spaceprobecontrol.spaceprobe;

import com.api.spaceprobecontrol.planet.Planet;
import com.api.spaceprobecontrol.planet.PlanetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SpaceProbeController {

    private final PlanetRepository planetRepository;
    private final SpaceProbeService spaceProbeService;

    public SpaceProbeController(PlanetRepository planetRepository, SpaceProbeService spaceProbeService) {
        this.planetRepository = planetRepository;
        this.spaceProbeService = spaceProbeService;
    }

    @PostMapping("/planets/{id}/probes")
    ResponseEntity<?> registerNewSpaceProbe(@PathVariable("id") Long id,
                                            @RequestBody @Valid DesignationSpaceProbeRequest request) {
        Optional<Planet> possiblePlanet = planetRepository.findById(id);

        return possiblePlanet.map(planet -> {
            if (!spaceProbeService.allCanLand(request.getSpaceProbes(), planet))
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Make sure the coordinates aren't duplicated, don't conflict with existing ones and respect planet borders");

            return ResponseEntity.status(HttpStatus.CREATED).body(spaceProbeService.saveAll(request.toModel(planet)));

        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("There's no planet with id " + id));
    }
}
