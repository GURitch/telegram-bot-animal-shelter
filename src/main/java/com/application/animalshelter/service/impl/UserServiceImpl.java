package com.application.animalshelter.service.impl;

import com.application.animalshelter.dao.UserDAO;
import com.application.animalshelter.entıty.AppUser;
import com.application.animalshelter.exception.UserNotFoundException;
import com.application.animalshelter.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public AppUser addUser(AppUser user) {
        return userDAO.save(user);
    }

    @Override
    public AppUser findUserById(long id) {
        return userDAO.findById(id).orElseThrow(()-> new UserNotFoundException("Объект не найден"));
    }

    @Override
    public String deleteUserById(long id) {
        userDAO.delete(findUserById(id));
        return "Объект удален";
    }

    @Override
    public List<AppUser> findAllUsers() {
        return userDAO.findAll();
    }
}
