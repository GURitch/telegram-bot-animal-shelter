package com.application.animalshelter.dao;

import com.application.animalshelter.entıty.AnimalShelter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalShelterDAO extends JpaRepository<AnimalShelter, Long> {
}
