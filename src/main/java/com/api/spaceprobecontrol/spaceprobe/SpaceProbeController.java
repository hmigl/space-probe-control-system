package com.api.spaceprobecontrol.spaceprobe;

import com.api.spaceprobecontrol.planet.PlanetRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class SpaceProbeController {

    private final PlanetRepository planetRepository;

    public SpaceProbeController(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @PostMapping("/planets/{id}/probes")
    void registerNewSpaceProbe(@PathVariable("id") Long id,
                               @RequestBody @Valid DesignationSpaceProbeRequest request) {
        if (!planetRepository.existsById(id))
            return;
        request.getSpaceProbes().forEach(System.out::println);
    }
}
