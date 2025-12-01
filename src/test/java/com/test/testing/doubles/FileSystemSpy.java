package com.test.testing.doubles;

import com.test.service.FileSystem;
import java.util.ArrayList;
import java.util.List;

public class FileSystemSpy implements FileSystem {
    public record FileOperation(String path, String content) {}

    private final List<FileOperation> operations = new ArrayList<>();

    @Override
    public void write(String path, String content) {
        operations.add(new FileOperation(path, content));
    }

    // Metody inspekcji
    public boolean wasWrittenTo(String path) {
        return operations.stream().anyMatch(op -> op.path().equals(path));
    }

    public String getContentOf(String path) {
        return operations.stream()
                .filter(op -> op.path().equals(path))
                .map(FileOperation::content)
                .findFirst()
                .orElse(null);
    }
}