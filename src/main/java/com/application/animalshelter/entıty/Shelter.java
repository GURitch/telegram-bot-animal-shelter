package com.application.animalshelter.entıty;

import com.application.animalshelter.enums.AnimalType;
import com.application.animalshelter.enums.CityName;
import com.application.animalshelter.enums.CountryName;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Collection;


@Entity
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "shelter")
    private Collection<Animal> animals;

    public Long getId() {
        return id;
    }

    public CountryName getCountryName() {
        return countryName;
    }

    public CityName getCityName() {
        return cityName;
    }

    public String getAddress() {
        return address;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public String getPassRules() {
        return passRules;
    }

    public String getShelterRules() {
        return shelterRules;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public Collection<Animal> getAnimals() {
        return animals;
    }
}
