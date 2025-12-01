package com.test.testing.doubles;

import com.test.model.Employee;
import com.test.service.SkillsRepository;
import java.util.*;

public class SkillsRepositoryFake implements SkillsRepository {
    // Mapa przechowująca umiejętności pracowników w pamięci
    private final Map<Employee, Set<String>> database = new HashMap<>();

    public void addSkill(Employee employee, String skill) {
        database.computeIfAbsent(employee, k -> new HashSet<>()).add(skill);
    }

    @Override
    public boolean hasSkills(Employee employee, List<String> skills) {
        Set<String> employeeSkills = database.getOrDefault(employee, Collections.emptySet());
        return employeeSkills.containsAll(skills);
    }
}