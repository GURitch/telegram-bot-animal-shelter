package com.application.animalshelter.controllers;

import com.application.animalshelter.entıty.Volunteer;
import com.application.animalshelter.service.VolunteerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {
    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @PostMapping
    public ResponseEntity<Volunteer> addVolunteer(@RequestBody Volunteer volunteer){
        return ResponseEntity.ok(volunteerService.addVolunteer(volunteer));
    }
    @GetMapping
    public ResponseEntity<Volunteer> getVolunteerById(@RequestParam long id){
        return ResponseEntity.ok(volunteerService.findVolunteerById(id));
    }
    @DeleteMapping
    public ResponseEntity<String> deleteVolunteerById(@RequestParam long id){
        return ResponseEntity.ok(volunteerService.deleteVolunteerById(id));
    }
}
