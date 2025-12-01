package com.test.testing.doubles;

import com.test.model.Employee;
import com.test.model.Task;
import com.test.service.AllocationSystem;
import java.util.ArrayList;
import java.util.List;

public class AllocationSystemSpy implements AllocationSystem {
    private final List<AssignmentLog> logs = new ArrayList<>();

    public record AssignmentLog(Employee employee, Task task) {}

    @Override
    public void assign(Employee employee, Task task) {
        logs.add(new AssignmentLog(employee, task));
    }

    // Metody inspekcji
    public boolean wasCalled() {
        return !logs.isEmpty();
    }

    public Employee getAssignedEmployee() {
        return logs.isEmpty() ? null : logs.get(0).employee();
    }

    public Task getAssignedTask() {
        return logs.isEmpty() ? null : logs.get(0).task();
    }
}