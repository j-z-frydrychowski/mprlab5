package com.test.testing.doubles;

import com.test.model.Employee;
import com.test.service.DataFormatter;
import java.util.List;

public class DataFormatterStub implements DataFormatter {
    private String returnValue = "";

    public DataFormatterStub withReturnValue(String returnValue) {
        this.returnValue = returnValue;
        return this;
    }

    @Override
    public String format(List<Employee> employees) {
        return returnValue;
    }
}