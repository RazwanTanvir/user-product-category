package com.baylor.practicum_new.controller;

import com.baylor.practicum_new.dto.LoginRequestDTO;
import com.baylor.practicum_new.dto.UserResponseDTO;
import com.baylor.practicum_new.entities.User;
import com.baylor.practicum_new.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        String userEmail = user.getEmail();

        try {
            User savedUser = userService.createUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            if ("User already exists.".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User creation failed.");
            }
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {

        Optional<User> user = userService.authenticateUser(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        if (user.isPresent()) {
            UserResponseDTO userResponse = new UserResponseDTO();
            userResponse.setUserId(user.get().getUserId());
            userResponse.setName(user.get().getName());
            userResponse.setEmail(user.get().getEmail());

            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials");
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long userId){
        User user = userService.getUserById(userId);
        if (user != null)
            return new ResponseEntity<>(user, HttpStatus.OK);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");

    }


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}