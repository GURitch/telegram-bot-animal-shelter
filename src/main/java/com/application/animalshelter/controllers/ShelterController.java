package com.application.animalshelter.controllers;

import com.application.animalshelter.entıty.Shelter;
import com.application.animalshelter.service.ShelterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/shelter")
public class ShelterController {
    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @PostMapping()
    public ResponseEntity<Shelter> addNewShelter(@RequestBody Shelter shelter){
        if(shelter ==null){
            return ResponseEntity.notFound().build();
        }

        Shelter savedShelter = shelterService.saveShelter(shelter);
        return ResponseEntity.ok(savedShelter);
    }

    @GetMapping()
    public ResponseEntity<Shelter> getShelter(@RequestParam Long id){
        Optional<Shelter> shelter = shelterService.getShelter(id);
        if(shelter.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shelter.get());
    }

    @GetMapping("/getAll")
    public ResponseEntity<Collection<Shelter>> getAllShelters(){
        return ResponseEntity.ok(shelterService.getAllShelters());
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteShelter(@RequestBody Shelter shelter){
        shelterService.deleteShelter(shelter);
        return ResponseEntity.ok().build();
    }
}
