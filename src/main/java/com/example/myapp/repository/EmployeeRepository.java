package com.example.myapp.repository;

import com.example.myapp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByGenderAndFullNameStartingWith(String gender, String surnameStart);
}
