package com.example.ppis.controller;

import com.example.ppis.dto.LoginDto;
import com.example.ppis.dto.UserDto;
import com.example.ppis.dto.UserLoginDTO;
import com.example.ppis.dto.UserRegisterDTO;
import com.example.ppis.model.User;
import com.example.ppis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/login")
    LoginDto login(@RequestBody @Valid UserLoginDTO user) throws Exception {
        return userService.login(user);
    }

    @PostMapping("/user/register")
    UserRegisterDTO register(@RequestBody @Valid User user) throws Exception {
        return userService.postUser(user);
    }

    @DeleteMapping("/user/{id}")
    HashMap<String,String> deleteUser(@PathVariable Integer id) throws Exception {
        return userService.deleteUserById(id);
    }

    @GetMapping("/user/all")
    List<UserDto> allUsers() throws Exception {
        return userService.getListOfUsers();
    }

}
