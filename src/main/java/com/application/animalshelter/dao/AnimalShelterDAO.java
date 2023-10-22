package com.application.animalshelter.dao;

import com.application.animalshelter.entıty.AnimalShelter;
import com.application.animalshelter.enums.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalShelterDAO extends JpaRepository<AnimalShelter, Long> {
    List<AnimalShelter> findByCity(City city);
    AnimalShelter findByName(String name);
}
