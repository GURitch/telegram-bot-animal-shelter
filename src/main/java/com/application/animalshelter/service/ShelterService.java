package com.application.animalshelter.service;

import com.application.animalshelter.entıty.Shelter;
import java.util.Collection;
import java.util.Optional;

public interface ShelterService {
    Shelter addShelter(Shelter shelter);
    Shelter findShelterById(long Id);
    String deleteShelterById(long id);
    Collection<Shelter> getAllShelters();

}
