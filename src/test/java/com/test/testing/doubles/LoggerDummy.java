package com.test.testing.doubles;

import com.test.service.Logger;

public class LoggerDummy implements Logger {
    @Override
    public void log(String message) {
        throw new UnsupportedOperationException("LoggerDummy should not be called in this test scenario!");
    }
}