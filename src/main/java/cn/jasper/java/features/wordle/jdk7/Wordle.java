package cn.jasper.java.features.wordle.jdk7;

public record Wordle(String hidden) {

    public String guess(String guess) {

        String result = "";
        boolean[] used = new boolean[hidden.length()];
        for (int i = 0; i < guess.length(); i++) {
            if (guess.codePointAt(i) == this.hidden.codePointAt(i)) {
                result += Character.toString(guess.codePointAt(i)).toUpperCase();
                used[i] = true;
            } else {
                boolean found = false;
                for (int j = 0; j < hidden.length(); j++) {
                    if (used[j]) continue;
                    if (i != j // 肯定不会相同，可省略
                            && this.hidden.codePointAt(j) != guess.codePointAt(j)
                            && guess.codePointAt(i) == hidden.codePointAt(j)) {
                        result += Character.toString(guess.codePointAt(i));
                        found = true;
                        used[j] = true;
                        break;
                    }
                }
                if (!found) result += ".";
            }
        }
        return result;
    }
}
