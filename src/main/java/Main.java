import crossfire.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static java.lang.System.out;

public class Main {
    static Game game;

    public static void main(String [] args) {
        Obstacle firstObstacle = new ObstacleImpl(List.of(Damage.GRAY.times(2), Damage.RED), "firstObstacle");
        Obstacle secondObstacle = new ObstacleImpl(List.of(Damage.RED, Damage.GRAY.times(2)), "secondObstacle");

        var first = new Player();
        var second = new Player();

        first.placeObstacle(firstObstacle);
        second.placeObstacle(secondObstacle);

        game = new Game(List.of(first, second));

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                display();
                selectAction(reader);
            }

        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private static int getNextInt(BufferedReader reader) throws IOException {
        return Integer.parseInt(reader.readLine());
    }

    private static void assignDamage(BufferedReader reader) throws IOException {
        var playedCards = game.cardsInPlay().keySet().stream().toList();
        out.println("select card in play: ");
        printItems(playedCards);
        int cardIndex = getNextInt(reader);
        var obstacles = game.obstaclesInPlay().keySet().stream().toList();
        out.println("select and obstacle:\n");
        printItems(obstacles);
        int obstacleIndex = getNextInt(reader);
        game.assign(playedCards.get(cardIndex), obstacles.get(obstacleIndex));
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
        printItems(List.of("play card", "assign Damage", "end Turn"));
    }

    private static void selectAction(BufferedReader reader) throws IOException {
        int action = getNextInt(reader);
        switch (action) {
            case 0 -> playCard(reader);
            case 1 -> assignDamage(reader);
            case 2 -> endTurn();
        }
    }

    private static void endTurn(){
        game.endTurn();
    }

    private static void playCard(BufferedReader reader) throws IOException {
        var p = game.activePlayer();
        var hand = p.hand();
        int cardIndex = getNextInt(reader);
        p.play(hand.get(cardIndex));
    }
}
