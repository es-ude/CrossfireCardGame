package crossfire;

import crossfire.actions.Action;

import java.io.IOException;
import java.util.List;

public interface Game {
    List<Action> getActions();

    void saveGame(List<Integer> userInputs) throws IOException;
}
