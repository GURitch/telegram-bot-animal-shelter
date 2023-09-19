package com.application.animalshelter.controllers;

import com.application.animalshelter.entıty.Animal;
import com.application.animalshelter.service.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animal")
public class AnimalController {
    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping("/add")
    public ResponseEntity<Animal> addAnimal (@RequestBody Animal animal){
        return ResponseEntity.ok(animalService.addAnimal(animal));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Animal> deleteAnimal (Long id){
        animalService.deleteAnimalById(id);
        return ResponseEntity.ok().build();
    }
}
