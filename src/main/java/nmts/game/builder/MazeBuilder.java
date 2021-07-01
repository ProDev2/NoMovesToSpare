/*
 * Copyright (c) 2021 GVoid (Pascal Gerner).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nmts.game.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public final class MazeBuilder {
    public static final int S = 0x1 << 30;
    public static final int E = 0x1 << 31;

    public static final int WL = 0x1 << 26;
    public static final int WT = 0x1 << 27;
    public static final int WR = 0x1 << 28;
    public static final int WB = 0x1 << 29;
    public static final int WA = WL | WT | WR | WB;

    public static final int M = 0x1 << 25;

    public static final int PL = 0x1 << 21;
    public static final int PT = 0x1 << 22;
    public static final int PR = 0x1 << 23;
    public static final int PB = 0x1 << 24;
    public static final int PA = PL | PT | PR | PB;

    /* -------- Initialization -------- */
    public static MazeBuilder with(int width, int height) {
        return new MazeBuilder(width, height);
    }

    /* -------- Builder --------------- */
    private final int[][] mMaze;
    private final int mWidth, mHeight;

    private int mSx, mSy;

    private MazeBuilder(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be at least 1");
        }

        mMaze = new int[height][width];
        mWidth = width;
        mHeight = height;

        fill();
    }

    @SuppressWarnings({
            "UnusedReturnValue",
            "UnnecessaryLocalVariable"
    })
    public MazeBuilder fill() {
        int[][] maze = mMaze;

        int width = mWidth;
        int height = mHeight;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                maze[y][x] = WA;
            }
        }

        return this;
    }

    public boolean isXIn(int x) {
        return (x | mWidth - 1 - x) >= 0;
    }

    public boolean isYIn(int y) {
        return (y | mHeight - 1 - y) >= 0;
    }

    public boolean isIn(int x, int y) {
        return (x | y | mWidth - 1 - x | mHeight - 1 - y) >= 0;
    }

    public int getXOff(int x) {
        if (x < 0) return x;
        else if (x >= mWidth) return x - mWidth + 1;
        else return 0;
    }

    public int getYOff(int y) {
        if (y < 0) return y;
        else if (y >= mHeight) return y - mHeight + 1;
        else return 0;
    }

    public MazeBuilder setStart(int x, int y) {
        mSx = x;
        mSy = y;
        return this;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int[][] getMaze() {
        return mMaze;
    }

    public MazeBuilder create(Random r, int[] n, boolean p) {
        if (r == null) {
            r = new Random();
        }
        if (n != null) {
            for (int i = 0; i < 5; i++) {
                n[i] = -1;
            }
        }

        int[][] m = mMaze;
        int w = mWidth, h = mHeight, s = w * h;
        int sx = mSx, sy = mSy;

        if (n != null) {
            n[0] = sx;
            n[1] = sy;
        }

        int[] di = new int[] {w, h};
        int[] sc = new int[] {sx, sy};
        boolean[][] mm = new boolean[3][3];

        int cl = 0, pl = 0, epl = 0;
        List<int[]> cs = new ArrayList<>(s);
        List<int[]> ps = p ? new ArrayList<>(s) : null;
        List<int[]> eps = p ? new ArrayList<>(s) : null;

        m[sy][sx] |= M | S;
        cs.add(cl++, sc);
        if (p) ps.add(pl++, sc);

        int[] e = null;
        int l = 1, el = 0;

        while (cl > 0) {
            int[] c = cs.remove(--cl);
            int mc = map(m, di, c, 1, M, true, mm);
            if (mc <= 0) {
                int[] pc = null;
                if (p) pc = ps.remove(--pl);
                if (p & el - epl >= l--) eps.add(epl++, pc);
                continue;
            } else l++;
            cs.add(cl++, c);

            int x = c[0], y = c[1], nxr, nyr;
            int t = 5, v = 0;
            do {
                int nv = --t > 1 ? r.nextInt(t) : 1;
                v = (v + nv) % 4;
                nxr = (v - 1) % 2;
                nyr = (v - 2) % 2;
            } while (!mm[nyr + 1][nxr + 1]);
            int nx = x + nxr, ny = y + nyr;
            int[] nc = new int[] {nx, ny, nxr, nyr};

            m[ny][nx] |= M;
            cs.add(cl++, nc);
            if (p) ps.add(pl++, nc);

            if (l >= el) {
                el = l;
                e = nc;
            }
            if (l >= el && p && epl > 0) {
                eps.clear();
                epl = 0;
            }

            if (nxr < 0) {
                m[y][x] &= ~WL;
                m[ny][nx] &= ~WR;
            } else if (nyr < 0) {
                m[y][x] &= ~WT;
                m[ny][nx] &= ~WB;
            } else if (nxr > 0) {
                m[y][x] &= ~WR;
                m[ny][nx] &= ~WL;
            } else if (nyr > 0) {
                m[y][x] &= ~WB;
                m[ny][nx] &= ~WT;
            }
        }

        if (e != null) m[e[1]][e[0]] |= E;
        if (p && epl > 0) {
            int[] pc = eps.remove(--epl);
            while (epl > 0) {
                int[] npc = eps.remove(--epl);

                int x = pc[0], y = pc[1];
                int nxr = npc[2], nyr = npc[3];

                if (nxr < 0) m[y][x] |= PL;
                else if (nyr < 0) m[y][x] |= PT;
                else if (nxr > 0) m[y][x] |= PR;
                else if (nyr > 0) m[y][x] |= PB;

                pc = npc;
            }
        }

        if (e != null && n != null) {
            n[2] = el;
            n[3] = e[0];
            n[4] = e[1];
        }

        return this;
    }

    public static int map(int[][] src, int[] dim, int[] pos, int shape, int mask, boolean zero, boolean[][] dest) {
        int w = dim[0], h = dim[1], c = 0, v;
        for (int y = 0, ys = pos[1] - 1; y < 3; y++, ys++) {
            for (int x = 0, xs = pos[0] - 1; x < 3; x++, xs++) {
                //noinspection StatementWithEmptyBody
                if (shape == 0) ;
                else if (shape == 1 && x % 2 == y % 2) continue;
                else if (shape == 2 && x % 2 == 0 & y % 2 == 0) continue;
                else if (shape == 3 && x % 2 != y % 2) continue;
                else if (shape == 4 && x % 2 != 0 | y % 2 != 0) continue;
                boolean is;
                dest[y][x] = is = (xs | ys | w - 1 - xs | h - 1 - ys) >= 0
                        && zero & (v = src[ys][xs] & mask) == 0 | !zero & v != 0;
                if (is) c++;
            }
        }
        return c;
    }
}
