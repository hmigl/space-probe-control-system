package com.api.spaceprobecontrol.spaceprobe;

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
    ResponseEntity<?> registerNewSpaceProbe(@RequestBody @Valid LandSpaceProbeRequest request,
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

    @GetMapping
    ResponseEntity<?> retrieveSpaceProbesByPlanet(@RequestParam("planetId") Long id) {
        Optional<Planet> possiblePlanet = planetRepository.findById(id);

        return possiblePlanet.map(planet -> ResponseEntity.status(HttpStatus.OK).body(planet.getSpaceProbes()))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> retrieveSpecificSpaceProbe(@PathVariable Long id) {
        Optional<SpaceProbe> possibleProbe = spaceProbeService.findById(id);

        return possibleProbe.map(probe -> ResponseEntity.status(HttpStatus.OK).body(probe))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping
    ResponseEntity<?> moveSpaceProbe(@RequestBody @Valid MoveSpaceProbeRequest request,
                                     @RequestParam("planetId") Long id) {
        Optional<Planet> possiblePlanet = planetRepository.findById(id);

        return possiblePlanet.map(planet -> {
            List<SpaceProbe> repositionedSpaceProbes = spaceProbeService.processInstructions(request.getInstructions());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(spaceProbeService.saveAll(repositionedSpaceProbes));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping
    ResponseEntity<?> deleteAllSpaceProbes() {
        List<SpaceProbe> spaceProbes = spaceProbeService.findAll();

        if (spaceProbes.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no space probes to delete");
        spaceProbeService.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("Space probes were deleted successfully");
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteSpaceProbe(@PathVariable Long id) {
        Optional<SpaceProbe> possibleProbe = spaceProbeService.findById(id);

        return possibleProbe.map(probe -> {
            spaceProbeService.delete(probe);
            return ResponseEntity.status(HttpStatus.OK).body("Space Probe "+id+" deleted successfully");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
