package com.test.service;

import com.test.model.Employee;
import java.util.List;

public interface DataFormatter {
    String format(List<Employee> employees);
}