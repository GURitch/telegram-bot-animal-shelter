package com.application.animalshelter.controllers;

import com.application.animalshelter.entıty.Animal;
import com.application.animalshelter.entıty.Shelter;
import com.application.animalshelter.service.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/animal")
public class AnimalController {
    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping()
    public ResponseEntity<Animal> addAnimal (@RequestBody Animal animal){
        return ResponseEntity.ok(animalService.addAnimal(animal));
    }
    @DeleteMapping()
    public ResponseEntity<Animal> deleteAnimal (@RequestParam Long id){
        animalService.deleteAnimalById(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping()
    public ResponseEntity<Optional<Animal>> getAnimal(@RequestParam Long id){
        return ResponseEntity.ok(animalService.findAnimal(id));
    }
    @GetMapping("/getAll")
    public ResponseEntity<Collection<Animal>> getAllAnimals(){
        return ResponseEntity.ok(animalService.getAllAnimal());
    }
}
