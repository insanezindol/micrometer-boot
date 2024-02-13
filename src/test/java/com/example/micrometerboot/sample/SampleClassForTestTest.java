package com.example.micrometerboot.sample;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@Slf4j
class SampleClassForTestTest {

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

    @Test
    @DisplayName("add 계산 성공 테스트")
    void add() {
        // given
        final String a = "10";
        final String b = "20";
        final int c = 30;

        // when
        SampleClassForTest sampleClassForTest = new SampleClassForTest();
        int result = sampleClassForTest.add(a, b);

        // then
        assertThat(result).isEqualTo(c);
    }

    @Test
    @DisplayName("add 계산 실패 테스트")
    void addWithException() {
        // given
        final String a = "TEST";
        final String b = "20";

        // when, then
        SampleClassForTest sampleClassForTest = new SampleClassForTest();
        assertThrows(NumberFormatException.class, () -> {
            sampleClassForTest.add(a, b);
        });
    }

}
