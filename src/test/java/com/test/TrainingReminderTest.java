package com.test;

import com.test.model.Certificate;
import com.test.model.Employee;
import com.test.model.Position;
import com.test.service.TrainingReminderService;
import com.test.testing.doubles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TrainingReminderTest {

    private Employee emp1, emp2, emp3;
    private LocalDate today;
    private LoggerDummy loggerDummy;

    @BeforeEach
    void setUp() {
        today = LocalDate.of(2024, 1, 1);
        emp1 = new Employee("Jan", "K", "jan@t.pl", "C", Position.PROGRAMISTA);
        emp2 = new Employee("Anna", "N", "anna@t.pl", "C", Position.MANAGER);
        emp3 = new Employee("Tom", "B", "tom@t.pl", "C", Position.STAZYSTA);

        // Dummy jest wspólny - zawsze ignorowany
        loggerDummy = new LoggerDummy();
    }

    @Test
    void shouldSendRemindersOnlyForExpiringCertificates_UsingSpy() {
        // GIVEN
        // 1. Wygasa za 10 dni (Powinno wysłać)
        Certificate certExpiring = new Certificate(emp1, "BHP", today.plusDays(10));
        // 2. Wygasł wczoraj (Już za późno na przypomnienie "nadchodzące", albo inna logika - tu zakładamy że system szuka nadchodzących 30 dni)
        Certificate certExpired = new Certificate(emp2, "RODO", today.minusDays(1));
        // 3. Ważny jeszcze rok (Nie wysyłać)
        Certificate certValid = new Certificate(emp3, "BHP", today.plusDays(365));

        CertificateRepositoryStub repoStub = new CertificateRepositoryStub(List.of(certExpiring, certExpired, certValid));
        CommunicationServiceSpy commSpy = new CommunicationServiceSpy();

        TrainingReminderService service = new TrainingReminderService(repoStub, commSpy, loggerDummy);

        // WHEN
        service.processReminders(today);

        // THEN (Asercja na stanie Szpiega)
        assertThat(commSpy.sentCount()).isEqualTo(1);
        assertThat(commSpy.getMessage(0).email()).isEqualTo("jan@t.pl");
        assertThat(commSpy.getMessage(0).content()).contains("BHP", "10 dni");
    }

    @Test
    void shouldSendNoRemindersIfAllCertificatesValid_UsingMock() {
        // GIVEN
        Certificate certValid1 = new Certificate(emp1, "BHP", today.plusDays(100));
        Certificate certValid2 = new Certificate(emp2, "RODO", today.plusDays(40));

        CertificateRepositoryStub repoStub = new CertificateRepositoryStub(List.of(certValid1, certValid2));

        // MOCK: Oczekujemy dokładnie 0 wywołań
        CommunicationServiceMock commMock = new CommunicationServiceMock(0);

        TrainingReminderService service = new TrainingReminderService(repoStub, commMock, loggerDummy);

        // WHEN
        service.processReminders(today);

        // THEN (Weryfikacja behawioralna Mocka)
        commMock.verify();
    }
}