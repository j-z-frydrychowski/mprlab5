package com.test.model;

import java.time.LocalDate;

public record Certificate(Employee owner, String type, LocalDate expiryDate) {
    public boolean isExpiringIn(LocalDate fromDate, int days) {
        LocalDate threshold = fromDate.plusDays(days);
        // Wygasa w przyszłości (po od dziś), ale przed progiem 30 dni
        return expiryDate.isAfter(fromDate) && expiryDate.isBefore(threshold) || expiryDate.equals(threshold);
    }
}