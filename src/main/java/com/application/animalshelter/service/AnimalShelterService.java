package com.application.animalshelter.service;

import com.application.animalshelter.entıty.AnimalShelter;

public interface AnimalShelterService {
    AnimalShelter saveAnimalShelter(AnimalShelter animalShelter);
    AnimalShelter getAnimalShelter(Long Id);
    void deleteAnimalShelter(AnimalShelter animalShelter);

}
