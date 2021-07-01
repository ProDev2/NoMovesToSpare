package nmts.game.screen;

import java.awt.*;
import java.awt.event.KeyEvent;
import nmts.game.holder.Maze;
import nmts.game.holder.Player;
import org.gvoid.engine.Game;

import static nmts.game.builder.MazeBuilder.*;

public class PlayScreen extends Game {
    private final Maze maze;
    private final Player player;

    public PlayScreen() {
        maze = new Maze(null);
        maze.buildMaze();

        player = new Player(maze);
    }

    @Override
    protected void onUpdate(float deltaTime) {
        if (keys.handle(new int[] {KeyEvent.VK_A, KeyEvent.VK_LEFT})) player.dir = 0;
        else if (keys.handle(new int[] {KeyEvent.VK_W, KeyEvent.VK_UP})) player.dir = 1;
        else if (keys.handle(new int[] {KeyEvent.VK_D, KeyEvent.VK_RIGHT})) player.dir = 2;
        else if (keys.handle(new int[] {KeyEvent.VK_S, KeyEvent.VK_DOWN})) player.dir = 3;

        boolean hitWall = player.update(deltaTime);
        if (hitWall) {
            System.out.println("At wall");
        }
    }

    @Override
    protected void onRender(Graphics2D graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, size.xInt(), size.yInt());

        int[] d = maze.data;
        int[][] m = maze.maze;
        if (m == null) return;

        int mw = maze.width, mh = maze.height;

        float cx = size.xFloat() / 2f, cy = size.yFloat() / 2f;
        float cw = size.xFloat() / mw, ch = size.yFloat() / mh;
        float gs = Math.min(cw, ch) * 0.9f;
        float ws = gs / 16f, tws = ws * 2f;

        float left = cx - ((float) mw / 2f) * gs;
        float top = cy - ((float) mh / 2f) * gs;

        graphics.setColor(Color.CYAN);
        graphics.fillRect((int) (left + gs * (double) d[0]), (int) (top + gs * (double) d[1]), (int) gs, (int) gs);
        graphics.setColor(Color.GREEN);
        graphics.fillRect((int) (left + gs * (double) d[3]), (int) (top + gs * (double) d[4]), (int) gs, (int) gs);

        float plx = left + player.x * gs;
        float ply = top + player.y * gs;
        float pr = player.radius * gs, tpr = pr * 2f;
        float pcr = pr / 4f;

        graphics.setColor(Color.ORANGE);
        graphics.fillRoundRect((int) (plx - pr), (int) (ply - pr), (int) tpr, (int) tpr, (int) pcr, (int) pcr);

        for (int y = 0; y < mh; y++) {
            final int[] row = m[y];
            for (int x = 0; x < mw; x++) {
                int t = row[x];

                float ax = left + gs * x;
                float ay = top + gs * y;

                graphics.setColor(Color.WHITE);
                if ((t & WL) != 0) {
                    graphics.fillRect((int) (ax - ws), (int) (ay - ws), (int) tws, (int) (gs + tws));
                }
                if ((t & WT) != 0) {
                    graphics.fillRect((int) (ax - ws), (int) (ay - ws), (int) (gs + tws), (int) tws);
                }
                if ((t & WR) != 0) {
                    graphics.fillRect((int) (ax + gs - ws), (int) (ay - ws), (int) tws, (int) (gs + tws));
                }
                if ((t & WB) != 0) {
                    graphics.fillRect((int) (ax - ws), (int) (ay + gs - ws), (int) (gs + tws), (int) tws);
                }
            }
        }
    }
}
