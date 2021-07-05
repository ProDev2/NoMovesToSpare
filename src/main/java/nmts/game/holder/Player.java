package nmts.game.holder;

import nmts.game.builder.MazeBuilder;
import org.gvoid.engine.math.Maths;

public class Player {
    public static final int FROZEN = 0;
    public static final int ON_PATH = 1;
    public static final int OFF_PATH = 2;
    public static final int AT_WALL = 3;
    public static final int AT_GOAL = 4;

    private static final float PRECISION = 0.05f;

    private final Maze maze;
    private final MazeCollider collider;
    private final Camera camera;
    public float x, y;
    public int dir;
    public double angle;
    public float dx, dy;

    public float cps = 2.6f;
    public float sps = 2.1f;
    public float radius = 0.17f;

    public Player(Maze maze, MazeCollider collider, Camera camera) {
        this.maze = maze;
        this.collider = collider;
        this.camera = camera;
        this.y = this.x = 0.5f;
        reset(true);
    }

    public void syncCam(boolean force) {
        if (camera == null) return;
        camera.setDestPos(x, y, force);
    }

    public void reset() {
        reset(false);
    }

    public void reset(boolean jump) {
        float inset = Math.min(radius, 0.5f);

        int[] d = maze.data;
        x = d[0] + Maths.clamp(x % 1f, inset, 1f - inset);
        y = d[1] + Maths.clamp(y % 1f, inset, 1f - inset);

        int t = maze.maze[d[1]][d[0]];
        dir = Maze.pathDir(t);
        dx = dy = 0f;

        syncCam(jump);
    }

    public int update(float deltaTime) {
        int[][] m = maze.maze;
        if (m == null) return FROZEN;

        int state = ON_PATH;
        boolean move = true;

        float prc = PRECISION;

        int dir = this.dir;
        float ndx = Maze.dirX(dir);
        float ndy = Maze.dirY(dir);

        dx = Maths.lerp(dx, ndx, deltaTime * cps);
        dy = Maths.lerp(dy, ndy, deltaTime * cps);

        float mdx = dx * deltaTime * sps;
        float mdy = dy * deltaTime * sps;

        float mdl = (float) Math.pow(mdx * mdx + mdy * mdy, 0.5d);
        float mdt = Math.max(mdl - prc, prc);
        float rd = (float) Math.ceil(mdl * 1.1f);

        float rdx, rdy;
        if (mdl == 0d) {
            rdy = rdx = 0f;
        } else {
            rdx = (mdx / mdl) * prc;
            rdy = (mdy / mdl) * prc;

            angle = Math.atan2(-mdy, mdx);
        }

        float px = this.x + rdx, py = this.y + rdy;
        int apx = (int) this.x, apy = (int) this.y;

        int pt;
        try {
            pt = m[apy][apx];
        } catch (Exception e) {
            e.printStackTrace();
            pt = MazeBuilder.WA;
        }
        boolean whw = Maze.wallDir(pt, dir);

        collider.clear();
        collider.insert(m, apx, apy, (int) rd, 1d);

        double[] tr = new double[5];
        if (collider.castSphere(tr, px, py, mdx, mdy, rd, radius - prc) && tr[2] <= mdt) {
            mdx = (float) tr[0] - px;
            mdy = (float) tr[1] - py;

            if (whw) {
                dx = dy = 0f;
            } else {
                float ls = (float) Math.pow(dx * dx + dy * dy, 0.5d);
                dx = ndx * ls;
                dy = ndy * ls;
            }

            state = AT_WALL;
            move = tr[2] > 0d;
        }

        if (move) {
            this.x += mdx;
            this.y += mdy;
        }

        syncCam(false);

        if (Maze.atGoal(pt)) return AT_GOAL;
        if (!Maze.atPath(pt)) return OFF_PATH;
        return state;
    }
}
