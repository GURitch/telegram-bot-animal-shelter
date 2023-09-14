package com.application.animalshelter.service.impl;

import com.application.animalshelter.dao.AnimalShelterDAO;
import com.application.animalshelter.dao.AppUserDAO;
import com.application.animalshelter.entıty.AnimalShelter;
import com.application.animalshelter.service.AnimalShelterService;
import org.springframework.stereotype.Service;

@Service
public class AnimalShelterServiceImpl implements AnimalShelterService {
    private final AnimalShelterDAO animalShelterDAO;

    public AnimalShelterServiceImpl(AnimalShelterDAO animalShelterDAO) {
        this.animalShelterDAO = animalShelterDAO;
    }

    @Override
    public AnimalShelter saveAnimalShelter(AnimalShelter animalShelter) {
        return  animalShelterDAO.save(animalShelter);
    }

    @Override
    public AnimalShelter getAnimalShelter(Long Id) {
        return animalShelterDAO.findById(Id).orElse(null);
    }

    @Override
    public void deleteAnimalShelter(AnimalShelter animalShelter) {
        animalShelterDAO.delete(animalShelter);
    }
}
