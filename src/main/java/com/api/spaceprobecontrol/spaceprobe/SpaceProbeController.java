package com.api.spaceprobecontrol.spaceprobe;

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
    void registerNewSpaceProbe(@PathVariable("id") Long id,
                               @RequestBody @Valid DesignationSpaceProbeRequest request) {
        if (!spaceProbeService.allCanLand(request, id)) {
            System.out.println("uepa!");
            return;
        }
//        spaceProbeService.process(request);
        request.getSpaceProbes().forEach(System.out::println);
    }
}
