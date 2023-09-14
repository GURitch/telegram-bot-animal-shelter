package com.application.animalshelter.dao;

import com.application.animalshelter.entıty.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {
    AppUser findByTelegramUserId(Long telegramUserId);
}