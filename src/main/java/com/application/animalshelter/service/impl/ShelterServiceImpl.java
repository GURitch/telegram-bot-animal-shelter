package com.application.animalshelter.service.impl;

import com.application.animalshelter.dao.ShelterDAO;
import com.application.animalshelter.entıty.Shelter;
import com.application.animalshelter.exception.ShelterNotFoundException;
import com.application.animalshelter.service.ShelterService;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Optional;

@Service
public class ShelterServiceImpl implements ShelterService {
    private final ShelterDAO shelterDAO;

    public ShelterServiceImpl(ShelterDAO shelterDAO) {
        this.shelterDAO = shelterDAO;
    }

    @Override
    public Shelter addShelter(Shelter shelter) {
        return  shelterDAO.save(shelter);
    }

    @Override
    public Shelter findShelterById(long id) {
        return shelterDAO.findById(id).orElseThrow(ShelterNotFoundException::new);
    }
    @Override
    public String deleteShelterById(long id) {
        shelterDAO.delete(findShelterById(id));
        return "Объект удален";
    }

    @Override
    public Collection<Shelter> getAllShelters() {
        return shelterDAO.findAll();
    }

    @Override
    public Shelter editShelter(Shelter shelter) {
        return shelterDAO.save(shelter);
    }
}
