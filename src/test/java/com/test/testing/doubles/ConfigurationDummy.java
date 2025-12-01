package com.test.testing.doubles;

import com.test.config.AppConfiguration;

public class ConfigurationDummy extends AppConfiguration {
    @Override
    public String getDbUrl() {
        throw new UnsupportedOperationException("I am a Dummy! Do not call me!");
    }
}