package com.test.model;

import com.test.exception.InvalidDataException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectTeam {
    private final String name;
    private final int maxSize;
    private final List<Employee> members;

    public ProjectTeam(String name, int maxSize) {
        this.name = name;
        this.maxSize = maxSize;
        this.members = new ArrayList<>();
    }

    public void addMember(Employee employee) {
        if (members.size() >= maxSize) {
            throw new InvalidDataException("Team is full (max " + maxSize + ")");
        }
        if (employee.hasTeam()) {
            throw new InvalidDataException("Employee is already assigned to team " + employee.getCurrentTeamName());
        }

        members.add(employee);
        employee.setCurrentTeamName(this.name);
    }

    public void removeMember(Employee employee) {
        if (!members.contains(employee)) {
            throw new InvalidDataException("Employee is not in this team");
        }
        members.remove(employee);
        employee.setCurrentTeamName(null);
    }

    public void transferMember(Employee employee, ProjectTeam destinationTeam) {
        // Najpierw usuwamy z obecnego (zwalniamy blokadę 'hasTeam')
        this.removeMember(employee);

        try {
            // Próbujemy dodać do nowego
            destinationTeam.addMember(employee);
        } catch (Exception e) {
            // Rollback w przypadku błędu (np. nowy zespół pełny)
            // Przywracamy do starego zespołu (zakładamy, że się zmieści, bo przed chwilą tu był)
            this.addMember(employee);
            throw e;
        }
    }

    public boolean meetsDiversityRequirement(int minDifferentPositions) {
        long uniquePositionsCount = members.stream()
                .map(Employee::getJobTitle)
                .distinct()
                .count();

        return uniquePositionsCount >= minDifferentPositions;
    }

    public List<Employee> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public String getName() {
        return name;
    }
}