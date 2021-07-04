package nmts.game.screen;

import java.awt.*;
import org.gvoid.engine.Game;
import org.gvoid.engine.math.CMath;

public class TestScreen extends Game {
    private double[] shape;

    public TestScreen() {
        shape = new double[0];
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

    @Override
    protected void onRender(Graphics2D graphics) {
        double[] s = shape;
        if (s.length <= 4) return;

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

        double px = cursor.pos.x;
        double py = cursor.pos.y;

        double[] t = new double[5];
        int i = CMath.distToShape(t, s, null, s.length / 2, px, py, true);
        CMath.calc(t);

        double hpx = t[3], hpy = t[4], hr = 5d;

        graphics.setColor(Color.GREEN);
        graphics.fillArc((int) (hpx - hr), (int) (hpy - hr), (int) (hr * 2d), (int) (hr * 2d), 0, 360);

        graphics.setColor(Color.ORANGE);
        graphics.drawLine((int) px, (int) py, (int) (px + t[0] * t[2]), (int) (py + t[1] * t[2]));
    }
}
