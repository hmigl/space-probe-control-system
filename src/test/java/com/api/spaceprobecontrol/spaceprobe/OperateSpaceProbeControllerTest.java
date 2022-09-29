package com.api.spaceprobecontrol.spaceprobe;

import com.api.spaceprobecontrol.planet.Planet;
import com.api.spaceprobecontrol.planet.PlanetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Validator;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        var request = new LandSpaceProbeRequest(List.of(new LandSpaceProbeRequest.LandState(1, 1, Orientation.NORTH)));
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
        var request = new LandSpaceProbeRequest(List.of(new LandSpaceProbeRequest.LandState(1, 1, Orientation.NORTH)));
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
        var request = new LandSpaceProbeRequest(List.of(new LandSpaceProbeRequest.LandState(1, 1, Orientation.NORTH)));
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
    @DisplayName("Should retrieve all space probes")
    void shouldRetrieveAllSpaceProbes() throws Exception {
        var planetSpy = spy(new Planet(3, 3));
        given(planetRepository.findById(any()))
                .willReturn(Optional.of(planetSpy));
        doReturn(true)
                .when(planetSpy).hasSpaceProbes();
        doReturn(List.of(
                new SpaceProbe(new Point(1, 1), Orientation.NORTH, planetSpy),
                new SpaceProbe(new Point(2, 2), Orientation.SOUTH, planetSpy))
        ).when(planetSpy).getSpaceProbes();

        mockMvc.perform(get(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("planetId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    @DisplayName("Should retrieve specific space probe")
    void shouldRetrieveSpecificSpaceProbe() throws Exception {
        given(spaceProbeService.findById(any()))
                .willReturn(Optional.of(new SpaceProbe(new Point(4, 2), Orientation.NORTH, new Planet(5,5))));

        mockMvc.perform(get(URI + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coordinate.x").value(4))
                .andExpect(jsonPath("$.coordinate.y").value(2))
                .andExpect(jsonPath("$.pointsTo").value("NORTH"));
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
                .andExpect(status().isOk());

        verify(spaceProbeService, times(1)).saveAll(anyIterable());
    }

    @Test
    @DisplayName("Should delete all space probes")
    void shouldDeleteAllSpaceProbes() throws Exception {
        given(spaceProbeService.findAll())
                .willReturn(List.of(
                        new SpaceProbe(new Point(1, 1), Orientation.NORTH, new Planet(1,1)),
                        new SpaceProbe(new Point(1, 1), Orientation.SOUTH, new Planet(2,2))
                ));

        mockMvc.perform(delete(URI))
                .andExpect(status().isOk());

        verify(spaceProbeService).deleteAll();
    }

    @Test
    @DisplayName("Should delete specific space probe")
    void shouldDeleteSpecificSpaceProbe() throws Exception {
        given(spaceProbeService.findById(any()))
                .willReturn(Optional.of(new SpaceProbe(new Point(1, 1), Orientation.EAST, new Planet(1, 1))));

        mockMvc.perform(delete(URI + "/{id}", 1L))
                .andExpect(status().isOk());

        verify(spaceProbeService).delete(any(SpaceProbe.class));
    }
}
