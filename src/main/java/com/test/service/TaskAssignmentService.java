package com.test.service;

import com.test.config.AppConfiguration;
import com.test.model.Employee;
import com.test.model.Task;
import java.util.List;
import java.util.Optional;

public class TaskAssignmentService {
    private final CalendarService calendarService;
    private final SkillsRepository skillsRepository;
    private final AllocationSystem allocationSystem;
    private final AppConfiguration configuration; // Dummy field

    public TaskAssignmentService(CalendarService calendarService,
                                 SkillsRepository skillsRepository,
                                 AllocationSystem allocationSystem,
                                 AppConfiguration configuration) {
        this.calendarService = calendarService;
        this.skillsRepository = skillsRepository;
        this.allocationSystem = allocationSystem;
        this.configuration = configuration;
    }

    public void assignTask(Task task) {
        // 1. Pobierz dostępnych pracowników
        List<Employee> availableEmployees = calendarService.getAvailableEmployees(task.duration());

        // 2. Znajdź kandydata z odpowiednimi umiejętnościami
        Optional<Employee> candidate = availableEmployees.stream()
                .filter(emp -> skillsRepository.hasSkills(emp, task.requiredSkills()))
                .findFirst();

        // 3. Jeśli znaleziono, przypisz zadanie
        candidate.ifPresent(employee -> allocationSystem.assign(employee, task));
    }
}