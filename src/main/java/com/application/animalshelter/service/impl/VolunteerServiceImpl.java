package com.application.animalshelter.service.impl;

import com.application.animalshelter.dao.VolunteerDAO;
import com.application.animalshelter.entıty.Volunteer;
import com.application.animalshelter.service.VolunteerService;
import org.springframework.stereotype.Service;

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
        return null;
    }

    @Override
    public String deleteVolunteerById(long id) {
        return null;
    }

    @Override
    public Volunteer editVolunteer(Volunteer volunteer) {
        return null;
    }
}
