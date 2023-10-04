package com.application.animalshelter.dao;

import com.application.animalshelter.entıty.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerDAO extends JpaRepository<Volunteer, Long> {
}
