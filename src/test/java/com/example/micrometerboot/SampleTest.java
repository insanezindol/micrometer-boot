package com.example.micrometerboot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class SampleTest {

    @Test
    @DisplayName("JUnit 기본 문법")
    void junitTestSample() {
        assertEquals("text", "text");
    }

    @Test
    @DisplayName("Assertj 기본 문법")
    void assertjTestSample() {
        assertThat("text").isEqualTo("text");
    }

}
