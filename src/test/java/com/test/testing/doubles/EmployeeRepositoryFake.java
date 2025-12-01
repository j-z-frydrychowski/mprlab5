package com.test.testing.doubles;

import com.test.model.Employee;
import com.test.service.EmployeeRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmployeeRepositoryFake implements EmployeeRepository {
    private final List<Employee> db = new ArrayList<>();

    @Override
    public void add(Employee employee) {
        db.add(employee);
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(db); // Zwracamy kopię, by chronić oryginał
    }
}