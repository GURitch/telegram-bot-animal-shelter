package com.application.animalshelter.controllers;

import com.application.animalshelter.entıty.Animal;
import com.application.animalshelter.entıty.AppUser;
import com.application.animalshelter.entıty.Shelter;
import com.application.animalshelter.service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;


@RestController
@RequestMapping("/shelter")
public class ShelterController {
    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @Operation(summary = "ДОБАВИТЬ ПРИЮТ",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Приют добавлен",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Shelter.class)
                    )
            )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Введите объект в формате JSON"
            ),
            tags = "Приюты"
    )
    @PostMapping()
    public ResponseEntity<Shelter> addNewShelter(@RequestBody Shelter shelter){
        if(shelter ==null){
            return ResponseEntity.notFound().build();
        }

        Shelter savedShelter = shelterService.addShelter(shelter);
        return ResponseEntity.ok(savedShelter);
    }

    @PutMapping
    public ResponseEntity<Shelter> editUser(@RequestBody Shelter shelter){
        return ResponseEntity.ok(shelterService.editShelter(shelter));
    }

    @Operation(summary = "ПОЛУЧИТЬ ПО ID",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Получен приют",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Shelter.class)
                    )
            ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Приют не найден в БД"
                    )
            },
            tags = "Приюты"
    )
    @GetMapping()
    public ResponseEntity<Shelter> getShelter(@Parameter(description = "ID приюта", example = "1")@RequestParam Long id){
        return ResponseEntity.ok(shelterService.findShelterById(id));
    }

    @Operation(summary = "УДАЛИТЬ ПО ID",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Приют удален",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Shelter.class)
                    )
            )
            },
            tags = "Приюты"
    )
    @DeleteMapping()
    public ResponseEntity<String> deleteShelterById(@RequestParam long id){
        return ResponseEntity.ok(shelterService.deleteShelterById(id));
    }

    @Operation(summary = "ПОЛУЧИТЬ ВСЕ ПРИЮТЫ",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Полученные приюты",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Animal.class))
                    )
            )
            },
            tags = "Приюты"
    )
    @GetMapping("/getAll")
    public ResponseEntity<Collection<Shelter>> getAllShelters(){
        return ResponseEntity.ok(shelterService.getAllShelters());
    }
}
