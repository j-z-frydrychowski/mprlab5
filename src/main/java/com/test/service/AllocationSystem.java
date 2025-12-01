package com.test.service;

import com.test.model.Employee;
import com.test.model.Task;

public interface AllocationSystem {
    void assign(Employee employee, Task task);
}