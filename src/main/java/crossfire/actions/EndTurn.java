package crossfire.actions;

import crossfire.GameImpl;

public class EndTurn implements Action {
    private final GameImpl game;

    public EndTurn(GameImpl game) {
        this.game = game;
    }

    @Override
    public String describe() {
        return "End Turn";
    }

    @Override
    public void take() {
        this.game.endTurn();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof EndTurn)
            return this.game.equals(((EndTurn) o).game);
        return false;
    }
}
