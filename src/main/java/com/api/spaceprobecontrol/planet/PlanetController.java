package com.api.spaceprobecontrol.planet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
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
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(repository.save(newPlanet));
    }

    @GetMapping
    ResponseEntity<?> showPlanets() {
        List<Planet> planets = (List<Planet>) repository.findAll();

        if (planets.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no planets");
        return ResponseEntity.status(HttpStatus.OK).body(planets);
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

    @DeleteMapping
    @Transactional
    ResponseEntity<?> deleteAllPlanets() {
        List<Planet> planets = (List<Planet>) repository.findAll();

        if (planets.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no planets to delete");
        repository.deleteAll(); return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    ResponseEntity<?> deletePlanet(@PathVariable Long id) {
        Optional<Planet> possiblePlanet = repository.findById(id);

        return possiblePlanet.map(planet -> {
            repository.delete(planet);
            return ResponseEntity.status(HttpStatus.OK).body("Planet "+id+" deleted successfully");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
