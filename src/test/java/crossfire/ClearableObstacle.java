package crossfire;

import java.util.Collections;
import java.util.List;

public class ClearableObstacle implements Obstacle {
    int assignments = 0;
    int necessaryAssignemnts;
    boolean cleared = false;

    public ClearableObstacle(int necessaryAssignments) {
        this.necessaryAssignemnts = necessaryAssignments;
    }
    @Override
    public boolean cleared() {
        return cleared;
    }

    @Override
    public void assign(Damage damage) {
        assignments++;
    }

    @Override
    public void applyAssignedDamage() {
        if (assignments == necessaryAssignemnts) {
            cleared = true;
        }
        assignments = 0;
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
