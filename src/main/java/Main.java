import java.util.Locale;
import nmts.game.frames.GameFrame;
import nmts.game.screen.PlayScreen;
import nmts.game.screen.TestScreen;
import nmts.game.screen.TestScreen2;
import nmts.game.screen.TestScreen3;

public class Main {
    public static void main(String[] args) {
        GameFrame frame = new GameFrame();

        if (args.length > 0) {
            String cmd = args[0].toLowerCase(Locale.ROOT);
            cmd = cmd.replaceAll("[^a-zA-z\\-]", "");
            if (cmd.equals("--antialias") || cmd.equals("--anti") || cmd.equals("--a")) {
                frame.setAntialias(false);
            }
        }

        frame.setGame(new PlayScreen());
        //frame.setGame(new TestScreen());
        //frame.setGame(new TestScreen2());
        //frame.setGame(new TestScreen3());
    }
}
