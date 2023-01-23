package crossfire.actions;

import crossfire.Card;
import crossfire.GameImpl;
import crossfire.Obstacle;

public class AssignCard implements Action{
    private final Obstacle obstacle;
    private final Card card;
    private final GameImpl game;

    public AssignCard(GameImpl game, Obstacle obstacle, Card card) {
        this.obstacle = obstacle;
        this.card = card;
        this.game = game;
    }

    public String describe() {
        return "Assign damage from " + this.card.toString() + " to " + this.obstacle.toString();
    }

    public void take() {
        this.game.assign(this.card, this.obstacle);
    }

    public boolean equals(Object other) {
        if (this == other) return true;
        if (other instanceof AssignCard otherAction) {
            return this.game.equals(otherAction.game)
                    && this.card.equals(otherAction.card)
                    && this.obstacle.equals(otherAction.obstacle);
        }
        return false;
    }

}
