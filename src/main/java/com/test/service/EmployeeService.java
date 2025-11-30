package com.test.service;

import com.test.exception.InvalidDataException;
import com.test.model.CompanyStatistics;
import com.test.model.Employee;
import com.test.model.Employee;
import com.test.model.Position;

import javax.naming.directory.AttributeInUseException;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeService {
    private final ArrayList<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void ShowEmployees(){
        System.out.println("Employees: ");
        for (Employee employee : employees) {
            System.out.println("---------------------");
            System.out.println(employee.toString());
            System.out.println("---------------------");
        }
    }

    public Employee SearchEmployee(String email){
        for (Employee employee : employees) {
            if (employee.getEmail().equals(email)) {
                return employee;
            }
        }
        return null;
    }

    public List<Employee> SearchEmployeeByCompany(String companyName){
        return employees.stream()
                .filter(e -> e.getCompanyName().equalsIgnoreCase(companyName))
                .collect(Collectors.toList());
    }

    public List<Employee> validateSalaryConsistency() {
        List<Employee> inconsistentSalaryEmployees = new ArrayList<>();

        for (Employee employee : employees) {
            double baseSalary = employee.getJobTitle().getSalary();

            if (employee.getSalary() < baseSalary) {
                inconsistentSalaryEmployees.add(employee);
            }
        }

        return inconsistentSalaryEmployees;
    }

    public Map<String, CompanyStatistics> getCompanyStatistics() {
        Map<String, List<Employee>> employeesByCompany = new HashMap<>();
        for (Employee employee : employees) {
            String companyName = employee.getCompanyName();
            if (!employeesByCompany.containsKey(companyName)) {
                employeesByCompany.put(companyName, new ArrayList<>());
            }
            employeesByCompany.get(companyName).add(employee);
        }

        Map<String, CompanyStatistics> statisticsMap = new HashMap<>();
        for (Map.Entry<String, List<Employee>> entry : employeesByCompany.entrySet()) {
            List<Employee> companyEmployees = entry.getValue();

            int employeeCount = companyEmployees.size();
            double totalSalary = 0;
            Employee highestPaidEmployee = null;

            for (Employee employee : companyEmployees) {
                totalSalary += employee.getSalary();

                if (highestPaidEmployee == null || employee.getSalary() > highestPaidEmployee.getSalary()) {
                    highestPaidEmployee = employee;
                }
            }

            double averageSalary = employeeCount > 0 ? totalSalary / employeeCount : 0;
            String highestPaidName = highestPaidEmployee != null ?
                    highestPaidEmployee.getName() + " " + highestPaidEmployee.getSurname() : "N/A";

            CompanyStatistics stats = new CompanyStatistics(employeeCount, averageSalary, highestPaidName);
            statisticsMap.put(entry.getKey(), stats);
        }

        return statisticsMap;
    }
    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void promoteEmployee(Employee employee, Position newPosition) {
        if (newPosition.getHierarchyLevel() <= employee.getJobTitle().getHierarchyLevel()) {
            throw new InvalidDataException("Invalid promotion path: Cannot promote from "
                    + employee.getJobTitle() + " to " + newPosition);
        }

        employee.setJobTitle(newPosition);
        // Automatyczne dostosowanie wynagrodzenia do bazy nowego stanowiska
        employee.setSalary(newPosition.getSalary());
    }

    public void giveRaise(Employee employee, double percentage) {
        if (percentage <= 0) {
            throw new InvalidDataException("Raise percentage must be positive");
        }

        double currentSalary = employee.getSalary();
        double raiseAmount = currentSalary * (percentage / 100.0);
        double newSalary = currentSalary + raiseAmount;
        double maxSalaryForPosition = employee.getJobTitle().getMaxSalary();

        if (newSalary > maxSalaryForPosition) {
            newSalary = maxSalaryForPosition;
        }

        employee.setSalary(newSalary);
    }
}
