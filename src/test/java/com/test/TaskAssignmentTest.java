package com.test;

import com.test.config.AppConfiguration;
import com.test.model.Employee;
import com.test.model.Position;
import com.test.model.Task;
import com.test.service.TaskAssignmentService;
import com.test.testing.doubles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TaskAssignmentTest {

    private TaskAssignmentService service;
    private SkillsRepositoryFake skillsFake;
    private AllocationSystemSpy allocationSpy;
    private Employee skilledEmployee;
    private Employee unskilledEmployee;

    @BeforeEach
    void setUp() {
        // Dane testowe
        skilledEmployee = new Employee("Jan", "Expert", "j@t.pl", "C", Position.PROGRAMISTA);
        unskilledEmployee = new Employee("Adam", "Junior", "a@t.pl", "C", Position.STAZYSTA);

        // 1. Stub - Kalendarz zwraca obu pracowników jako dostępnych
        CalendarServiceStub calendarStub = new CalendarServiceStub(List.of(unskilledEmployee, skilledEmployee));

        // 2. Fake - Tylko Jan ma wymagane umiejętności
        skillsFake = new SkillsRepositoryFake();
        skillsFake.addSkill(skilledEmployee, "Java");
        skillsFake.addSkill(skilledEmployee, "TDD");

        // 3. Spy - Do weryfikacji przypisania
        allocationSpy = new AllocationSystemSpy();

        // 4. Dummy - Konfiguracja (ignorowana)
        AppConfiguration configDummy = new ConfigurationDummy();

        // Inicjalizacja serwisu (Wstrzykiwanie ręcznych atrap)
        service = new TaskAssignmentService(calendarStub, skillsFake, allocationSpy, configDummy);
    }

    @Test
    void shouldAssignFirstAvailableAndSkilledEmployee() {
        // GIVEN
        Task task = new Task("T-1", List.of("Java", "TDD"), Duration.ofHours(4));

        // WHEN
        service.assignTask(task);

        // THEN
        assertThat(allocationSpy.wasCalled()).isTrue();
        assertThat(allocationSpy.getAssignedEmployee()).isEqualTo(skilledEmployee);
        assertThat(allocationSpy.getAssignedTask()).isEqualTo(task);
    }

    @Test
    void shouldNotAssignIfSkillsMissing() {
        // GIVEN
        Task difficultTask = new Task("T-2", List.of("QuantumPhysics"), Duration.ofHours(1));

        // WHEN
        service.assignTask(difficultTask);

        // THEN
        assertThat(allocationSpy.wasCalled()).isFalse();
    }
}