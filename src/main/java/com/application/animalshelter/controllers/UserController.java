package com.application.animalshelter.controllers;

import com.application.animalshelter.entıty.AppUser;
import com.application.animalshelter.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<AppUser> addUser(@RequestBody AppUser user){
        return ResponseEntity.ok(userService.addUser(user));
    }
    @GetMapping
    public ResponseEntity<AppUser> getUserById(@RequestParam long id){
        return ResponseEntity.ok(userService.findUserById(id));
    }
    @DeleteMapping
    public ResponseEntity<String> deleteUserById(@RequestParam long id){
        return ResponseEntity.ok(userService.deleteUserById(id));
    }
    @GetMapping("/all")
    public ResponseEntity<List<AppUser>> getAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }
}
