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

package org.gvoid.engine.math;

public class CMath {
    public static double PRECISION = 0.0001d;

    public static double clamp(double n, double mi, double ma) {
        return n < mi ? mi : Math.min(ma, n);
    }

    public static int castRay(double[] t, double[] s, long[] es, int sl, double px, double py, double dx, double dy, double d, boolean clamp) {
        return castSphere(t, s, es, sl, px, py, dx, dy, d, 0d, clamp, false);
    }

    public static int castRay(double[] t, double[] s, long[] es, int sl, double px, double py, double dx, double dy, double d, boolean clamp, boolean exc) {
        return castSphere(t, s, es, sl, px, py, dx, dy, d, 0d, clamp, exc);
    }

    public static int castSphere(double[] t, double[] s, long[] es, int sl, double px, double py, double dx, double dy, double d, double r, boolean clamp) {
        return castSphere(t, s, es, sl, px, py, dx, dy, d, r, clamp, false);
    }

    public static int castSphere(double[] t, double[] s, long[] es, int sl, double px, double py, double dx, double dy, double d, double r, boolean clamp, boolean exc) {
        double pc = PRECISION;

        double dl = dx * dx + dy * dy;
        if (dl == 0d) return -1;
        else if (dl != 1d) {
            dl = Math.pow(dl, 0.5d);
            dx /= dl;
            dy /= dl;
        }

        double rd = d, hd, cd;
        double cx = px, cy = py;
        int hi;
        for (;; rd -= hd) {
            cd = rd * rd + 0.0001d;
            hi = distToShape(t, s, es, sl,
                             cx, cy, dx, dy, cd,
                             clamp, exc);
            if (hi == -1) break;
            hd = Math.pow(t[2], 0.5d) - r;
            if (hd > rd) break;
            if (hd <= pc) {
                t[0] = cx;
                t[1] = cy;
                t[2] = d - rd;
                return hi;
            }
            cx += dx * hd;
            cy += dy * hd;
        }
        return -1;
    }

    public static void calc(double[] t) {
        double dtl;
        t[2] = dtl = Math.pow(t[2], 0.5d);
        t[0] /= dtl;
        t[1] /= dtl;
    }

    public static int distToShape(double[] t, double[] s, long[] es, int sl, double px, double py, boolean clamp) {
        return distToShape(t, s, es, sl, px, py, 0d, 0d, Double.MAX_VALUE, clamp, false);
    }

    public static int distToShape(double[] t, double[] s, long[] es, int sl, double px, double py, double d, boolean clamp) {
        return distToShape(t, s, es, sl, px, py, 0d, 0d, d, clamp, false);
    }

    public static int distToShape(double[] t, double[] s, long[] es, int sl, double px, double py, double dx, double dy, double d, boolean clamp) {
        return distToShape(t, s, es, sl, px, py, dx, dy, d, clamp, false);
    }

    public static int distToShape(double[] t, double[] s, long[] es, int sl, double px, double py, double dx, double dy, double d, boolean clamp, boolean exc) {
        boolean ca, cd;
        exc &= ca = es != null;
        cd = dx != 0d || dy != 0d;
        double sx = s[0], sy = s[1], ex, ey;
        double v1x, v1y, v2x, v2y;
        double ps, tx, ty, dtx, dty, dtl, dtd;
        int si = 1, fi = -1, ai = 2;
        do {
            v1x = (ex = s[ai++]) - sx;
            v1y = (ey = s[ai++]) - sy;
            calc: {
                if (ca && has(es, si)) break calc;
                ps = projScale(v1x, v1y,
                               v2x = px - sx,
                               v2y = py - sy);
                if (clamp) ps = clamp(ps, 0f, 1f);
                dtx = (tx = v1x * ps) - v2x;
                dty = (ty = v1y * ps) - v2y;
                dtl = dtx * dtx + dty * dty;
                if (dtl > d) break calc;
                dir: if (cd) {
                    dtd = dtx * dx + dty * dy;
                    if (dtd >= 0d) break dir;
                    if (exc) set(es, si);
                    break calc;
                }
                d = dtl;
                fi = si;
                t[3] = tx + sx;
                t[4] = ty + sy;
                t[2] = dtl;
                t[0] = dtx;
                t[1] = dty;
            }
            sx = ex;
            sy = ey;
        } while (++si < sl);
        return fi;
    }

    public static void distToLine(double[] t, double x1, double y1, double x2, double y2, double x3, double y3, boolean clamp) {
        double v1x = x2 - x1, v1y = y2 - y1;
        double v2x = x3 - x1, v2y = y3 - y1;
        distToProj(t, v1x, v1y, v2x, v2y, clamp);
        t[3] += x1;
        t[4] += y1;
    }

    public static void distToProj(double[] t, double v1x, double v1y, double v2x, double v2y, boolean clamp) {
        double ps = projScale(v1x, v1y, v2x, v2y);
        if (clamp) ps = clamp(ps, 0d, 1d);
        double px, py;
        t[3] = px = v1x * ps;
        t[4] = py = v1y * ps;
        double dpx = px - v2x, dpy = py - v2y;
        t[2] = dpx * dpx + dpy * dpy;
        t[0] = dpx;
        t[1] = dpy;
    }

    public static double projScale(double v1x, double v1y, double v2x, double v2y) {
        double ds = v1x * v1x + v1y * v1y;
        if (ds == 0d) return 0d;
        return (v1x * v2x + v1y * v2y) / ds;
    }

    public static void cBits(long[] bits) {
        for (int i = 0, l = bits.length; i < l; i++) {
            bits[i] = 0L;
        }
    }
    public static long[] nBits(int n) {
        return new long[((n - 1) >> 6) + 1];
    }
    public static void set(long[] bits, int i) {
        bits[i >> 6] |= 1L << i;
    }
    public static boolean has(long[] bits, int i) {
        return (bits[i >> 6] & (1L << i)) != 0L;
    }
}
