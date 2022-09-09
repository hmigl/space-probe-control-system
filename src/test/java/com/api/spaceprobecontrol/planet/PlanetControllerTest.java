package com.api.spaceprobecontrol.planet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlanetController.class)
class PlanetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanetRepository repository;

    @Test
    @DisplayName("Should perform GET at '/api/planets' and return status 200")
    void test1() throws Exception {
        when(repository.findAll())
                .thenReturn(List.of(new Planet(1, 1), new Planet(2, 2)));

        mockMvc.perform(get("/api/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].xAxis").value(1))
                .andExpect(jsonPath("$[0].yAxis").value(1))
                .andExpect(jsonPath("$[1].xAxis").value(2))
                .andExpect(jsonPath("$[1].xAxis").value(2));
    }

    @Test
    @DisplayName("Should perform GET at '/api/planets/{id}' and return status 200")
    void test2() throws Exception {
        when(repository.findById(1L))
                .thenReturn(Optional.of(new Planet(1, 1)));

        mockMvc.perform(get("/api/planets/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(3)))
                .andExpect(jsonPath("$.xAxis").value(1))
                .andExpect(jsonPath("$.yAxis").value(1));
    }

    @Test
    @DisplayName("Should perform GET at '/api/planets/{id}' and return status 404")
    void test3() throws Exception {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/planets/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}
