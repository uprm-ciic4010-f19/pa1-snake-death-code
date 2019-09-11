package Main;


/**
 * Created by AlexVR on 7/1/2018.
 */

// CHange grid size here
public class Launch {

    public static void main(String[] args) {
        GameSetUp game = new GameSetUp("Snake", 600, 600);
        game.start();
    }
}
