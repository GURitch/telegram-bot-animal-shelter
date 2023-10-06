package com.application.animalshelter.service;

import com.application.animalshelter.entıty.Volunteer;

import java.util.List;

public interface VolunteerService {
    Volunteer addVolunteer(Volunteer volunteer);

    Volunteer findVolunteerById(long id);

    String deleteVolunteerById(long id);

    Volunteer editVolunteer(Volunteer volunteer);
    List<Volunteer> getAllVolunteers();
}
