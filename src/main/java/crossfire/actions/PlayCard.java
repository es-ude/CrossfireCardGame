package crossfire.actions;

import crossfire.Card;
import crossfire.Player;

public class PlayCard implements Action {
    private final Player cardOwner;
    private final Card toBePlayed;

    public PlayCard(Player cardOwner, Card toBePlayed) {
        this.cardOwner = cardOwner;
        this.toBePlayed = toBePlayed;
    }

    @Override
    public String describe() {
        return "Play Card: " + toBePlayed.toString();
    }

    @Override
    public void take() {
        cardOwner.play(toBePlayed);
    }

    public boolean equals(Object other) {
        if (this == other) return true;
        if (other instanceof PlayCard) {
            return this.cardOwner.equals(((PlayCard) other).cardOwner) && this.toBePlayed.equals(((PlayCard) other).toBePlayed);
        }
        return false;
    }
}
