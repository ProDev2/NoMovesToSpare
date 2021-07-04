package nmts.game.screen;

import java.awt.*;
import nmts.game.builder.MazeBuilder;
import nmts.game.holder.MazeCollider;
import org.gvoid.engine.Game;
import org.gvoid.engine.math.CMath;

public class TestScreen3 extends Game {
    private double[] shape;
    private long[] skip;
    private int len;

    public TestScreen3() {
        shape = new double[1000];
        skip = CMath.nBits(1000);
        len = 0;
    }

    @Override
    protected void onUpdate(float deltaTime) {
        if (len <= 0) {
            int t1 = MazeBuilder.WT | MazeBuilder.WB;
            int t2 = MazeBuilder.WL | MazeBuilder.WR;

            len = MazeCollider.insert(0, 0, 60, t1, shape, skip, len);
            len = MazeCollider.insert(60, 60, 60, t2, shape, skip, len);

            System.out.println("(" + shape[0] + ", " + shape[1] + ")");
            for (int i = 0, ai = 2; i <= len; i++, ai += 2) {
                System.out.println(i + " > " + CMath.has(skip, i) + " (" + shape[ai] + ", " + shape[ai + 1] + ")");
            }
        }
    }

    private Double ppx, ppy;

    @Override
    protected void onRender(Graphics2D graphics) {
        double[] s = shape;
        if (s.length <= 4) return;

        long[] skip = this.skip;
        long[] sp = new long[skip.length];
        System.arraycopy(skip, 0, sp, 0, skip.length);


        int si = 2, sl = len;
        double sx = s[0], sy = s[1], ex, ey;
        do {
            int ii = si / 2;

            ex = s[si++];
            ey = s[si++];

            if (!CMath.has(sp, ii)) {
                graphics.setColor(Color.BLACK);
                graphics.setStroke(new BasicStroke());
                graphics.drawLine((int) sx, (int) sy, (int) ex, (int) ey);
            }

            sx = ex;
            sy = ey;
        } while (si < sl * 2);

        double hr = 20d;

        double px = cursor.pos.x;
        double py = cursor.pos.y;

        if (cursor.leftPressed) {
            ppx = px;
            ppy = py;
        } else if (ppx == null || ppy == null) {
            return;
        }

        graphics.setColor(Color.ORANGE);
        graphics.fillArc((int) (ppx - hr), (int) (ppy - hr), (int) (hr * 2d), (int) (hr * 2d), 0, 360);

        double dx = px - ppx;
        double dy = py - ppy;

        double[] t = new double[5];
        //int i = CMath.distToShape(t, s, skip, px, py, true);
        //CMath.calc(t);
        int i = CMath.castSphere(t, s, sp, sl, px, py, dx, dy, Double.MAX_VALUE, hr, true, true);
        if (i < 0) return;

        //double hpx = t[3], hpy = t[4];
        double hpx = t[0], hpy = t[1];

        graphics.setColor(Color.GREEN);
        graphics.fillArc((int) (hpx - hr), (int) (hpy - hr), (int) (hr * 2d), (int) (hr * 2d), 0, 360);

        graphics.setColor(Color.ORANGE);
        //graphics.drawLine((int) px, (int) py, (int) (px + t[0] * t[2]), (int) (py + t[1] * t[2]));
        graphics.drawLine((int) px, (int) py, (int) t[3], (int) t[4]);

        graphics.setColor(Color.RED);
        graphics.drawLine((int) px, (int) py, (int) hpx, (int) hpy);
    }
}
