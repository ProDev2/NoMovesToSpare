package nmts.game.holder;

import java.util.Random;
import nmts.game.builder.MazeBuilder;

import static nmts.game.builder.MazeBuilder.*;

public class Maze {
    public int MAZE_WIDTH = 4;
    public int MAZE_HEIGHT = 4;

    public final Random ra;
    private MazeBuilder mb;
    public int width, height;
    public int[] data;
    public int[][] maze;

    public final int[] dirs;

    public Maze(Random random) {
        if (random == null) {
            random = new Random();
        }

        ra = random;
        mb = null;
        data = null;
        maze = null;

        dirs = new int[4];
    }

    public void reset() {
        mb = null;
        width = height = 0;
        data = null;
        maze = null;
    }

    public void buildMaze() {
        boolean hasData = data != null
                && maze != null;

        int rw = MAZE_WIDTH, rh = MAZE_HEIGHT;
        if (mb == null) {
            mb = MazeBuilder.with(rw, rh);
            data = new int[5];
        } else if (mb.getWidth() != rw || mb.getHeight() != rh) {
            mb = MazeBuilder.with(rw, rh);
        } else mb.fill();

        int sx, sy;
        if (hasData) {
            sx = data[3];
            sy = data[4];

            sx -= mb.getXOff(sx);
            sy -= mb.getYOff(sy);
        } else {
            sx = ra.nextInt(mb.getWidth());
            sy = ra.nextInt(mb.getHeight());
        }

        maze = mb
                .setStart(sx, sy)
                .create(ra, data, true)
                .getMaze();
        width = mb.getWidth();
        height = mb.getHeight();
    }

    public void countDirs() {
        int[][] m = maze;
        int[] d = data;
        for (int i = 0; i < 4; i++) {
            dirs[i] = 0;
        }
        int cx = d[0], cy = d[1];
        int lt = pathDir(m[cy][cx]), t;
        for (int i = 0, len = d[2] - 1; i < len; i++, lt = t) {
            t = pathDir(m[cy][cx]);
            cx += dirX(t);
            cy += dirY(t);
            if (lt != t) dirs[t]++;
        }
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

    public static int dirX(int dir) {
        return (dir - 1) % 2;
    }

    public static int dirY(int dir) {
        return (dir - 2) % 2;
    }

    public static boolean atGoal(int t) {
        return (t & E) != 0;
    }

    public static boolean atPath(int t) {
        return (t & PA) != 0;
    }
}
