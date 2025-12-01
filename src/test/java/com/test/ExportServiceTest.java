package com.test;

import com.test.model.Employee;
import com.test.model.Position;
import com.test.service.ExportService;
import com.test.testing.doubles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ExportServiceTest {

    private EmployeeRepositoryFake repoFake;
    private DataFormatterStub formatterStub;

    @BeforeEach
    void setUp() {
        // Fake: Przygotowanie danych w "bazie"
        repoFake = new EmployeeRepositoryFake();
        repoFake.add(new Employee("Jan", "K", "j@t.pl", "C", Position.PROGRAMISTA));

        // Stub: Konfiguracja sztywnej odpowiedzi
        formatterStub = new DataFormatterStub().withReturnValue("Jan,Kowalski,PROGRAMISTA");
    }

    @Test
    void shouldExportDataUsingSpyVerification() {
        // GIVEN
        FileSystemSpy fileSystemSpy = new FileSystemSpy();
        ExportService service = new ExportService(repoFake, formatterStub, fileSystemSpy);
        String targetPath = "export.csv";

        // WHEN
        service.exportData(targetPath);

        // THEN (Spy: sprawdzamy co się nagrało)
        assertThat(fileSystemSpy.wasWrittenTo(targetPath)).isTrue();
        assertThat(fileSystemSpy.getContentOf(targetPath)).isEqualTo("Jan,Kowalski,PROGRAMISTA");
    }

    @Test
    void shouldFailIfWritingToWrongPathUsingMock() {
        // GIVEN
        String expectedPath = "report_2024.json";
        // Mock: Oczekuje konkretnej ścieżki
        FileSystemMock fileSystemMock = new FileSystemMock(expectedPath);

        ExportService service = new ExportService(repoFake, formatterStub, fileSystemMock);

        // WHEN & THEN (Mock: sam zgłasza błąd przy złym parametrze)
        // Próbujemy zapisać pod "wrong.txt", a Mock oczekuje "report_2024.json"
        assertThatThrownBy(() -> service.exportData("wrong.txt"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expected path 'report_2024.json' but got 'wrong.txt'");
    }

    @Test
    void shouldVerifyCallHappenedUsingMock() {
        // GIVEN
        String expectedPath = "data.csv";
        FileSystemMock fileSystemMock = new FileSystemMock(expectedPath);
        ExportService service = new ExportService(repoFake, formatterStub, fileSystemMock);

        // WHEN
        service.exportData(expectedPath);

        // THEN
        fileSystemMock.verify(); // Potwierdza, że zapis faktycznie nastąpił
    }
}