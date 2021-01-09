package com.example.ppis.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 2,max = 15)
    private String firstName;

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 2,max = 15)
    private String lastName;

    @Past
    private Date birthDate;

    private Date dateOfEmployment;

    public Employee() {
    }

    public Employee(@NotBlank String firstName, @NotBlank String lastName, Date birthDate, Date dateOfEmployment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.dateOfEmployment = dateOfEmployment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getDateOfEmployment() {
        return dateOfEmployment;
    }

    public void setDateOfEmployment(Date dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
    }
}
