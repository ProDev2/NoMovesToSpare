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

public final class Maths {
    public static double clamp(double n, double min, double max) {
        boolean norm = max - min >= 0d;
        if (n > (norm ? max : min))
            n = max;
        else if (n < (norm ? min : max))
            n = min;
        return n;
    }

    public static float clamp(float n, float min, float max) {
        boolean norm = max - min >= 0f;
        if (n > (norm ? max : min))
            n = max;
        else if (n < (norm ? min : max))
            n = min;
        return n;
    }

    public static long clamp(long n, long min, long max) {
        boolean norm = max - min >= 0L;
        if (n > (norm ? max : min))
            n = max;
        else if (n < (norm ? min : max))
            n = min;
        return n;
    }

    public static int clamp(int n, int min, int max) {
        boolean norm = max - min >= 0;
        if (n > (norm ? max : min))
            n = max;
        else if (n < (norm ? min : max))
            n = min;
        return n;
    }

    public static double lerpC(double s, double e, double t) {
        if (t < 0d) t = 0d;
        if (t > 1d) t = 1d;
        return s + (e - s) * t;
    }

    public static float lerpC(float s, float e, double t) {
        if (t < 0d) t = 0d;
        if (t > 1d) t = 1d;
        return s + (float) ((double) (e - s) * t);
    }

    public static long lerpC(long s, long e, double t) {
        if (t < 0d) t = 0d;
        if (t > 1d) t = 1d;
        return s + (long) ((double) (e - s) * t);
    }

    public static int lerpC(int s, int e, double t) {
        if (t < 0d) t = 0d;
        if (t > 1d) t = 1d;
        return s + (int) ((double) (e - s) * t);
    }

    public static double lerp(double s, double e, double t) {
        return s + (e - s) * t;
    }

    public static float lerp(float s, float e, double t) {
        return s + (float) ((double) (e - s) * t);
    }

    public static long lerp(long s, long e, double t) {
        return s + (long) ((double) (e - s) * t);
    }

    public static int lerp(int s, int e, double t) {
        return s + (int) ((double) (e - s) * t);
    }

    public static double step(double n, double to, double by) {
        if (n > to) {
            n -= by;
            if (n < to) n = to;
        } else if (n < to) {
            n += by;
            if (n > to) n = to;
        }
        return n;
    }

    public static float step(float n, float to, float by) {
        if (n > to) {
            n -= by;
            if (n < to) n = to;
        } else if (n < to) {
            n += by;
            if (n > to) n = to;
        }
        return n;
    }

    public static long step(long n, long to, long by) {
        if (n > to) {
            n -= by;
            if (n < to) n = to;
        } else if (n < to) {
            n += by;
            if (n > to) n = to;
        }
        return n;
    }

    public static int step(int n, int to, int by) {
        if (n > to) {
            n -= by;
            if (n < to) n = to;
        } else if (n < to) {
            n += by;
            if (n > to) n = to;
        }
        return n;
    }

    public static double in(double n, double m) {
        return (n %= m) >= 0d ? n : m + n;
    }

    public static float in(float n, float m) {
        return (n %= m) >= 0f ? n : m + n;
    }

    public static long in(long n, long m) {
        return (n %= m) >= 0L ? n : m + n;
    }

    public static int in(int n, int m) {
        return (n %= m) >= 0 ? n : m + n;
    }

    public static double in(double n) {
        return (n %= 1d) >= 0d ? n : 1d + n;
    }

    public static float in(float n) {
        return (n %= 1f) >= 0f ? n : 1f + n;
    }

    public static double min(double n1, double n2) {
        return n2 - n1 >= 0d ? n1 : n2;
    }

    public static double max(double n1, double n2) {
        return n1 - n2 >= 0d ? n1 : n2;
    }

    public static float min(float n1, float n2) {
        return n2 - n1 >= 0f ? n1 : n2;
    }

    public static float max(float n1, float n2) {
        return n1 - n2 >= 0f ? n1 : n2;
    }

    public static long min(long n1, long n2) {
        return n2 - n1 >= 0L ? n1 : n2;
    }

    public static long max(long n1, long n2) {
        return n1 - n2 >= 0L ? n1 : n2;
    }

    public static int min(int n1, int n2) {
        return n2 - n1 >= 0 ? n1 : n2;
    }

    public static int max(int n1, int n2) {
        return n1 - n2 >= 0 ? n1 : n2;
    }

    public static double positive(double n) {
        return n >= 0d ? n : -n;
    }

    public static float positive(float n) {
        return n >= 0f ? n : -n;
    }

    public static long positive(long n) {
        return n >= 0L ? n : -n;
    }

    public static int positive(int n) {
        return n >= 0 ? n : -n;
    }

    public static double negative(double n) {
        return n <= 0d ? n : -n;
    }

    public static float negative(float n) {
        return n <= 0f ? n : -n;
    }

    public static long negative(long n) {
        return n <= 0L ? n : -n;
    }

    public static int negative(int n) {
        return n <= 0 ? n : -n;
    }

    public static double interpolateSlowInSlowOut(double n) {
        double b = Math.floor(n);
        double t = in(n);
        return b + 0.5d - Math.cos(Math.PI * t) * 0.5d;
    }

    public static float interpolateSlowInSlowOut(float n) {
        double b = Math.floor(n);
        double t = in(n);
        return (float) (b + 0.5d - Math.cos(Math.PI * t) * 0.5d);
    }

    public static double interpolateFastInFastOut(double n) {
        double b = Math.floor(n);
        double t = in(n - 0.5d) - 0.5d;
        return b + in(Math.sin(Math.PI * t) * 0.5d);
    }

    public static float interpolateFastInFastOut(float n) {
        double b = Math.floor(n);
        double t = in((double) n - 0.5d) - 0.5d;
        return (float) (b + in(Math.sin(Math.PI * t) * 0.5d));
    }

    private Maths() {
        throw new UnsupportedOperationException();
    }
}
