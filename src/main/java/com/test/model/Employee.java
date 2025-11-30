package com.test.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Employee {
    private String name;
    private String surname;
    private String email;
    private String companyName;
    private Position position;
    private double salary;
    private LocalDate hireDate;
    private List<Evaluation> evaluations = new ArrayList<>();
    private String currentTeamName;

    public Employee(){
        this.companyName = "TechCorp";
    }

    public Employee(String name, String surname, String email, String companyName, Position position) {
        this.name = validate(name);
        this.surname = validate(surname);
        this.email = validate(email);
        this.companyName = validate(companyName);
        this.position = validate(position);
        this.salary = position.getSalary();
        this.hireDate = LocalDate.now();
    }

    public Employee(String name, String surname, String email, String companyName, Position position, LocalDate hireDate) {
        this.name = validate(name);
        this.surname = validate(surname);
        this.email = validate(email);
        this.companyName = validate(companyName);
        this.position = validate(position);
        this.salary = position.getSalary();
        this.hireDate = validate(hireDate);

        // Walidacja daty z przyszłości
        if (this.hireDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Hire date cannot be in the future");
        }
    }

    public List<Evaluation> getEvaluations() {
        return Collections.unmodifiableList(evaluations);
    }

    public void addEvaluation(int year, int score) {
        this.evaluations.add(new Evaluation(year, score));
    }

    public double getAverageRating() {
        if (evaluations.isEmpty()) {
            return 0.0;
        }
        return evaluations.stream()
                .mapToInt(Evaluation::score)
                .average()
                .orElse(0.0);
    }

    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getEmail() {
        return email;
    }
    public String getCompanyName() {
        return companyName;
    }
    public Position getJobTitle() {
        return position;
    }
    public double getSalary() {
        return salary;
    }
    public LocalDate getHireDate() {
        return hireDate;
    }
    public int getSeniorityInYears(LocalDate referenceDate) {
        if (referenceDate.isBefore(hireDate)) {
            throw new IllegalArgumentException("Reference date cannot be before hire date");
        }
        return Period.between(hireDate, referenceDate).getYears();
    }
    public String getCurrentTeamName() {
        return currentTeamName;
    }

    public void setName(String name) {
        this.name = validate(name);
    }
    public void setSurname(String surname) {
        this.surname = validate(surname);
    }
    public void setEmail(String email) {
        this.email = validate(email);
    }
    public void setCompanyName(String companyName) {
        this.companyName = validate(companyName);
    }
    public void setJobTitle(Position position) {
        this.position = validate(position);
    }
    public void setSalary(double salary) {
        if(salary <= 0){
            throw new IllegalArgumentException("Salary cannot be negative or zero");
        }
        else this.salary = salary;
    }
    public void setCurrentTeamName(String teamName) {
        this.currentTeamName = teamName;
    }

    public boolean hasTeam() {
        return currentTeamName != null && !currentTeamName.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Employee e = (Employee) o;
        return email.equals(e.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        return "Imie: " + name +
                "\nSurname: " + surname +
                "\nEmail: " + email +
                "\nCompany: " + companyName +
                "\nJob title: " + position.getName() +
                "\nSalary: " + salary + "\n";
    }

    private static <T> T validate(T o){
        if(o == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        else return o;
    }
}