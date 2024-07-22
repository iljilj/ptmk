package com.example.myapp.service;


import com.example.myapp.annotation.MeasureExecutionTime;
import com.example.myapp.model.Employee;
import com.example.myapp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public void createEmployeeTable() {
        // тут пусто потому что spring.jpa.hibernate.ddl-auto=update
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        // так как надо выводить работников с уникальными ФИО + дата рождения,
        // то если у работников совпадают ФИО + дата, но при этом разный пол,
        // попадет только один из них
        List<Employee> allEmployees = employeeRepository.findAll();
        List<Employee> uniqueEmployees = allEmployees.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                employee -> employee.getFullName() + employee.getDateOfBirth(),
                                employee -> employee,
                                (existing, replacement) -> existing
                        ),
                        map -> map.values().stream()
                                .sorted(Comparator.comparing(Employee::getFullName))
                                .collect(Collectors.toList())
                ));

        return uniqueEmployees;
    }

    @MeasureExecutionTime
    public List<Employee> getEmployeesByCriteria() {
        return employeeRepository.findByGenderAndFullNameStartingWith("Male", "F");
    }

    public void fillEmployees(int numberOfEmployees) {
        final int batchSize = 10000;
        List<Employee> employees = new ArrayList<>(batchSize);
        Random random = new Random();

        for (int i = 0; i < numberOfEmployees; i++) {
            String gender = random.nextBoolean() ? "Male" : "Female";

            String lastName = lastNames.get(random.nextInt(lastNames.size()));
            String firstName = (gender.equals("Male"))
                    ? maleNames.get(random.nextInt(maleNames.size()))
                    : femaleNames.get(random.nextInt(femaleNames.size()));
            String fullName = lastName + " " + firstName;

            int startYear = 1960;
            int endYear = 2010;
            int year = startYear + random.nextInt(endYear - startYear + 1);
            int dayOfYear = random.nextInt(364) + 1;
            LocalDate dateOfBirth = LocalDate.ofYearDay(year, dayOfYear);

            employees.add(new Employee(fullName, dateOfBirth, gender));

            if (employees.size() == batchSize) {
                employeeRepository.saveAll(employees);
                employees.clear();
            }
        }

        if (!employees.isEmpty()) {
            employeeRepository.saveAll(employees);
        }
    }


    public void fillEmployeesByCriteria(int numberOfEmployees) {
        List<Employee> employees = new ArrayList<>();

        Random random = new Random();
        List<String> filteredLastNames = lastNames.stream()
                .filter(lastName -> lastName.startsWith("F"))
                .toList();


        for (int i = 0; i < numberOfEmployees; i++) {
            String lastName = filteredLastNames.get(random.nextInt(filteredLastNames.size()));
            String firstName = maleNames.get(random.nextInt(maleNames.size()));
            String fullName = lastName + " " + firstName;

            int startYear = 1960;
            int endYear = 2010;
            int year = startYear + random.nextInt(endYear - startYear + 1);
            int dayOfYear = random.nextInt(364) + 1;
            LocalDate dateOfBirth = LocalDate.ofYearDay(year, dayOfYear);

            employees.add(new Employee(fullName, dateOfBirth, "Male"));

        }
        employeeRepository.saveAll(employees);
    }


    // использую списки имен, потому что если делать это через специальные библиотеки,
    // генерирующие имена, будет сильный перекос в сторону определенных букв
    // например, B или H встречаются гораздо чаще, чем X или Q
    // а по заданию первые буквы имен должны быть равномерно распределены
    private static final List<String> lastNames = Arrays.asList(
            "Anderson", "Armstrong", "Adams", // A
            "Brown", "Baker", "Bryant",       // B
            "Clark", "Carter", "Collins",     // C
            "Davis", "Diaz", "Dixon",         // D
            "Evans", "Edwards", "Ellis",      // E
            "Foster", "Fisher", "Freeman",    // F
            "Garcia", "Gonzalez", "Griffin",  // G
            "Harris", "Hill", "Hughes",       // H
            "Ingram", "Irwin", "Ivy",         // I
            "Jackson", "Johnson", "Jones",    // J
            "King", "Knight", "Kelly",        // K
            "Lewis", "Lee", "Long",           // L
            "Moore", "Martinez", "Morris",    // M
            "Nelson", "Nguyen", "Nichols",    // N
            "Owens", "Ortiz", "Oliver",       // O
            "Parker", "Perez", "Peterson",    // P
            "Quinn", "Quinlan", "Quincy",     // Q
            "Robinson", "Rodriguez", "Reed",  // R
            "Smith", "Scott", "Sullivan",     // S
            "Taylor", "Turner", "Thomas",     // T
            "Upton", "Underwood", "Ulrich",   // U
            "Vasquez", "Vega", "Vaughn",      // V
            "Walker", "White", "Wright",      // W
            "Xavier", "Xander", "Xenon",      // X
            "Young", "Yates", "York",         // Y
            "Zimmerman", "Zane", "Zhao"       // Z
    );

    private final List<String> maleNames = Arrays.asList(
            "Alex", "John", "Michael", "David", "James", "Robert", "William", "Joseph", "Daniel", "Paul",
            "Mark", "George", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Jeffrey",
            "Ryan", "Jacob", "Gary", "Nicholas", "Eric", "Jonathan", "Frank", "Larry", "Scott", "Justin"
    );

    private final List<String> femaleNames = Arrays.asList(
            "Emma", "Olivia", "Ava", "Isabella", "Sophia", "Mia", "Charlotte", "Amelia", "Harper", "Evelyn",
            "Abigail", "Emily", "Ella", "Scarlett", "Grace", "Chloe", "Victoria", "Riley", "Aria", "Lily",
            "Avery", "Sofia", "Camila", "Aurora", "Zoey", "Natalie", "Luna", "Hannah", "Layla", "Mila"
    );

}
