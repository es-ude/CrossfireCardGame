package crossfire;

import java.util.Arrays;

public record Damage(int red, int green, int blue, int black, int neutral) {
    public static final Damage NULL = new Damage(0, 0, 0, 0, 0);
    public static final Damage BLUE = new Damage(0, 0, 1, 0, 0);
    public static final Damage RED = new Damage(1, 0, 0, 0, 0);
    public static final Damage GREEN = new Damage(0, 1, 0, 0, 0);
    public static final Damage BLACK = new Damage(0, 0, 0, 1, 0);
    public static final Damage GRAY = new Damage(0, 0, 0, 0, 1);

    public static final Damage ALL_COLORS = new Damage(1, 1, 1, 1, 1);

    public Damage times(int i) {
        return new Damage(i * this.red, i * this.green, i * this.blue, i * this.black, i * this.neutral);
    }

    public Damage add(Damage other) {
        return new Damage(red + other.red, green + other.green, blue + other.blue, black + other.black, neutral + other.neutral);
    }

    public boolean greaterOrEqual(Damage other) {
        int[] diffs = {0, 0, 0, 0, 0};
        diffs[0] = green - other.green;
        diffs[1] = black - other.black;
        diffs[2] = blue - other.blue;
        diffs[3] = red - other.red;
        diffs[4] = neutral - other.neutral;
        var all_colored_are_greater = Arrays.stream(Arrays.copyOfRange(diffs, 0, 3)).allMatch(n -> n >= 0);
        if (all_colored_are_greater) {
            var remaining_value_for_neutral = Arrays.stream(diffs).sum();
            return remaining_value_for_neutral >= 0;
        }
        return false;
    }
}
