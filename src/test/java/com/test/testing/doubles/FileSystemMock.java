package com.test.testing.doubles;

import com.test.service.FileSystem;

public class FileSystemMock implements FileSystem {
    private final String expectedPath;
    private boolean wasCalled = false;

    public FileSystemMock(String expectedPath) {
        this.expectedPath = expectedPath;
    }

    @Override
    public void write(String path, String content) {
        if (!path.equals(expectedPath)) {
            throw new AssertionError(
                    String.format("FileSystemMock Failure: Expected path '%s' but got '%s'", expectedPath, path)
            );
        }
        wasCalled = true;
    }

    public void verify() {
        if (!wasCalled) {
            throw new AssertionError("FileSystemMock Failure: Expected write() to be called, but it was not.");
        }
    }
}