package com.example.myapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employee", indexes = {
        @Index(name = "idx_fullname_gender", columnList = "fullName, gender")
})
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;

    public Employee(String fullName, LocalDate dateOfBirth, String gender) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public int calculateAge() {
        return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
    }
}
