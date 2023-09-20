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

    public void setId(Long id) {
        this.id = id;
    }

    public void setCountryName(CountryName countryName) {
        this.countryName = countryName;
    }

    public void setCityName(CityName cityName) {
        this.cityName = cityName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public void setPassRules(String passRules) {
        this.passRules = passRules;
    }

    public void setShelterRules(String shelterRules) {
        this.shelterRules = shelterRules;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

    public void setAnimals(Collection<Animal> animals) {
        this.animals = animals;
    }
}
