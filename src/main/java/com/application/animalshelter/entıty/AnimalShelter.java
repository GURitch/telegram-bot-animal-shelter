package com.application.animalshelter.entıty;

import com.application.animalshelter.enums.AnimalType;
import com.application.animalshelter.enums.CityName;
import com.application.animalshelter.enums.CountryName;
import jakarta.persistence.*;


@Entity
public class AnimalShelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Enumerated(EnumType.STRING)
    private CountryName countryName;

    @Enumerated(EnumType.STRING)
    private CityName cityName;

    private String address;

    private String workingHours;

    private String passRules;
    private String shelterRules;

    @Enumerated(EnumType.STRING)
    private AnimalType animalType;
}
