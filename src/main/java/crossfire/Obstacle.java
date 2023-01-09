package crossfire;

import java.util.List;

public interface Obstacle {
    boolean cleared();

    void assign(Damage damage);

    void applyAssignedDamage();

    List<Damage> levels();

    String name();

    int currentLevel();
}
