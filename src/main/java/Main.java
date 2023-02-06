import crossfire.*;
import crossfire.actions.Action;
import crossfire.actions.EndTurn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.out;

public class Main {
    static GameImpl game;
    static private List<Integer> userInputs;

    public static void setup() {
        Obstacle firstObstacle = new ObstacleImpl(List.of(Damage.GRAY.times(2), Damage.RED), "firstObstacle");
        Obstacle secondObstacle = new ObstacleImpl(List.of(Damage.RED, Damage.GRAY.times(2)), "secondObstacle");
        List<Card> cards = new LinkedList<>();
        for (int id=0; id<2*10; id++) {
            cards.add(Card.Street_Smarts(id));
        }
        var playerBuilder = new PlayerBuilder();
        var first = playerBuilder.deck(cards.subList(0, 10)).build();
        var second = playerBuilder.deck(cards.subList(10, 20)).build();

        first.placeObstacle(firstObstacle);
        second.placeObstacle(secondObstacle);

        userInputs = new LinkedList<>();

        game = new GameImpl(List.of(first, second));
    }

    public static void main(String [] args) {
        setup();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            gameLoadRequest(reader);
            while (true) {
                display();
                selectAction(reader);
            }

        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private static void gameLoadRequest(BufferedReader reader) throws IOException {
        System.out.println("Should a saved game be loaded (Y/N)");
        if (getNextString(reader).strip().equals("Y"))
        {
            game = gameLoader.load(game);

        }

    }

    private static int getNextInt(BufferedReader reader) throws IOException {
        return Integer.parseInt(reader.readLine());
    }

    private static String getNextString(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    private static <T> void printItems(List<T> items) {
        for (int i = 0; i < items.size(); i++) {
            out.println("\t[" + i + "]: " + items.get(i));
        }
    }

    private static void display() {
        out.println("Player " + game.activePlayer());
        out.println("Hand: ");
        printItems(game.activePlayer().hand());
        out.println("Cards in play:");
        printItems(game.cardsInPlay().keySet().stream().toList());
        out.println("Obstacles:");
        printItems(game.obstaclesInPlay().keySet().stream().toList());
        out.println("Actions:");
        printItems(game.getActions().stream().map(Action::describe).toList());
    }

    private static void selectAction(BufferedReader reader) throws IOException {
        var actions = game.getActions();
        int action = getNextInt(reader);
        userInputs.add(action);
        actions.get(action).take();
        if (actions.get(action) instanceof EndTurn )
        {
            game.saveGame(userInputs);
        }
    }

}
