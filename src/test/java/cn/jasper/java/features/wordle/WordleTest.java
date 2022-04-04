package cn.jasper.java.features.wordle;

import cn.jasper.java.features.wordle.eclipsecollections.Wordle;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class WordleTest {

    @ParameterizedTest
    @CsvFileSource(resources = {"/wordle.csv"})
    void wordle(String hidden, String guess, String expected) {
        Assertions.assertThat(new Wordle(hidden).guess(guess)).isEqualTo(expected);
    }
}