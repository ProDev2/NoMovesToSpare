package nmts.game.holder;

public class Player {
    private final Maze maze;
    public float x, y;
    public int dir;

    public float sps = 1.7f;
    public float radius = 0.25f;

    public Player(Maze maze) {
        this.maze = maze;
        reset();
    }

    public void reset() {
        int[] d = maze.data;
        x = d[0];
        y = d[1];

        int t = maze.maze[d[1]][d[0]];
        dir = Maze.pathDir(t);
    }

    public boolean update(float deltaTime) {
        int[][] m = maze.maze;
        if (m == null) return false;

        int dir = this.dir;
        float dx = Maze.dirX(dir);
        float dy = Maze.dirY(dir);

        float mdx = dx * deltaTime * sps;
        float mdy = dy * deltaTime * sps;

        float rdx = dx * radius;
        float rdy = dy * radius;

        float px = this.x + rdx, py = this.y + rdy;
        float nx = px + mdx, ny = py + mdy;

        int apx = (int) px, apy = (int) py;
        int anx = (int) nx, any = (int) ny;

        if (apx != anx || apy != any) {
            int pt = m[apy][apx];
            if (Maze.wallDir(pt, dir)) {
                return true;
            }

            int nt = m[any][anx];
        }

        this.x += mdx;
        this.y += mdy;
        return false;
    }
}
