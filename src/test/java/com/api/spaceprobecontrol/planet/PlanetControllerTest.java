package com.api.spaceprobecontrol.planet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlanetController.class)
class PlanetControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PlanetRepository repository;

    private static final String URI = "/api/planets";

    @Test
    @DisplayName("Should list all planets")
    void shouldListAllPlanets() throws Exception {
        given(repository.findAll())
                .willReturn(List.of(new Planet(1, 1), new Planet(2, 2)));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].xAxis").value(1))
                .andExpect(jsonPath("$[0].yAxis").value(1))
                .andExpect(jsonPath("$[1].xAxis").value(2))
                .andExpect(jsonPath("$[1].xAxis").value(2));
    }

    @Test
    @DisplayName("Should show details about existent planet")
    void shouldShowExistentPlanetDetails() throws Exception {
        given(repository.findById(any()))
                .willReturn(Optional.of(new Planet(1, 1)));

        mockMvc.perform(get(URI + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(4)))
                .andExpect(jsonPath("$.xAxis").value(1))
                .andExpect(jsonPath("$.yAxis").value(1));
    }

    @Test
    @DisplayName("Should not find planet")
    void shouldNotFindPlanet() throws Exception {
        given(repository.findById(any()))
                .willReturn(Optional.empty());

        mockMvc.perform(get(URI + "/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should register new planet")
    void shouldRegisterNewPlanet() throws Exception {
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"xAxis\": 2, \"yAxis\": 2}"))
                .andExpect(status().isCreated());

        verify(repository, times(1)).save(any(Planet.class));
    }

    @Test
    @DisplayName("Shouldn't reshape planet")
    void shouldNotReshapePlanet() throws Exception {
        var planetSpy = spy(new Planet(1, 1));

        given(repository.findById(any()))
                .willReturn(Optional.of(planetSpy));
        doReturn(true)
                .when(planetSpy).hasSpaceProbes();

        mockMvc.perform(put(URI + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"xAxis\": 2, \"yAxis\": 2}"))
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("Should reshape planet")
    void shouldReshapePlanet() throws Exception {
        var planetSpy = spy(new Planet(3, 3));

        given(repository.findById(any()))
                .willReturn(Optional.of(planetSpy));
        doReturn(false)
                .when(planetSpy).hasSpaceProbes();

        mockMvc.perform(put(URI + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"xAxis\": 2, \"yAxis\": 2}"))
                .andExpect(status().isOk());

        verify(planetSpy).reshape(any(RegisterPlanetRequest.class));
        verify(repository).save(any(Planet.class));
    }

    @Test
    @DisplayName("Should delete all planets")
    void shouldDeleteAllPlanets() throws Exception {
        given(repository.findAll())
                .willReturn(List.of(new Planet(1, 1), new Planet(2, 2)));

        mockMvc.perform(delete(URI))
                .andExpect(status().isOk());

        verify(repository).deleteAll();
    }

    @Test
    @DisplayName("Should find no planets to delete")
    void shouldFindNoPlanetsToDelete() throws Exception {
        mockMvc.perform(delete(URI))
                .andExpect(status().isNotFound());

        verify(repository, times(0)).deleteAll();
    }

    @Test
    @DisplayName("Should delete specific planet")
    void shouldDeleteSpecificPlanet() throws Exception {
        given(repository.findById(any()))
                .willReturn(Optional.of(new Planet(1, 1)));

        mockMvc.perform(delete(URI + "/{id}", 1L))
                .andExpect(status().isOk());

        verify(repository).delete(any(Planet.class));
    }
}
