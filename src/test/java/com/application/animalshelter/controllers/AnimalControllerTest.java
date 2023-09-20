package com.application.animalshelter.controllers;

import com.application.animalshelter.entıty.Animal;
import com.application.animalshelter.service.AnimalService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
class AnimalControllerTest {
    @Mock
    private AnimalService animalService;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AnimalController(animalService)).build();
    }

    @Test
    void testAddAnimal() throws Exception {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setName("Test Animal");

        when(animalService.addAnimal(any(Animal.class))).thenReturn(animal);

        mockMvc.perform(MockMvcRequestBuilders.post("/animal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Animal\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Animal"));
    }

    @Test
    void testDeleteAnimal() throws Exception {
        Long animalId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/animal")
                        .param("id", animalId.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(animalService, times(1)).deleteAnimalById(animalId);
    }

    @Test
    void testGetAnimal() throws Exception {
        Long animalId = 1L;
        Animal animal = new Animal();
        animal.setId(animalId);
        animal.setName("Test Animal");

        when(animalService.findAnimal(animalId)).thenReturn(Optional.of(animal));

        mockMvc.perform(MockMvcRequestBuilders.get("/animal")
                        .param("id", animalId.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Animal"));
    }

    @Test
    void testGetAllAnimals() throws Exception {
        Animal animal1 = new Animal();
        animal1.setId(1L);
        animal1.setName("Animal 1");

        Animal animal2 = new Animal();
        animal2.setId(2L);
        animal2.setName("Animal 2");

        when(animalService.getAllAnimal()).thenReturn(List.of(animal1, animal2));

        mockMvc.perform(MockMvcRequestBuilders.get("/animal/getAll"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Animal 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Animal 2"));
    }
}