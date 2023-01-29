package crossfire;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class gameLoader {
    public static GameImpl load(GameImpl game) {
        String saveGameString = new String();
        try {
           saveGameString= new String(Files.readAllBytes(Paths.get("saveGame.txt")));
        } catch (IOException e) {
            System.out.println("Error while loading Game");
        }
        String[] savedInputs = saveGameString.split(",");

        for (String savedInput: savedInputs)
        {
            var actions = game.getActions();
            int action = Integer.parseInt(savedInput);
            actions.get(action).take();
        }
        return game;
    }
}
