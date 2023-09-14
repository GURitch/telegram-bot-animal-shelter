package com.application.animalshelter.listener;

import com.application.animalshelter.entıty.AnimalShelter;
import com.application.animalshelter.service.AnimalShelterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animal-shelter")
public class VolunteerController {
    private final AnimalShelterService animalShelterService;

    public VolunteerController(AnimalShelterService animalShelterService) {
        this.animalShelterService = animalShelterService;
    }

    @PostMapping()
    public ResponseEntity<AnimalShelter> addNewShelter(@RequestBody AnimalShelter animalShelter){
        if(animalShelter==null){
            return ResponseEntity.notFound().build();
        }

        AnimalShelter savedAnimalShelter = animalShelterService.saveAnimalShelter(animalShelter);
        return ResponseEntity.ok(savedAnimalShelter);
    }

    @GetMapping()
    public void getNewShelter(@RequestParam Long Id){
    }

}
