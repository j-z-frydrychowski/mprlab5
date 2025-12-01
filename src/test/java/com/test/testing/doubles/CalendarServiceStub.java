package com.test.testing.doubles;

import com.test.model.Employee;
import com.test.service.CalendarService;
import java.time.Duration;
import java.util.List;

public class CalendarServiceStub implements CalendarService {
    private final List<Employee> availableEmployees;

    public CalendarServiceStub(List<Employee> availableEmployees) {
        this.availableEmployees = availableEmployees;
    }

    @Override
    public List<Employee> getAvailableEmployees(Duration duration) {
        return availableEmployees;
    }
}