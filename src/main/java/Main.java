import nmts.game.frames.GameFrame;
import nmts.game.screen.PlayScreen;
import nmts.game.screen.TestScreen;
import nmts.game.screen.TestScreen2;

public class Main {
    public static void main(String[] args) {
        GameFrame frame = new GameFrame();
        frame.setGame(new PlayScreen());
        //frame.setGame(new TestScreen());
        //frame.setGame(new TestScreen2());
    }
}
