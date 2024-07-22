package com.example.myapp;

import com.example.myapp.model.Employee;
import com.example.myapp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class MyApp implements CommandLineRunner {

    @Autowired
    private EmployeeService employeeService;

    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);
    }

    @Override
    public void run(String... args) {
        if (args.length == 0) {
            return;
        }

        switch (args[0]) {
            case "1":
                employeeService.createEmployeeTable();
                break;
            case "2":
                Employee employee = new Employee();
                employee.setFullName(args[1]);
                employee.setDateOfBirth(LocalDate.parse(args[2]));
                employee.setGender(args[3]);
                employeeService.saveEmployee(employee);
                break;
            case "3":
                employeeService.getAllEmployees().forEach(emp -> {
                    System.out.printf("%s, %s. %s (age %d)%n",
                            emp.getFullName(),
                            emp.getGender(),
                            emp.getDateOfBirth(),
                            emp.calculateAge());
                });
                break;
            case "4":
                employeeService.fillEmployees(1000000);
                employeeService.fillEmployeesByCriteria(100);
                break;
            case "5":
                List<Employee> employees = employeeService.getEmployeesByCriteria();
                System.out.printf("%d rows%n", employees.size());
                employees.stream().limit(10).forEach(emp -> {
                    System.out.printf("%s, %s. %s (age %d)%n",
                            emp.getFullName(),
                            emp.getGender(),
                            emp.getDateOfBirth(),
                            emp.calculateAge());
                });
                System.out.println("...");
                System.out.println("Only first 10 are printed"); // а то время выполнения отлетает наверх
                break;
            default:
                System.out.println("Invalid mode.");
        }
    }

}


