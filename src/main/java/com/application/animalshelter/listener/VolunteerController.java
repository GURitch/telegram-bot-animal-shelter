package com.application.animalshelter.listener;

import com.application.animalshelter.entıty.AnimalShelter;
import com.application.animalshelter.service.AnimalShelterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * API for volunteers
 */

@RestController
@RequestMapping("/animal-shelter")
public class VolunteerController {
    private final AnimalShelterService animalShelterService;

    public VolunteerController(AnimalShelterService animalShelterService) {
        this.animalShelterService = animalShelterService;
    }

    /**
     * a volunteer can add a new shelter or change an existing shelter
     * @param animalShelter must not be null
     * @return ResponseEntity 200 code
     * @see org.springframework.data.jpa.repository.JpaRepository#save(Object)
     */

    @PostMapping()
    public ResponseEntity<AnimalShelter> addNewShelter(@RequestBody AnimalShelter animalShelter){
        if(animalShelter==null){
            return ResponseEntity.notFound().build();
        }

        AnimalShelter savedAnimalShelter = animalShelterService.saveAnimalShelter(animalShelter);
        return ResponseEntity.ok(savedAnimalShelter);
    }

    /**
     * a volunteer can get the existing shelter by its id
     * @param Id must not be null
     * @return ResponseEntity 200 code
     * @see org.springframework.data.jpa.repository.JpaRepository#save(Object)
     */
    @GetMapping()
    public ResponseEntity<AnimalShelter> getShelter(@PathVariable Long Id){
        AnimalShelter animalShelter = animalShelterService.getAnimalShelter(Id);
        return ResponseEntity.ok(animalShelter);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AnimalShelter>> getAllShelters(){
        List<AnimalShelter> animalShelters = animalShelterService.getAnimalShelters();
        return ResponseEntity.ok(animalShelters);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteAnimalShelter(@PathVariable Long Id){
        animalShelterService.deleteAnimalShelter(Id);
        return ResponseEntity.ok().build();
    }

}
