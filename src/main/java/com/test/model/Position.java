package com.test.model;

public enum Position {
    STAZYSTA("Stażysta", 3000, 5000, 1),
    PROGRAMISTA("Programista", 8000, 15000, 2),
    MANAGER("Manager", 12000, 20000, 3),
    WICEPREZES("Wiceprezes", 18000, 30000, 4),
    PREZES("Prezes", 25000, 50000, 5);

    private final String name;
    private final double baseSalary;
    private final double maxSalary;
    private final int hierarchyLevel;

    Position(String name, double baseSalary, double maxSalary, int hierarchyLevel) {
        this.name = name;
        this.baseSalary = baseSalary;
        this.maxSalary = maxSalary;
        this.hierarchyLevel = hierarchyLevel;
    }

    public String getName() { return name; }
    public double getSalary() { return baseSalary; }
    public double getMaxSalary() { return maxSalary; }
    public int getHierarchyLevel() { return hierarchyLevel; }

    public static Position getPosition(String name){
        return Position.valueOf(name.toUpperCase());
    }
}