package com.api.spaceprobecontrol.spaceprobe;

import com.api.spaceprobecontrol.shared.ExistentId;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.List;

public class MoveSpaceProbeRequest {
    @Valid
    @NotNull
    @NotEmpty
    private final List<MovementDemand> instructions;

    public MoveSpaceProbeRequest(@JsonProperty("instructions") List<MovementDemand> instructions) {
        this.instructions = instructions;
    }

    public List<MovementDemand> getInstructions() {
        return instructions;
    }

    public static class MovementDemand {
        @Positive
        @NotNull
        @ExistentId(fieldName = "id", domainClass = SpaceProbe.class)
        private final Long probeId;
        @NotNull
        @Pattern(regexp = "[MLR]+")
        private final String command;

        public MovementDemand(Long probeId, String command) {
            this.probeId = probeId;
            this.command = command;
        }

        public Long getProbeId() {
            return probeId;
        }

        public String getCommand() {
            return command;
        }
    }
}
