package legends.game;
import java.util.Scanner;
public class MainMenu {

    public enum Choice {
        NEW_GAME,
        CONTINUE,
        EXIT
    }
    private final Scanner in;
    public MainMenu(Scanner in) {
        this.in = in;
    }

    public Choice prompt(boolean hasActiveGame, int currentFloor) {
        while (true) {
            System.out.println();
            System.out.println(UI.title("Legends: Heroes and Monsters"));
            // Option 1
            System.out.println("1) Start New Game");
            // Option 2 - continue only if game exists
            if (hasActiveGame) {
                System.out.println("2) Continue Exploration (floor " + currentFloor + ")");
            } else {
                System.out.println("2) Continue Exploration (no active game)");
            }
            // Option 3
            System.out.println("3) Exit");
            System.out.print("Enter choice: ");
            if (!in.hasNextLine()) {
                System.out.println("[AUTO] No more input. Exiting game.");
                return Choice.EXIT;
            }
            String choice = in.nextLine().trim();
            switch (choice) {
                case "1":
                    return Choice.NEW_GAME;
                case "2":
                    return Choice.CONTINUE;
                case "3":
                    return Choice.EXIT;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}

