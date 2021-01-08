package com.example.ppis.service;

import com.example.ppis.dto.ChangePasswordDto;
import com.example.ppis.dto.LoginDto;
import com.example.ppis.dto.ResponseMessageDTO;
import com.example.ppis.dto.UserDto;
import com.example.ppis.dto.UserLoginDTO;
import com.example.ppis.dto.UserRegisterDTO;
import com.example.ppis.dto.UserRoleDTO;
import com.example.ppis.model.Role;
import com.example.ppis.model.User;
import com.example.ppis.repository.RoleRepository;
import com.example.ppis.repository.UserRepository;
import com.example.ppis.security.CustomUserDetails;
import com.example.ppis.security.JwtUtil;
import com.example.ppis.security.RepositoryAwareUserDetailsService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final RepositoryAwareUserDetailsService userDetailsService;

    private final String SECRET_KEY;

    @Autowired
    public UserService(AuthenticationManager authenticationManager,
                                 PasswordEncoder passwordEncoder,
                                 RepositoryAwareUserDetailsService userDetailsService,
                                 @Value("${secret-key}") String SECRET_KEY) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.SECRET_KEY = SECRET_KEY;
        this.passwordEncoder=passwordEncoder;
    }


    private String hashPassword(String password) {
        String hashPassword = passwordEncoder.encode(password);
        return hashPassword;
    }

    public void validateToken(String token, String role) throws Exception {
        String username = JwtUtil.extractUsername(token, SECRET_KEY);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        Boolean valid = JwtUtil.validateToken(token, userDetails, SECRET_KEY);
        User user = userRepository.findByEmail(username);
        if(role.equals("admin")) {
            if(!valid || !user.getRole().getName().equals("admin")) {
                throw new Exception("Invalid token");
            }
        }
        else if (role.equals("hr")) {
            if(!valid || !user.getRole().getName().equals("hr")) {
                throw new Exception("Invalid token");
            }
        }
        else if (!role.equals("admin") || !role.equals("hr")) {
            throw new Exception("Invalid token");
        }

    }

    public LoginDto login(UserLoginDTO loginRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        catch (Exception e) {
            throw e;
        }

        final CustomUserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        final String token = JwtUtil.generateToken(userDetails, SECRET_KEY, false);

        return new LoginDto(token);
    }

    public UserRegisterDTO postUser(User user) throws Exception {

        Role role = roleRepository.findById(user.getRole().getId()).get();

        if(role == null) {
            throw new Exception("Role with id " + user.getRole().getId().toString() + " does not exists");
        }

        User existingUser = userRepository.findByEmail(user.getEmail());
        if(existingUser != null) {
            throw new Exception("User with given email address already exists");
        }

        existingUser = userRepository.findByUsername(user.getUsername());
        if(existingUser != null) {
            throw new Exception("Chose different username");
        }

        user.setRole(role);
        user.setPassword(hashPassword(user.getPassword()));
        user.setDefaultPassword(true);

        User newUser =  userRepository.save(user);

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(
                newUser.getUsername(),
                newUser.getEmail(),
                new UserRoleDTO(
                        newUser.getRole().getId(),
                        newUser.getRole().getName()
                )
        );

        return userRegisterDTO;

    }

    public HashMap<String, String> deleteUserById(Integer id) throws Exception {
        if (!userRepository.existsById(id)) {
            throw new Exception("Obrisan user sa id-em "+id);
        }
        userRepository.deleteById(id);
        return new ResponseMessageDTO("Uspjesno obrisan korisnik sa id-em " + id).getHashMap();
    }

    public List<UserDto> getListOfUsers() throws Exception {
        if (userRepository.count() == 0) {
            throw new Exception("Nema korisnika u bazi");
        }
        List<User> sviKorisnici = new ArrayList<>();
        List<UserDto> novaLista = new ArrayList<>();
         userRepository.findAll().forEach(sviKorisnici::add);
        for(User user : sviKorisnici) {
           novaLista.add(new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getDefaultPassword()));
        }
        return novaLista;
    }

    public UserDto changePassword(ChangePasswordDto changePasswordDto) throws Exception {
        User user = userRepository.findByEmail(changePasswordDto.getEmail());

        if(user == null) {
            throw new NotFoundException("User does not exists");
        }

        if(!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new Exception("Incorect old password");
        }

        user.setPassword(hashPassword(changePasswordDto.getNewPassword()));
        user.setDefaultPassword(false);

        User updatedUser = userRepository.save(user);

        return new UserDto(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail(), updatedUser.getRole(), updatedUser.getDefaultPassword());
    }

    public UserDto getUserByEmail(String email, String token) throws Exception {
        User user = userRepository.findByEmail(email);

        token = token.split(" ")[1];

        if(!JwtUtil.extractUsername(token, SECRET_KEY).equals(user.getEmail())) {
            throw new Exception("Unauthorized");
        }

        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getDefaultPassword());
    }
}
