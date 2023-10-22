package com.application.animalshelter.service;

import com.application.animalshelter.entıty.AnimalShelter;
import com.application.animalshelter.enums.City;

import java.util.List;
import java.util.Set;

public interface AnimalShelterService {
    AnimalShelter saveAnimalShelter(AnimalShelter animalShelter);
    AnimalShelter getAnimalShelter(Long Id);
    List<AnimalShelter> getAnimalShelters();
    List<AnimalShelter> getAnimalSheltersByCity(City city);
    void deleteAnimalShelter(Long Id);

}
