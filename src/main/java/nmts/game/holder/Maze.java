package nmts.game.holder;

import java.util.Random;
import nmts.game.builder.MazeBuilder;
import org.gvoid.engine.math.CMath;

import static nmts.game.builder.MazeBuilder.*;

public class Maze {
    public static final int MAZE_WIDTH = 10;
    public static final int MAZE_HEIGHT = 10;

    public static final int[] GW = {
            WL, WT, WR, WB
    };
    public static final double[][] GWP = {
            {0.0d, 1.0d},
            {0.0d, 0.0d},
            {1.0d, 0.0d},
            {1.0d, 1.0d},
            {0.0d, 1.0d}
    };

    private final Random ra;
    private MazeBuilder mb;
    public int width, height;
    public int[] data;
    public int[][] maze;

    public Maze(Random random) {
        if (random == null) {
            random = new Random();
        }

        ra = random;
        mb = null;
        data = null;
        maze = null;
    }

    public void buildMaze() {
        if (mb == null) {
            mb = MazeBuilder.with(
                    MAZE_WIDTH, MAZE_HEIGHT
            );
            data = new int[5];
        } else mb.fill();

        maze = mb
                .setStart(
                        ra.nextInt(mb.getWidth()),
                        ra.nextInt(mb.getHeight())
                )
                .create(ra, data, true)
                .getMaze();
        width = mb.getWidth();
        height = mb.getHeight();
    }

    public static int pathDir(int t) {
        int dir;
        if ((t & PL) != 0) dir = 0;
        else if ((t & PT) != 0) dir = 1;
        else if ((t & PR) != 0) dir = 2;
        else if ((t & PB) != 0) dir = 3;
        else throw new IllegalStateException("No path");
        return dir;
    }

    public static boolean wallDir(int t, int dir) {
        if (dir == 0) return ((t & WL) != 0);
        if (dir == 1) return ((t & WT) != 0);
        if (dir == 2) return ((t & WR) != 0);
        if (dir == 3) return ((t & WB) != 0);
        throw new IllegalArgumentException("Invalid dir");
    }

    public static int insert(int t, double[] s, long[] es, int len) {
        //if (len > 0) CMath.set(es, len);
        boolean c = false;
        for (int i = 0, wi = 1, ai = (len + 1) * 2; i < GW.length; i++, wi++) {
            if ((t & GW[i]) == 0) {
                c = false;
                continue;
            }
            if (c) {
            } else {
                CMath.set(es, len);
            }
            c = true;
        }
        return 0;
    }

    public static int dirX(int dir) {
        return (dir - 1) % 2;
    }

    public static int dirY(int dir) {
        return (dir - 2) % 2;
    }
}
