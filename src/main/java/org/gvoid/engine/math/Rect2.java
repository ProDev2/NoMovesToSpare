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

@SuppressWarnings({
        "unused",
        "UnusedReturnValue"
})
public class Rect2 {
    public double left, top, right, bottom;

    public Rect2() {
        this.left = 0d;
        this.top = 0d;
        this.right = 0d;
        this.bottom = 0d;
    }

    public Rect2(double left, double top, double right, double bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Rect2(Rect2 src) {
        this();

        if (src != null)
            src.applyTo(this);
    }

    public void applyTo(Rect2 target) {
        if (target == null)
            return;

        target.left = left;
        target.top = top;
        target.right = right;
        target.bottom = bottom;
    }

    public Rect2 copy() {
        return new Rect2(this);
    }

    public double left() {
        return Math.min(left, right);
    }

    public double top() {
        return Math.min(top, bottom);
    }

    public double right() {
        return Math.max(left, right);
    }

    public double bottom() {
        return Math.max(top, bottom);
    }

    public double x() {
        return left();
    }

    public double y() {
        return top();
    }

    public double width() {
        return right() - left();
    }

    public double height() {
        return bottom() - top();
    }

    public double centerX() {
        return (left + right) / 2f;
    }

    public double centerY() {
        return (top + bottom) / 2f;
    }

    public Rect2 set(double x, double y) {
        double w = right - left;
        double h = bottom - top;
        left = x;
        top = y;
        right = x + w;
        bottom = y + h;
        return this;
    }

    public Rect2 set(double x, double y, double width, double height) {
        left = x;
        top = y;
        right = x + width;
        bottom = y + height;
        return this;
    }

    public Rect2 setX(double x) {
        double w = right - left;
        left = x;
        right = x + w;
        return this;
    }

    public Rect2 setY(double y) {
        double h = bottom - top;
        top = y;
        bottom = y + h;
        return this;
    }

    public Rect2 setWidth(double width) {
        right = left + width;
        return this;
    }

    public Rect2 setHeight(double height) {
        bottom = top + height;
        return this;
    }

    public Rect2 moveBy(double dx, double dy) {
        left += dx;
        top += dy;
        right += dx;
        bottom += dy;
        return this;
    }

    public Rect2 resizeBy(double dw, double dh) {
        resizeBy(dw, dh, true);
        return this;
    }
    
    public Rect2 resizeBy(double dw, double dh, boolean restrict) {
        if (restrict) {
            double d;
            dw = (d = left - right) <= 0d ? Math.max(d, dw) : Math.min(d, dw);
            dh = (d = top - bottom) <= 0d ? Math.max(d, dh) : Math.min(d, dh);
        }

        right += dw;
        bottom += dh;
        return this;
    }

    public boolean contains(Point2 p) {
        if (p == null)
            return false;

        return left() <= p.x
                && top() <= p.y
                && right() >= p.x
                && bottom() >= p.y;
    }

    public boolean intersects(Rect2 r) {
        return intersects(r, true);
    }

    public boolean intersects(Rect2 r, boolean orTouches) {
        if (r == null)
            return false;

        if (orTouches) {
            return left() <= r.right()
                    && right() >= r.left()
                    && top() <= r.bottom()
                    && bottom() >= r.top();
        } else {
            return left() < r.right()
                    && right() > r.left()
                    && top() < r.bottom()
                    && bottom() > r.top();
        }
    }
}
