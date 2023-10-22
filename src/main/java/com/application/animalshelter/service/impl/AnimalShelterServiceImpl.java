package com.application.animalshelter.service.impl;

import com.application.animalshelter.dao.AnimalShelterDAO;
import com.application.animalshelter.dao.AppUserDAO;
import com.application.animalshelter.entıty.AnimalShelter;
import com.application.animalshelter.enums.City;
import com.application.animalshelter.service.AnimalShelterService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    public List<AnimalShelter> getAnimalShelters() {
        return Collections.unmodifiableList(animalShelterDAO.findAll());
    }

    @Override
    public List<AnimalShelter> getAnimalSheltersByCity(City city) {
        return Collections.unmodifiableList(animalShelterDAO.findByCity(city));
    }

    @Override
    public void deleteAnimalShelter(Long Id) {
        AnimalShelter animalShelter = getAnimalShelter(Id);
        if(animalShelter==null){
            //TODO добавить Exception
        }
        animalShelterDAO.delete(animalShelter);
    }
}
