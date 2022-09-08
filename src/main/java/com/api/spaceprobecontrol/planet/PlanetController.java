package com.api.spaceprobecontrol.planet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

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
}
