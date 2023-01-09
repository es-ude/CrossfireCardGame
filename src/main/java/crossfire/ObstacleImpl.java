package crossfire;

import java.util.List;

public class ObstacleImpl implements Obstacle {

    private final List<Damage> levels;
    private final String name;
    private int currentLevel = 0;
    private Damage accumulator = Damage.NULL;

    public ObstacleImpl(List<Damage> levels, String name) {
        this.levels = List.copyOf(levels);
        this.name = name;
    }

    @Override
    public boolean cleared() {
        return currentLevel == levels.size();
    }

    @Override
    public void assign(Damage damage) {
        accumulator = accumulator.add(damage);
    }

    private Damage computeDamageRequiredToReachLevel(int level) {
        return levels.subList(currentLevel, level).stream().reduce(Damage.NULL, Damage::add);
    }

    private boolean canReachLevel(int level) {
        return accumulator.greaterOrEqual(computeDamageRequiredToReachLevel(level));
    }

    private int determineMaximumReachableLevel() {
        int reachableLevel = currentLevel;
        while (reachableLevel + 1 <= levels.size() && canReachLevel(reachableLevel + 1)) reachableLevel++;
        return reachableLevel;
    }

    @Override
    public void applyAssignedDamage() {
        currentLevel = determineMaximumReachableLevel();
        accumulator = Damage.NULL;
    }

    @Override
    public List<Damage> levels() {
        return levels;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append(name());
        builder.append(": ");
        for (int i = 0; i < levels().size(); i++) {
            builder.append("\n\t");
            if (i == currentLevel) builder.append("\t>");
            builder.append(levels.get(i));
        }
        return builder.toString();
    }

    @Override
    public int currentLevel() {
        return currentLevel;
    }
}
