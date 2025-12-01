package com.test.service;

import com.test.model.Employee;
import java.util.List;

public class ExportService {
    private final EmployeeRepository employeeRepository;
    private final DataFormatter dataFormatter;
    private final FileSystem fileSystem;

    public ExportService(EmployeeRepository employeeRepository,
                         DataFormatter dataFormatter,
                         FileSystem fileSystem) {
        this.employeeRepository = employeeRepository;
        this.dataFormatter = dataFormatter;
        this.fileSystem = fileSystem;
    }

    public void exportData(String destinationPath) {
        // 1. Pobierz dane (z Fake Repository)
        List<Employee> employees = employeeRepository.findAll();

        // 2. Sformatuj dane (przez Stub Formatter)
        String formattedData = dataFormatter.format(employees);

        // 3. Zapisz wynik (monitorowane przez Spy lub Mock FileSystem)
        fileSystem.write(destinationPath, formattedData);
    }
}