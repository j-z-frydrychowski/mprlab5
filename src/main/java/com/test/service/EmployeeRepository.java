package com.test.service;

import com.test.model.Employee;
import java.util.List;

public interface EmployeeRepository {
    void add(Employee employee);
    List<Employee> findAll();
}