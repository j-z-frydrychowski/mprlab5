package com.test.model;

import java.time.Duration;
import java.util.List;

public record Task(String id, List<String> requiredSkills, Duration duration) {}