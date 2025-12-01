package com.test.service;

import com.test.model.Employee;
import java.util.List;

public interface SkillsRepository {
    boolean hasSkills(Employee employee, List<String> skills);
}