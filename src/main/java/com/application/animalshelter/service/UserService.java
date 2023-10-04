package com.application.animalshelter.service;

import com.application.animalshelter.entıty.AppUser;

import java.util.List;

public interface UserService {
    AppUser addUser(AppUser appUser);
    AppUser findUserById(long id);
    String deleteUserById(long id);
    List<AppUser> findAllUsers();
}
