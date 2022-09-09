package com.api.spaceprobecontrol.planet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].xAxis").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].yAxis").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].xAxis").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].xAxis").value(2));
    }

    @Test
    @DisplayName("Should perform GET at '/api/planets/{id}' and return status 200")
    void test2() throws Exception {
        when(repository.findById(1L))
                .thenReturn(Optional.of(new Planet(1, 1)));

        mockMvc.perform(get("/api/planets/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.xAxis").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.yAxis").value(1));
    }

    @Test
    @DisplayName("Should perform GET at '/api/planets/{id}' and return status 404")
    void test3() throws Exception {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/planets/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Should perform POST at '/api/planets' and return status 201")
    void test4() throws Exception {
        mockMvc.perform(post("/api/planets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"xAxis\": 2, \"yAxis\": 2}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("location"))
                .andExpect(MockMvcResultMatchers.header().string("location", containsString("planets/")));
    }

    @Test
    @DisplayName("Should perform POST at '/api/planets' and return status 400")
    void test5() throws Exception {
        mockMvc.perform(post("/api/planets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
