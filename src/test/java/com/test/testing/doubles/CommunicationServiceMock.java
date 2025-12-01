package com.test.testing.doubles;

import com.test.service.CommunicationService;

public class CommunicationServiceMock implements CommunicationService {
    private final int expectedCalls;
    private int actualCalls = 0;

    public CommunicationServiceMock(int expectedCalls) {
        this.expectedCalls = expectedCalls;
    }

    @Override
    public void sendReminder(String email, String content) {
        actualCalls++;
        if (actualCalls > expectedCalls) {
            throw new AssertionError("Mock expectation failed: Expected " + expectedCalls + " calls, but got more.");
        }
    }

    // Metoda weryfikująca wywoływana na końcu testu
    public void verify() {
        if (actualCalls != expectedCalls) {
            throw new AssertionError("Mock expectation failed: Expected " + expectedCalls + " calls, but got " + actualCalls);
        }
    }
}