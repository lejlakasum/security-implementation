package com.example.ppis.controller;

import com.example.ppis.dto.LoginDto;
import com.example.ppis.dto.UserDto;
import com.example.ppis.dto.UserLoginDTO;
import com.example.ppis.dto.UserRegisterDTO;
import com.example.ppis.model.User;
import com.example.ppis.service.UserService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    private final Bucket bucket;
    private final Bucket bucket2;

    public UserController() {
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
        Bandwidth limit2 = Bandwidth.classic(100, Refill.greedy(100, Duration.ofMinutes(1)));
        this.bucket2 = Bucket4j.builder()
                .addLimit(limit2)
                .build();
    }

    @PostMapping("/user/login")
    ResponseEntity<LoginDto> login(@RequestBody @Valid UserLoginDTO user) throws Exception {
        if(bucket.tryConsume(1)) {
            return new ResponseEntity<>(userService.login(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

    @PostMapping("/user/register")
    ResponseEntity<UserRegisterDTO> register(@RequestBody @Valid User user) throws Exception {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(userService.postUser(user), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

    @DeleteMapping("/user/{id}")
    ResponseEntity<HashMap<String,String>> deleteUser(@PathVariable Integer id) throws Exception {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(userService.deleteUserById(id), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);

    }

    @GetMapping("/user/all")
    ResponseEntity<List<UserDto>> allUsers() throws Exception {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(userService.getListOfUsers(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);

    }

}
