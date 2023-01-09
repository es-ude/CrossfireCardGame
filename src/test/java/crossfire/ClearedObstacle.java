package crossfire;

import java.util.Collections;
import java.util.List;

class ClearedObstacle implements Obstacle {

    @Override
    public boolean cleared() {
        return true;
    }

    @Override
    public void assign(Damage damage) {

    }

    @Override
    public void applyAssignedDamage() {

    }

    @Override
    public List<Damage> levels() {
        return Collections.emptyList();
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public int currentLevel() {
        return 0;
    }
}
