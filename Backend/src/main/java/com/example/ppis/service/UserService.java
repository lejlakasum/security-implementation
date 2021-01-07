package com.example.ppis.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        user.setRole(role);
        user.setPassword(hashPassword(user.getPassword()));

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
           novaLista.add(new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole()));
        }
        return novaLista;
    }
}
