package com.test.service;

import com.test.model.Employee;
import java.time.Duration;
import java.util.List;

public interface CalendarService {
    List<Employee> getAvailableEmployees(Duration duration);
}