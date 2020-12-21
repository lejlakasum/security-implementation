package com.example.ppis.model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = "[\\w\\d]{3,}", message = "Username može sadržati najmanje 3 slova ili/i broja")
    private String username;

    //@Pattern(regexp = "[\\w\\d]{7,}", message = "Sifra mora imati minimalno 7 znakova (slova ili brojeva)")
    private String password;

    @Pattern(regexp = "^(.+)@(.+)$", message = "Email nije dobrog formata")
    private String email;

    @ManyToOne
    private Role role;

    public User() {}

    public User(@Pattern(regexp = "[\\w\\d]{3,}", message = "Username može sadržati najmanje 3 slova ili/i broja") String username, String password, @Pattern(regexp = "^(.+)@(.+)$", message = "Email nije dobrog formata") String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
