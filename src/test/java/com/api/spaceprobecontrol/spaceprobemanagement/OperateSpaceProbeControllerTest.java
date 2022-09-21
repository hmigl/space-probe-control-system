package com.api.spaceprobecontrol.spaceprobemanagement;

import com.api.spaceprobecontrol.planet.Planet;
import com.api.spaceprobecontrol.planet.PlanetRepository;
import com.api.spaceprobecontrol.spaceprobe.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OperateSpaceProbeController.class)
class OperateSpaceProbeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PlanetRepository planetRepository;
    @MockBean
    private SpaceProbeService spaceProbeService;
    @MockBean(name = "mvcValidator")
    private Validator mockValidator;

    private static final String URI = "/api/probes";

    @Test
    @DisplayName("Should register a new space probe")
    void shouldRegisterNewSpaceProbe() throws Exception {
        var planet = new Planet(1, 1);
        var request = new LandSpaceProbeRequest(List.of(new LandSpaceProbeRequest.LandState(1, 1, Directions.NORTH)));
        String json = new ObjectMapper().writeValueAsString(request);

        given(planetRepository.findById(any()))
                .willReturn(Optional.of(planet));
        given(spaceProbeService.allCanLand(request.getSpaceProbes(), planet))
                .willReturn(true);

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .param("planetId", "1"))
                .andExpect(status().isCreated());

        verify(spaceProbeService, times(1)).saveAll(anyIterable());
    }

    @Test
    @DisplayName("Shouldn't be able to land")
    void shouldNotBeAbleToLand() throws Exception {
        var planet = new Planet(1, 1);
        var request = new LandSpaceProbeRequest(List.of(new LandSpaceProbeRequest.LandState(1, 1, Directions.NORTH)));
        String json = new ObjectMapper().writeValueAsString(request);

        given(planetRepository.findById(any()))
                .willReturn(Optional.of(planet));
        given(spaceProbeService.allCanLand(request.getSpaceProbes(), planet))
                .willReturn(false);

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .param("planetId", "1"))
                .andExpect(status().isBadRequest());

        verify(spaceProbeService, times(0)).saveAll(anyIterable());
    }

    @Test
    @DisplayName("Shouldn't find planet")
    void shouldNotFoundPlanet() throws Exception {
        var request = new LandSpaceProbeRequest(List.of(new LandSpaceProbeRequest.LandState(1, 1, Directions.NORTH)));
        String json = new ObjectMapper().writeValueAsString(request);

        given(planetRepository.findById(any()))
                .willReturn(Optional.empty());

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .param("planetId", "42"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should move space probe")
    void shouldMoveSpaceProbe() throws Exception {
        var planet = new Planet(5, 5);
        var request = new MoveSpaceProbeRequest(List.of(
                new MoveSpaceProbeRequest.MovementDemand(1L, "LMLMLMLMM"),
                new MoveSpaceProbeRequest.MovementDemand(2L, "MMRMMRMRRML"))
        );
        String json = new ObjectMapper().writeValueAsString(request);

        given(planetRepository.findById(any()))
                .willReturn(Optional.of(planet));

        mockMvc.perform(put(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .param("planetId", "1"))
                .andExpect(status().isCreated());
        verify(spaceProbeService, times(1)).saveAll(anyIterable());
    }
}
