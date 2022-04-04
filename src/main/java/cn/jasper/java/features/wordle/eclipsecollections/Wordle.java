package cn.jasper.java.features.wordle.eclipsecollections;

import org.eclipse.collections.api.bag.primitive.MutableCharBag;
import org.eclipse.collections.impl.factory.Strings;
import org.eclipse.collections.impl.factory.primitive.CharBags;
import org.eclipse.collections.impl.string.immutable.CharAdapter;

public record Wordle(String hidden) {

    public String guess(String guess) {
        CharAdapter guessChars = Strings.asChars(guess.toLowerCase());
        CharAdapter hiddenChars = Strings.asChars(this.hidden);
        MutableCharBag remaining = hiddenChars
                .injectIntoWithIndex(
                        CharBags.mutable.empty(),
                        (bag, each, i) ->
                                guessChars.get(i) != each ?
                                        bag.with(each) : bag);

        return guessChars.collectWithIndex(
                        (each, i) -> hiddenChars.get(i) == each ?
                                Character.toUpperCase(each) :
                                this.replaceDifferentPositionOrNotMatch(remaining, each))
                .makeString("");
    }

    private char replaceDifferentPositionOrNotMatch(MutableCharBag remaining, char each) {
        return remaining.remove(each) ? each : '.';
    }

}
