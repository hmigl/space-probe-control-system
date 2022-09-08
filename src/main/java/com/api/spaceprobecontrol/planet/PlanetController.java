package com.api.spaceprobecontrol.planet;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {

    @PostMapping
    void registerNewPlanet(@RequestBody @Valid RegisterPlanetRequest request) {
        var newPlanet = request.toModel();
        System.out.println(newPlanet.toString());
    }
}
