package com.application.animalshelter.dao;

import com.application.animalshelter.entıty.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<AppUser, Long> {
    AppUser findByTelegramUserId(Long telegramUserId);

//    @Query("UPDATE AppUser u SET u.shelterType = :shelterType WHERE u.telegramUserId = :userId")
//    void changeShelterTypeAtUser(@Param("userId") long userId, @Param("animalType") String shelterType);
}
