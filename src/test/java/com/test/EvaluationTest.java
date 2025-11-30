package com.test;

import com.test.model.Employee;
import com.test.model.Evaluation;
import com.test.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

public class EvaluationTest {

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee("Jan", "Test", "j@t.pl", "C", Position.PROGRAMISTA);
    }

    @Test
    void shouldAddEvaluationToHistory() {
        // WHEN
        employee.addEvaluation(2023, 5);
        employee.addEvaluation(2024, 4);

        // THEN - AssertJ weryfikacja złożonej struktury
        assertThat(employee.getEvaluations())
                .hasSize(2)
                .extracting(Evaluation::year, Evaluation::score)
                .containsExactly(
                        tuple(2023, 5),
                        tuple(2024, 4)
                );
    }

    @Test
    void shouldThrowExceptionForInvalidScore() {
        assertThatThrownBy(() -> employee.addEvaluation(2023, 6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("between 1 and 5");
    }

    // Źródło danych: Lista ocen (int), oczekiwana średnia (double)
    static Stream<Arguments> averageRatingProvider() {
        return Stream.of(
                Arguments.of(List.of(5, 5, 5), 5.0),       // Wszystkie takie same
                Arguments.of(List.of(3, 4, 5), 4.0),       // Rosnące
                Arguments.of(List.of(1, 5), 3.0),          // Skrajne
                Arguments.of(List.of(5), 5.0),             // Pojedyncza ocena
                Arguments.of(List.of(), 0.0)               // Brak ocen
        );
    }

    @ParameterizedTest(name = "Dla ocen {0} średnia powinna wynosić {1}")
    @MethodSource("averageRatingProvider")
    void shouldCalculateAverageRating(List<Integer> scores, double expectedAverage) {
        // GIVEN
        int startYear = 2020;
        for (int score : scores) {
            employee.addEvaluation(startYear++, score);
        }

        // WHEN
        double actualAverage = employee.getAverageRating();

        // THEN
        assertThat(actualAverage).isEqualTo(expectedAverage);
    }
}