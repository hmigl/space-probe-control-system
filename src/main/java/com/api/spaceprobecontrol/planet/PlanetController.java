package com.api.spaceprobecontrol.planet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {

    private final PlanetRepository repository;

    public PlanetController(PlanetRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    ResponseEntity<Object> registerNewPlanet(@RequestBody @Valid RegisterPlanetRequest request) {
        var newPlanet = request.toModel();
        repository.save(newPlanet);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("location", "/api/planets/" + newPlanet.getId())
                .build();
    }

    @GetMapping
    ResponseEntity<?> showPlanets() {
        return ResponseEntity.status(HttpStatus.OK).body(repository.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> showSpecificPlanet(@PathVariable("id") Long id) {
        return repository.findById(id)
                .map(planet -> ResponseEntity.status(HttpStatus.OK).body(planet))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    @Transactional
    ResponseEntity<?> updatePlanet(@RequestBody @Valid RegisterPlanetRequest request, @PathVariable Long id) {
        Optional<Planet> possiblePlanet = repository.findById(id);

        return possiblePlanet.map(planet -> {
            if (planet.hasSpaceProbes()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            planet.reshape(request);
            return ResponseEntity.status(HttpStatus.OK).body(repository.save(planet));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
