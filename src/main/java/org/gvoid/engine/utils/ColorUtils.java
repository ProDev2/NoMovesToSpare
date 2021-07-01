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

package org.gvoid.engine.utils;

public final class ColorUtils {
    public static final int A_SHIFT = 24;
    public static final int R_SHIFT = 16;
    public static final int G_SHIFT = 8;
    public static final int B_SHIFT = 0;

    public static final int COMP_MASK = 0xFF;
    public static final int A_MASK = COMP_MASK << A_SHIFT;
    public static final int R_MASK = COMP_MASK << R_SHIFT;
    public static final int G_MASK = COMP_MASK << G_SHIFT;
    public static final int B_MASK = COMP_MASK << B_SHIFT;

    public static int setAlphaF(int color, float factor) {
        return (color & ~A_MASK) | ((int) (factor * (float) COMP_MASK) << A_SHIFT);
    }

    public static int setRedF(int color, float factor) {
        return (color & ~R_MASK) | ((int) (factor * (float) COMP_MASK) << R_SHIFT);
    }

    public static int setGreenF(int color, float factor) {
        return (color & ~G_MASK) | ((int) (factor * (float) COMP_MASK) << G_SHIFT);
    }

    public static int setBlueF(int color, float factor) {
        return (color & ~B_MASK) | ((int) (factor * (float) COMP_MASK) << B_SHIFT);
    }

    public static int setAlpha(int color, int alpha) {
        return (color & ~A_MASK) | ((alpha & COMP_MASK) << A_SHIFT);
    }

    public static int setRed(int color, int red) {
        return (color & ~R_MASK) | ((red & COMP_MASK) << R_SHIFT);
    }

    public static int setGreen(int color, int green) {
        return (color & ~G_MASK) | ((green & COMP_MASK) << G_SHIFT);
    }

    public static int setBlue(int color, int blue) {
        return (color & ~B_MASK) | ((blue & COMP_MASK) << B_SHIFT);
    }

    private ColorUtils() {
        throw new UnsupportedOperationException();
    }
}
