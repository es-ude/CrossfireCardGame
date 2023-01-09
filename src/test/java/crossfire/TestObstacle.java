package crossfire;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestObstacle {
    private ObstacleImpl createUnnamedObstacle(List<Damage> d) {
        return new ObstacleImpl(d, "");
    }

    @Test
    public void canCreateObstacle() {
        var damage = new ArrayList<Damage>();
        damage.add(Damage.NULL);
        new ObstacleImpl(damage, "");
    }

    @Test
    public void obstacleIsUnclearedWithoutApplyingDamage() {
        var damage = List.of(Damage.BLACK);
        var target = createUnnamedObstacle(damage);
        assertFalse(target.cleared());
    }

    @Test
    public void obstacleWithoutDamageIsCleared() {
        var target = createUnnamedObstacle(List.of());
        assertTrue(target.cleared());
    }

    @Test
    public void obstacleWithUnclearedDamageIsUncleared() {
        var target = createUnnamedObstacle(List.of(Damage.GREEN));
        assertFalse(target.cleared());
    }

    @Test
    public void applyingSingleDamagePointClearsObstacle() {
        var target = createUnnamedObstacle(List.of(Damage.BLACK));
        target.assign(Damage.BLACK);
        target.applyAssignedDamage();
        assertTrue(target.cleared());
    }

    @Test
    public void applyingWrongDamagePointDoesNotClearObstacle() {
        var target = createUnnamedObstacle(List.of(Damage.BLACK));
        target.assign(Damage.GREEN);
        target.applyAssignedDamage();
        assertFalse(target.cleared());
    }

    @Test
    public void applyingCorrectDamageTwiceClearsObstacle() {
        var target = createUnnamedObstacle(List.of(Damage.BLACK, Damage.GREEN));
        target.assign(Damage.BLACK);
        target.assign(Damage.GREEN);
        target.applyAssignedDamage();
        assertTrue(target.cleared());
    }

    @Test
    public void applyPartialDamageOnceDoesNotClearObstacle() {
        var target = createUnnamedObstacle(List.of(Damage.BLACK.times(2)));
        target.assign(Damage.BLACK);
        target.applyAssignedDamage();
        assertFalse(target.cleared());
    }

    @Test
    public void applyPartialDamageTwiceClearsObstacle() {
        var target = createUnnamedObstacle(List.of(Damage.BLACK.times(2)));
        target.assign(Damage.BLACK);
        target.assign(Damage.BLACK);
        target.applyAssignedDamage();
        assertTrue(target.cleared());
    }

    @Test
    public void applyPartialInTwoPassesDoesNotClear() {
        var target = createUnnamedObstacle(List.of(Damage.BLUE, Damage.BLACK.times(3)));
        target.assign(Damage.BLACK);
        target.assign(Damage.BLUE);
        target.applyAssignedDamage();
        target.assign(Damage.BLACK.times(2));
        target.applyAssignedDamage();
        assertFalse(target.cleared());
    }

    @Test
    public void applyDamageInTwoPassesForTwoLevelsClears() {
        var target = createUnnamedObstacle(List.of(Damage.BLACK, Damage.RED));
        target.assign(Damage.BLACK);
        target.applyAssignedDamage();
        target.assign(Damage.RED);
        target.applyAssignedDamage();
        assertTrue(target.cleared());
    }

    @Test
    public void getLevelWithoutClearedOnes() {
        var target = createUnnamedObstacle(List.of(Damage.BLACK, Damage.RED));
        target.assign(Damage.BLACK);
        assertEquals(0, target.currentLevel());
        target.applyAssignedDamage();
        assertEquals(1, target.currentLevel());
    }

    @Test
    public void coloredDamageClearsGray() {
        var target = createUnnamedObstacle(List.of(Damage.GRAY));
        target.assign(Damage.GREEN);
        target.applyAssignedDamage();
        assertTrue(target.cleared());
    }

    @Test
    public void useTheColorsWeDoNotNeedForLaterLevelsToClearGray() {
        var target = createUnnamedObstacle(List.of(Damage.GRAY.times(3), Damage.RED));
        target.assign(Damage.ALL_COLORS);
        target.applyAssignedDamage();
        assertTrue(target.cleared());
    }

    @Test
    public void useUpRedColorForGrayLevel() {
        var target = createUnnamedObstacle(List.of(Damage.GRAY.times(5), Damage.RED));
        target.assign(Damage.ALL_COLORS);
        target.applyAssignedDamage();
        assertFalse(target.cleared());
    }

}
