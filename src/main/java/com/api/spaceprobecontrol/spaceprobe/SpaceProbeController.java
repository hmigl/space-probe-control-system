package com.api.spaceprobecontrol.spaceprobe;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
        List<SpaceProbeRequest> requests = request.getSpaceProbes();

        if (!spaceProbeService.planetExistsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There's no planet with id " + id);
        if (!spaceProbeService.allCanLand(requests, id))
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build(); // throw custom exception?
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
