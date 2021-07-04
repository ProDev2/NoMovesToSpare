package nmts.game.holder;

import java.awt.*;
import org.gvoid.engine.math.CMath;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static nmts.game.builder.MazeBuilder.WB;
import static nmts.game.builder.MazeBuilder.WL;
import static nmts.game.builder.MazeBuilder.WR;
import static nmts.game.builder.MazeBuilder.WT;

public class MazeCollider {
    private int cap, len;
    private double[] sp;
    private long[] es;

    public MazeCollider(int minCap) {
        ensure(minCap);
    }

    public int cap() {
        return cap;
    }

    public int len() {
        return len;
    }

    public void ensure(int amount) {
        int l = len;
        amount -= cap - l;
        if (amount <= 0) return;
        int nc = max(amount, l * 2) + 2;
        double[] nsp = new double[nc * 2];
        long[] nes = CMath.nBits(nc);
        if (l > 0) {
            System.arraycopy(sp, 0, nsp, 0, l * 2);
            System.arraycopy(es, 0, nes, 0, es.length);
        }
        cap = nc;
        sp = nsp;
        es = nes;
    }

    public void clear() {
        if (len > 0) {
            CMath.cBits(es);
        }
        len = 0;
    }

    public void insert(int[][] grid, double sc) {
        int xi, yi;
        int[] row;
        for (yi = 0; yi < grid.length; yi++) {
            row = grid[yi];
            for (xi = 0; xi < row.length; xi++) {
                insert((double) xi * sc, (double) yi * sc, sc, row[xi]);
            }
        }
    }

    public void insert(int[][] grid, int x, int y, int rad, double sc) {
        insert(grid, x - rad, y - rad, x + rad + 1, y + rad + 1, sc);
    }

    public void insert(int[][] grid, int sx, int sy, int ex, int ey, double sc) {
        sx = max(sx, 0);
        sy = max(sy, 0);
        int xi, yi;
        int[] row;
        for (yi = min(ey, grid.length) - 1; yi >= sy; yi--) {
            row = grid[yi];
            for (xi = min(ex, row.length) - 1; xi >= sx; xi--) {
                insert((double) xi * sc, (double) yi * sc, sc, row[xi]);
            }
        }
    }

    public void insert(long x, long y, int t) {
        insert(x, y, 1d, t);
    }

    public void insert(double x, double y, double sc, int t) {
        ensure(WP.length);
        len = insert(x, y, sc, t, sp, es, len);
    }

    public boolean castRay(double[] t, double px, double py, double dx, double dy, double d) {
        return castSphere(t, px, py, dx, dy, d, 0d, true, false);
    }

    public boolean castRay(double[] t, double px, double py, double dx, double dy, double d, boolean clamp, boolean exc) {
        return castSphere(t, px, py, dx, dy, d, 0d, clamp, exc);
    }

    public boolean castSphere(double[] t, double px, double py, double dx, double dy, double d, double r) {
        return castSphere(t, px, py, dx, dy, d, r, true, false);
    }

    public boolean castSphere(double[] t, double px, double py, double dx, double dy, double d, double r, boolean clamp, boolean exc) {
        if (len < 2) return false;
        long[] es = this.es;
        if (exc) {
            int esl = es.length;
            long[] nes = new long[esl];
            System.arraycopy(es, 0, nes, 0, esl);
            es = nes;
        }
        int hi = CMath.castSphere(t, sp, es, len, px, py, dx, dy, d, r, clamp, exc);
        return hi >= 0;
    }

    public void render(Graphics2D graphics, double x, double y, double sc) {
        if (len < 2) return;

        double[] s = sp;
        long[] e = es;

        int si = 2, sl = len;
        double sx = s[0], sy = s[1], ex, ey;
        do {
            int ii = si / 2;

            ex = s[si++];
            ey = s[si++];

            if (!CMath.has(e, ii)) {
                graphics.drawLine(
                        (int) (x + sx * sc),
                        (int) (y + sy * sc),
                        (int) (x + ex * sc),
                        (int) (y + ey * sc)
                );
            }

            sx = ex;
            sy = ey;
        } while (si < sl * 2);
    }

    public static final int[] W = {
            WL, WT, WR, WB
    };
    public static final double[] WP = {
            0.0d, 1.0d,
            0.0d, 0.0d,
            1.0d, 0.0d,
            1.0d, 1.0d,
            0.0d, 1.0d
    };

    public static int insert(double x, double y, double sc, int t, double[] s, long[] es, int len) {
        boolean dc = true;
        int c = W.length;
        for (int i = 0, wi = 0, ti, ai = len * 2; i < c; i++) {
            if ((t & W[i]) == 0) {
                if (dc) wi += 2;
                dc = true;
            } else {
                if (dc) CMath.set(es, len);
                for (ti = dc ? 2 : 1; ti > 0; ti--, len++) {
                    s[ai++] = x + WP[wi++] * sc;
                    s[ai++] = y + WP[wi++] * sc;
                }
                dc = false;
            }
        }
        return len;
    }
}
