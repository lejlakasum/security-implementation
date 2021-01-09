package com.example.ppis.controller;

import com.example.ppis.dto.ChangePasswordDto;
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
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    @Valid
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

    @PostMapping("/user/validate")
    @Valid
    ResponseEntity<Void> validateToken(@RequestBody @Valid LoginDto request, @RequestParam String role) {
        try {
            userService.validateToken(request.token, role);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/user/change-password")
    @Valid
    ResponseEntity<UserDto> changePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto) {
        if(bucket2.tryConsume(1)) {
            try {
                return new ResponseEntity<>(userService.changePassword(changePasswordDto), HttpStatus.OK);
            }
            catch (NotFoundException ex) {
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            catch (Exception ex) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

    @GetMapping("/user/{email}")
    ResponseEntity<UserDto> getUserByEmail(@PathVariable String email, @RequestHeader("Authorization") String token) {
        if(bucket2.tryConsume(1)) {
            try {
                return new ResponseEntity<>(userService.getUserByEmail(email, token) ,HttpStatus.OK);
            }
            catch (Exception ex) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

}
