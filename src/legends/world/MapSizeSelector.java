package legends.world;

import java.util.Scanner;
import legends.game.UI;

public class MapSizeSelector {

    public static int chooseMapSize(Scanner in) {
        while (true) {
            System.out.println(UI.title("Choose Region Size"));
            System.out.println("1) Small  (5 x 5)");
            System.out.println("2) Medium (8 x 8)");
            System.out.println("3) Large  (12 x 12)");
            System.out.print("Enter 1, 2, or 3: ");

            String line = in.nextLine().trim();
            switch (line) {
                case "1": return 5;
                case "2": return 8;
                case "3": return 12;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}
