package cn.jasper.java.features.wordle.jdk17;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 通过
 * 组合思想、
 * stream流(8)、
 * switch表达式(14)、
 * record记录类(16)、
 * sealed密封类(17)、
 * switch模式匹配(preview)，
 * 来解决 Wordle Kata
 */
public record Wordle(String hiddenAsString) {

    public String guess(String guessAsString) {

        Guess guess = new Guess(guessAsString);
        Hidden hidden = new Hidden(hiddenAsString);

        return guess.checkAgainst(hidden)
                        .replaceEachLetterWith(
                                l -> switch (l) {
                                    case WELL_PLACED letter ->
                                            Character.toString(letter.codePoint).toUpperCase();
                                    case NOT_WELL_PLACED letter ->
                                            Character.toString(letter.codePoint);
                                    case ABSENT ignored ->
                                            ".";
                                }
                        );
    }

    record Guess(String guess) {

        public int length() {
            return this.guess.length();
        }

        public int codePointAt(int index) {
            return this.guess.codePointAt(index);
        }

        public GuessWithIndex checkCharacterAtPosition(int index) {
            return new GuessWithIndex(this, index);
        }

        public LetterStream checkAgainst(Hidden hidden) {
            return new LetterStream(
                    IntStream.range(0, length())
                            .mapToObj(index -> checkCharacterAtPosition(index).with(hidden)));
        }
    }

    record LetterStream(Stream<Letter> stream) {

        public String replaceEachLetterWith(Function<Letter, String> mapper) {
            return this.stream.map(mapper).collect(Collectors.joining());
        }
    }

    record GuessWithIndex(Guess guess, int index) {

        public boolean isWellPlacedIn(Hidden hidden) {
            return hidden.match(this, index, index);
        }

        public boolean isNotWellPlaceIn(Hidden hidden) {
            return IntStream.range(0, hidden.length())
//                    .filter(i -> index != i)
                    .filter(i -> !hidden.used[i])
                    .filter(i -> guess.codePointAt(i) != hidden.codePointAt(i))
                    .anyMatch(i -> hidden.match(this, index, i));
        }

        public Letter with(Hidden hidden) {
            if (isWellPlacedIn(hidden))
                return new WELL_PLACED(this.guess.codePointAt(index));
            if (isNotWellPlaceIn(hidden))
                return new NOT_WELL_PLACED(this.guess.codePointAt(index));
            return new ABSENT();
        }
    }

    record Hidden(String hidden, boolean[] used) {

        public Hidden(String hidden) {
            this(hidden, new boolean[hidden.length()]);
        }

        public int codePointAt(int index) {
            return this.hidden.codePointAt(index);
        }

        public int length() {
            return this.hidden.length();
        }

        private boolean match(GuessWithIndex guess, int indexGuess, int indexHidden) {
            boolean match = guess.guess().codePointAt(indexGuess) == hidden.codePointAt(indexHidden);
            if (match) this.used[indexHidden] = true;
            return match;
        }
    }

    sealed interface Letter permits WELL_PLACED, NOT_WELL_PLACED, ABSENT {}

    record WELL_PLACED(int codePoint) implements Letter {}
    record NOT_WELL_PLACED(int codePoint) implements Letter {}
    record ABSENT() implements Letter {}

}
