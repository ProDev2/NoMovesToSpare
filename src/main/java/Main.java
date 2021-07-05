import java.util.Locale;
import nmts.game.frames.GameFrame;
import nmts.game.screen.PlayScreen;
import nmts.game.screen.TestScreen;
import nmts.game.screen.TestScreen2;
import nmts.game.screen.TestScreen3;

public class Main {
    @SuppressWarnings("SpellCheckingInspection")
    public static void main(String[] args) {
        GameFrame frame = new GameFrame();

        int flags = 0;
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String cmd = args[i].toLowerCase(Locale.ROOT);
                cmd = cmd.replaceAll("[^a-zA-z\\-]", "");
                if (!cmd.startsWith("-")) {
                    throw new IllegalArgumentException("Unknown argument: " + args[i]);
                } else cmd = cmd.substring(1);

                if (cmd.equals("-antialias") || cmd.equals("-anti") || cmd.equals("-a")) {
                    frame.setAntialias(false);
                } else if (cmd.equals("r") || cmd.equals("rv") || cmd.equals("rotview") || cmd.equals("rotateview")) {
                    flags |= PlayScreen.FLAG_ROTATE_VIEW;
                } else if (cmd.equals("i") || cmd.equals("im") || cmd.equals("infmoves") || cmd.equals("infinitemoves")) {
                    flags |= PlayScreen.FLAG_INFINITE_MOVES;
                } else if (cmd.equals("-w") || cmd.equals("-wt") || cmd.equals("-walltouch")) {
                    flags |= PlayScreen.FLAG_NO_WALL_TOUCH;
                } else if (cmd.equals("s") || cmd.equals("sk") || cmd.equals("swapkeys")) {
                    flags |= PlayScreen.FLAG_INVERT_KEYS;
                } else if (cmd.equals("va") || cmd.equals("viewall") || cmd.equals("all")) {
                    flags |= PlayScreen.FLAG_SHOW_ALL;
                } else if (cmd.equals("-vm") || cmd.equals("-viewmarkers") || cmd.equals("-markers")) {
                    flags |= PlayScreen.FLAG_HIDE_MARKERS;
                } else if (cmd.equals("-vw") || cmd.equals("-viewwalls") || cmd.equals("-walls")) {
                    flags |= PlayScreen.FLAG_HIDE_WALLS;
                }
            }
        }

        frame.setGame(new PlayScreen(flags));
        //frame.setGame(new TestScreen());
        //frame.setGame(new TestScreen2());
        //frame.setGame(new TestScreen3());
    }
}
