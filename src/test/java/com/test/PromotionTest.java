package com.test;

import com.test.exception.InvalidDataException;
import com.test.model.Employee;
import com.test.model.Position;
import com.test.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PromotionTest {

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService();
    }

    static Stream<Arguments> validPromotionProvider() {
        return Stream.of(
                Arguments.of(Position.STAZYSTA, Position.PROGRAMISTA),
                Arguments.of(Position.PROGRAMISTA, Position.MANAGER),
                Arguments.of(Position.MANAGER, Position.WICEPREZES)
        );
    }

    @ParameterizedTest(name = "Awans z {0} na {1} powinien zaktualizować stanowisko i pensję")
    @MethodSource("validPromotionProvider")
    void shouldPromoteEmployeeValidPath(Position startPosition, Position targetPosition) {
        // GIVEN
        Employee employee = new Employee("Jan", "Test", "jan@test.pl", "Corp", startPosition);
        employeeService.addEmployee(employee);

        // WHEN
        employeeService.promoteEmployee(employee, targetPosition);

        // THEN
        // AssertJ - weryfikacja stanu obiektu
        assertThat(employee)
                .extracting(Employee::getJobTitle, Employee::getSalary)
                .containsExactly(targetPosition, targetPosition.getSalary());

        // Hamcrest - dodatkowe asercje czytelnościowe
        assertThat(employee.getJobTitle(), equalTo(targetPosition));
        assertThat(employee.getSalary(), greaterThan(startPosition.getSalary()));
        assertThat(employee.getSalary(), closeTo(targetPosition.getSalary(), 0.01));
    }

    @ParameterizedTest(name = "Próba awansu ze {0} na {1} powinna rzucić wyjątek (Błąd hierarchii)")
    @CsvSource({
            "MANAGER, PROGRAMISTA",
            "PREZES, STAZYSTA",
            "PROGRAMISTA, PROGRAMISTA"
    })
    void shouldThrowExceptionForInvalidHierarchy(Position startPosition, Position targetPosition) {
        // GIVEN
        Employee employee = new Employee("Jan", "Test", "jan@test.pl", "Corp", startPosition);

        // WHEN & THEN - AssertJ Fluent API
        assertThatThrownBy(() -> employeeService.promoteEmployee(employee, targetPosition))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("Invalid promotion path");
    }

    @ParameterizedTest(name = "Podwyżka o {1}% dla {0} powinna ustawić pensję na {2}")
    @CsvSource({
            "STAZYSTA, 10.0, 3300.0",   // 3000 + 10% = 3300
            "PROGRAMISTA, 20.0, 9600.0" // 8000 + 20% = 9600
    })
    void shouldGivePercentageRaise(Position position, double percentage, double expectedSalary) {
        // GIVEN
        Employee employee = new Employee("Anna", "Nowak", "anna@test.pl", "Corp", position);
        employeeService.addEmployee(employee);

        // WHEN
        employeeService.giveRaise(employee, percentage);

        // THEN
        assertThat(employee.getSalary()).isEqualTo(expectedSalary);

        // Hamcrest
        assertThat(employee.getSalary(), is(expectedSalary));
    }

    @ParameterizedTest(name = "Podwyżka dla {0} przekraczająca max ({1}%) powinna zostać przycięta do {2}")
    @CsvSource({
            "STAZYSTA, 100.0, 5000.0", // 3000 + 100% = 6000 -> Limit 5000
            "PROGRAMISTA, 200.0, 15000.0" // Limit 15000
    })
    void shouldCapSalaryAtMaxAllowed(Position position, double percentage, double expectedMax) {
        // GIVEN
        Employee employee = new Employee("Krzysztof", "Klucz", "kris@test.pl", "Corp", position);

        // WHEN
        employeeService.giveRaise(employee, percentage);

        // THEN
        // AssertJ z użyciem satisfies dla złożonej asercji
        assertThat(employee).satisfies(e -> {
            assertThat(e.getSalary()).isEqualTo(expectedMax);
            assertThat(e.getSalary()).isLessThanOrEqualTo(position.getMaxSalary());
        });
    }

    @ParameterizedTest(name = "Ujemna podwyżka {0}% powinna rzucić wyjątek")
    @CsvSource({"-5.0", "-0.1"})
    void shouldThrowExceptionForNegativeRaise(double percentage) {
        Employee employee = new Employee("X", "Y", "z@z.pl", "C", Position.PROGRAMISTA);

        assertThatThrownBy(() -> employeeService.giveRaise(employee, percentage))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("Raise percentage must be positive");
    }
}