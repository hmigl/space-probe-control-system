package com.api.spaceprobecontrol.spaceprobe;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class SpaceProbeController {

    private final SpaceProbeService spaceProbeService;

    public SpaceProbeController(SpaceProbeService spaceProbeService) {
        this.spaceProbeService = spaceProbeService;
    }

    @PostMapping("/planets/{id}/probes")
    ResponseEntity<?> registerNewSpaceProbe(@PathVariable("id") Long id,
                                         @RequestBody @Valid DesignationSpaceProbeRequest request) {
        if (!spaceProbeService.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There's no planet with id " + id);
        if (!spaceProbeService.allCanLand(request, id))
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build(); // throw custom exception?
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
