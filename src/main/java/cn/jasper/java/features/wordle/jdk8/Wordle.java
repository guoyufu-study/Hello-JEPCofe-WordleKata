package cn.jasper.java.features.wordle.jdk8;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record Wordle(String hiddenAsString) {
    // 组合思想、流

    public String guess(String guessAsString) {
        Guess guess = new Guess(guessAsString);
        Hidden hidden = new Hidden(hiddenAsString);
        return IntStream.range(0, guess.length())
                .mapToObj(
                        i -> {
                            if (guess.hasALetterAtPosition(i).thatIsWellPlaceOn(hidden))
                                return Character.toString(guess.codePointAt(i)).toUpperCase();
                            if (guess.hasALetterAtPosition(i).thatIsNotWellPlaceOn(hidden))
                                return Character.toString(guess.codePointAt(i));
                            return ".";
                        })
                .collect(Collectors.joining());
    }

    record Guess(String guess) {

        public int length() {
            return guess.length();
        }

        public int codePointAt(int index) {
            return guess.codePointAt(index);
        }

        public GuessWithIndex hasALetterAtPosition(int index) {
            return new GuessWithIndex(this, index);
        }
    }

    record GuessWithIndex(Guess guess, int index) {

        public boolean thatIsWellPlaceOn(Hidden hidden) {
            return hidden.match(guess, index, index);
        }

        public boolean thatIsNotWellPlaceOn(Hidden hidden) {
            return IntStream.range(0, hidden.length())
//                    .filter(i -> i != index)
                    .filter(i -> !hidden.used[i])
                    .filter(i -> guess.codePointAt(i) != hidden.codePointAt(i))
                    .anyMatch(i -> hidden.match(guess, i, index));
        }
    }

    record Hidden(String hidden, boolean[] used) {

        public Hidden(String hidden) {
            this(hidden, new boolean[hidden.length()]);
        }

        public int length() {
            return hidden.length();
        }

        public int codePointAt(int index) {
            return hidden.codePointAt(index);
        }

        public boolean match(Guess guess, int hiddenIndex, int guessIndex) {
            boolean match = guess.codePointAt(guessIndex) == hidden.codePointAt(hiddenIndex);
            if (match) used[hiddenIndex] = true;
            return match;
        }
    }
}
