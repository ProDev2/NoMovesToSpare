package nmts.game.screen;

import java.awt.*;
import org.gvoid.engine.Game;
import org.gvoid.engine.math.CMath;

public class TestScreen2 extends Game {
    private double[] shape;
    private long[] skip;

    public TestScreen2() {
        shape = new double[0];
        skip = CMath.nBits(1000);
    }

    @Override
    protected void onUpdate(float deltaTime) {
        if (shape.length <= 0) {
            addTestPoint(0.2d, 0.2d);
            addTestPoint(0.4d, 0.1d);
            addTestPoint(0.3d, 0.5d);
            addTestPoint(0.34d, 0.6d);
            addTestPoint(0.7d, 0.4d);
            addTestPoint(0.5d, 0.9d);
            addTestPoint(0.1d, 0.85d);
            addTestPoint(0.15d, 0.4d);

            CMath.set(skip, 4);
        }
    }

    public void addTestPoint(double x, double y) {
        double ax = size.x * x, ay = size.y * y;
        int l = shape.length, nl = l + 2;
        double[] n = new double[nl];
        System.arraycopy(shape, 0, n, 0, l);
        n[l++] = ax;
        n[l++] = ay;
        shape = n;
    }

    private Double ppx, ppy;

    @Override
    protected void onRender(Graphics2D graphics) {
        double[] s = shape;
        if (s.length <= 4) return;

        long[] skip = this.skip;
        long[] sp = new long[skip.length];
        System.arraycopy(skip, 0, sp, 0, skip.length);


        int si = 2, sl = s.length;
        double sx = s[0], sy = s[1], ex, ey;
        do {
            ex = s[si++];
            ey = s[si++];

            graphics.setColor(Color.BLACK);
            graphics.setStroke(new BasicStroke());
            graphics.drawLine((int) sx, (int) sy, (int) ex, (int) ey);

            sx = ex;
            sy = ey;
        } while (si < sl);

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
        int i = CMath.castSphere(t, s, sp, s.length / 2 - 1, px, py, dx, dy, Double.MAX_VALUE, hr, true, true);
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
