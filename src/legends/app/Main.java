package legends.app;

import legends.game.GameController;

public class Main {
    public static void main(String[] args) {
        try {
            GameController controller = new GameController();
            controller.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
