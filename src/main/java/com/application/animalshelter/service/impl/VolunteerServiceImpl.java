package com.application.animalshelter.service.impl;

import com.application.animalshelter.dao.VolunteerDAO;
import com.application.animalshelter.entıty.Volunteer;
import com.application.animalshelter.exception.VolunteerNotFoundException;
import com.application.animalshelter.service.VolunteerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteerServiceImpl implements VolunteerService {
    private final VolunteerDAO volunteerDAO;

    public VolunteerServiceImpl(VolunteerDAO volunteerDAO) {
        this.volunteerDAO = volunteerDAO;
    }

    @Override
    public Volunteer addVolunteer(Volunteer volunteer) {
        return volunteerDAO.save(volunteer);
    }

    @Override
    public Volunteer findVolunteerById(long id) {
        return volunteerDAO.findById(id).orElseThrow(()->new VolunteerNotFoundException("Объект не найден"));
    }

    @Override
    public String deleteVolunteerById(long id) {
        return null;
    }

    @Override
    public Volunteer editVolunteer(Volunteer volunteer) {
        return null;
    }

    @Override
    public List<Volunteer> getAllVolunteers() {
        return volunteerDAO.findAll();
    }
}
