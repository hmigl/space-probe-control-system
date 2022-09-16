package com.api.spaceprobecontrol.managespaceprobe;

import com.api.spaceprobecontrol.planet.Planet;
import com.api.spaceprobecontrol.planet.PlanetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/probes")
public class OperateSpaceProbeController {

    private final PlanetRepository planetRepository;
    private final SpaceProbeService spaceProbeService;

    public OperateSpaceProbeController(PlanetRepository planetRepository, SpaceProbeService spaceProbeService) {
        this.planetRepository = planetRepository;
        this.spaceProbeService = spaceProbeService;
    }

    @PostMapping
    ResponseEntity<?> registerNewSpaceProbe(@RequestBody @Valid DesignationSpaceProbeRequest request,
                                            @RequestParam("planetId") Long id) {
        Optional<Planet> possiblePlanet = planetRepository.findById(id);

        return possiblePlanet.map(planet -> {
            if (!spaceProbeService.allCanLand(request.getSpaceProbes(), planet))
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Make sure the coordinates aren't duplicated, don't conflict with existing ones and respect planet borders");

            return ResponseEntity.status(HttpStatus.CREATED).body(spaceProbeService.saveAll(request.toModel(planet)));

        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping
    ResponseEntity<?> moveSpaceProbe(@RequestBody @Valid MoveSpaceProbeRequest request,
                                     @RequestParam("planetId") Long id) {
        Optional<Planet> possiblePlanet = planetRepository.findById(id);

        return possiblePlanet.map(planet -> {
            List<SpaceProbe> repositionedSpaceProbes = spaceProbeService.processInstructions(request.getInstructions(), planet);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(spaceProbeService.saveAll(repositionedSpaceProbes));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
