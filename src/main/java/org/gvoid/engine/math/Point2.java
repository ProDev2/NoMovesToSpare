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
        "UnusedReturnValue",
        "SuspiciousNameCombination"
})
public final class Point2 {
    public double x, y;

    public Point2() {
        this.x = 0d;
        this.y = 0d;
    }

    public Point2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Point2(Point2 src) {
        this();

        if (src != null)
            src.applyTo(this);
    }

    public void applyTo(Point2 target) {
        if (target == null)
            return;

        target.x = x;
        target.y = y;
    }
	
	public Point2 copy() {
		return new Point2(this);
	}

    public int xInt() {
        return (int) x;
    }

    public int yInt() {
        return (int) y;
    }

    public float xFloat() {
        return (float) x;
    }

    public float yFloat() {
        return (float) y;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }
	
    public Point2 setZero() {
        this.x = 0d;
        this.y = 0d;
        return this;
    }
	
	public Point2 set(Point2 src) {
        this.x = src.x;
        this.y = src.y;
        return this;
    }
	
    public Point2 set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Point2 add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Point2 sub(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Point2 mul(double x, double y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Point2 div(double x, double y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    public Point2 pow(double x, double y) {
        this.x = Math.pow(this.x, x);
        this.y = Math.pow(this.y, y);
        return this;
    }
	
	public Point2 set(double scalar) {
        this.x = scalar;
        this.y = scalar;
        return this;
    }

    public Point2 add(double scalar) {
        this.x += scalar;
        this.y += scalar;
        return this;
    }

    public Point2 sub(double scalar) {
        this.x -= scalar;
        this.y -= scalar;
        return this;
    }

    public Point2 mul(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public Point2 div(double scalar) {
        this.x /= scalar;
        this.y /= scalar;
        return this;
    }

    public Point2 pow(double scalar) {
        this.x = Math.pow(this.x, scalar);
        this.y = Math.pow(this.y, scalar);
        return this;
    }
	
    public Point2 add(Point2 v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Point2 sub(Point2 v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Point2 mul(Point2 v) {
        this.x *= v.x;
        this.y *= v.y;
        return this;
    }

    public Point2 div(Point2 v) {
        this.x /= v.x;
        this.y /= v.y;
        return this;
    }

    public Point2 pow(Point2 v) {
        this.x = Math.pow(this.x, v.x);
        this.y = Math.pow(this.y, v.y);
        return this;
    }
	
	public Point2 addPoints(Point2 v1, Point2 v2) {
        this.x = v1.x + v2.x;
        this.y = v1.y + v2.y;
        return this;
    }

    public Point2 subPoints(Point2 v1, Point2 v2) {
        this.x = v1.x - v2.x;
        this.y = v1.y - v2.y;
        return this;
    }

    public Point2 mulPoints(Point2 v1, Point2 v2) {
        this.x = v1.x * v2.x;
        this.y = v1.y * v2.y;
        return this;
    }

    public Point2 divPoints(Point2 v1, Point2 v2) {
        this.x = v1.x / v2.x;
        this.y = v1.y / v2.y;
        return this;
    }

    public Point2 powPoints(Point2 v1, Point2 v2) {
        this.x = Math.pow(v1.x, v2.x);
        this.y = Math.pow(v1.y, v2.y);
        return this;
    }

    public Point2 addTo(Point2 v) {
        v.x += this.x;
        v.y += this.y;
        return this;
    }

    public Point2 subFrom(Point2 v) {
        v.x -= this.x;
        v.y -= this.y;
        return this;
    }

    public Point2 mulTo(Point2 v) {
        v.x *= this.x;
        v.y *= this.y;
        return this;
    }

    public Point2 divFrom(Point2 v) {
        v.x /= this.x;
        v.y /= this.y;
        return this;
    }

    public Point2 powSelf(int t) {
        double x = this.x, rx = 1d;
        double y = this.y, ry = 1d;
        for (int i = 0; i < t; i++) {
            rx *= x;
            ry *= y;
        }
        this.x = rx;
        this.y = ry;
        return this;
    }

    public Point2 mirrorXY() {
        double t = this.x;
        this.x = this.y;
        this.y = t;
        return this;
    }

    public Point2 mirrorX() {
        this.y *= -1d;
        return this;
    }

    public Point2 mirrorY() {
        this.x *= -1d;
        return this;
    }

    public Point2 rotQuartCW() {
        double t = this.x;
        this.x = this.y;
        this.y = -t;
        return this;
    }

    public Point2 rotQuartCCW() {
        double t = this.x;
        this.x = -this.y;
        this.y = t;
        return this;
    }

    public Point2 round() {
        this.x = Math.round(this.x);
        this.y = Math.round(this.y);
        return this;
    }
	
	public Point2 negate() {
		this.x *= -1d;
		this.y *= -1d;
		return this;
	}
	
	public Point2 negatePoint(Point2 v) {
		return set(v).negate();
	}

    public Point2 normalize() {
        double length = length();
        if (length != 0d) {
            x /= length;
            y /= length;
        }
        return this;
    }
	
	public double lengthSq() {
		return x * x + y * y;
	}

    public double length() {
        return Math.pow(
			x * x + y * y, 
			0.5d
        );
    }
	
	public Point2 setLength(double length) {
		return normalize().mul(length);
	}
	
	public Point2 lerp(Point2 v, double alpha) {
		x += (v.x - x) * alpha;
		y += (v.y - y) * alpha;
		
		return this;
	}

	public Point2 lerpPoints(Point2 v1, Point2 v2, double alpha) {
		x = v1.x + (v2.x - v1.x) * alpha;
		y = v1.y + (v2.y - v1.y) * alpha;
		
		return this;
	}
	
	/*
    public Point2 setLengthInDegrees(double length, double angleInDegrees) {
        setLength(length, Math.toRadians(angleInDegrees));
        return this;
    }

    public Point2 setLength(double length) {
        setLength(length, angle());
        return this;
    }

    public Point2 setLength(double length, double angle) {
        this.x = length * Math.cos(angle);
        this.y = length * Math.sin(angle);
        return this;
    }

    public double angleInDegrees() {
        return Math.toDegrees(angle());
    }

    public double angle() {
        return Math.atan2(y, x);
    }

    public Point2 rotateToDegrees(double angleInDegrees) {
        rotateTo(Math.toRadians(angleInDegrees));
        return this;
    }

    public Point2 rotateTo(double angle) {
        double length = length();
        this.x = length * Math.cos(angle);
        this.y = length * Math.sin(angle);
        return this;
    }

    public Point2 rotateByDegrees(double angleInDegrees) {
        rotateBy(Math.toRadians(angleInDegrees));
        return this;
    }

    public Point2 rotateBy(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double rx = x * cos - y * sin;
        y = x * sin + y * cos;
        x = rx;
        return this;
    }
	*/
	
	public Point2 fromArray(double[] array, int offset) {
		x = array[ offset + 0 ];
		y = array[ offset + 1 ];
		
		return this;
	}

	public double[] toArray(double[] array, int offset) {
		if (array == null) {
			array = new double[2];
			offset = 0;
		}

		array[ offset + 0 ] = x;
		array[ offset + 1 ] = y;
		
		return array;
	}
	
	public boolean isEqualTo(Point2 other) {
		return other != null && x == other.x && y == other.y;
	}

    @Override
    public String toString() {
        return "[x=" + f(x) + ", y=" + f(y) + "]" + "[len=" + f(length()) + ", len_sq=" + f(lengthSq()) + "]";
    }

    public static String f(double n) {
        n = (double) Math.round(n * 10000d) / 10000d;
        return n % 1d == 0d
               ? Long.toString((long) n)
               : Double.toString(n);
    }
	
	public Point2 getNormalized(Point2 v) {
        return v.copy().normalize();
    }

	public Point2 getNegated(Point2 v) {
		return v.copy().negate();
	}

    public static double dot(Point2 v1, Point2 v2) {
        return dot(v1.x, v1.y, v2.x, v2.y);
    }

    public static double dot(double x1, double y1, double x2, double y2) {
        return x1 * x2 + y1 * y2;
    }

    public static double dotRelativized(Point2 v1, Point2 v2) {
        return dotRelativized(v1.x, v1.y, v2.x, v2.y);
    }

    public static double dotRelativized(double x1, double y1, double x2, double y2) {
        double l1 = Math.pow(x1 * x1 + y1 * y1, 0.5d);
        double l2 = Math.pow(x2 * x2 + y2 * y2, 0.5d);

        if (l1 != 0d) {
            x1 /= l1;
            y1 /= l1;
        }
        if (l2 != 0d) {
            x2 /= l2;
            y2 /= l2;
        }

        return x1 * x2 + y1 * y2;
    }
	
	public static Point2 ortho(Point2 v1, Point2 v2) {
        return ortho(v1.x, v1.y, v2.x, v2.y);
    }

    public static Point2 ortho(double x1, double y1, double x2, double y2) {
        double rx = y1 * y1 * x2 - x1 * y2 * y1;
        double ry = x1 * x1 * y2 - y1 * x2 * x1;
        
        return new Point2(rx, ry);
    }
	
	public static Point2 orthoRelativized(Point2 v1, Point2 v2) {
        return orthoRelativized(v1.x, v1.y, v2.x, v2.y);
    }

    public static Point2 orthoRelativized(double x1, double y1, double x2, double y2) {
		double l1 = Math.pow(x1 * x1 + y1 * y1, 0.5d);
        double l2 = Math.pow(x2 * x2 + y2 * y2, 0.5d);

        if (l1 != 0d) {
            x1 /= l1;
            y1 /= l1;
        }
        if (l2 != 0d) {
            x2 /= l2;
            y2 /= l2;
        }

        double rx = y1 * y1 * x2 - x1 * y2 * y1;
        double ry = x1 * x1 * y2 - y1 * x2 * x1;

        return new Point2(rx, ry);
    }

    public static Point2 orthoNormalized(Point2 v1, Point2 v2) {
        return orthoNormalized(v1.x, v1.y, v2.x, v2.y);
    }

    public static Point2 orthoNormalized(double x1, double y1, double x2, double y2) {
        double rx = y1 * y1 * x2 - x1 * y2 * y1;
        double ry = x1 * x1 * y2 - y1 * x2 * x1;

        double rl = Math.pow(rx * rx + ry * ry, 0.5d);
        if (rl != 0d) {
            rx /= rl;
            ry /= rl;
        }

        return new Point2(rx, ry);
    }

    public static double projectOnDir(double angle, double x0, double y0, double x1, double y1) {
	    return Math.cos(angle) * (x1 - x0) + Math.sin(angle) * (y1 - y0);
    }

    public static double projectOnDir(double angle, double dx, double dy) {
	    return Math.cos(angle) * dx + Math.sin(angle) * dy;
    }

    public static Point2 projectOnPlane(Point2 normal, Point2 point, Point2 target) {
        return projectOnPlane(normal.x, normal.y, point.x, point.y, target);
    }

    public static Point2 projectOnPlane(double nx, double ny, double px, double py, Point2 t) {
        if (t == null) t = new Point2();

        double d = nx * px + ny * py;
        t.x = nx * -d + px;
        t.y = ny * -d + py;

        return t;
    }

    public static Point2 reflectOnPlane(Point2 normal, Point2 point, Point2 target) {
        return reflectOnPlane(normal.x, normal.y, point.x, point.y, target);
    }

    public static Point2 reflectOnPlane(double nx, double ny, double px, double py, Point2 t) {
        if (t == null) t = new Point2();

        double d = nx * px + ny * py;
        t.x = px - nx * d * 2d;
        t.y = py - ny * d * 2d;

        return t;
    }

    public static Point2 projectOnVector(Point2 normal, Point2 point, Point2 target) {
        return projectOnVector(normal.x, normal.y, point.x, point.y, target);
    }

    public static Point2 projectOnVector(double nx, double ny, double px, double py, Point2 t) {
        if (t == null) t = new Point2();

        double d = nx * nx + ny * ny;
        double p = nx * px + ny * py;
        double s = d != 0d ? p / d : 0d;

        t.x = nx * s;
        t.y = ny * s;

        return t;
    }

    public static double distanceToPlane(double nx, double ny, double px, double py) {
        return nx * px + ny * py;
    }

    public static boolean intersectDir(Point2 p1, Point2 n1, Point2 p2, Point2 n2, Point2 target) {
        return intersectDir(p1.x, p1.y, n1.x, n1.y, p2.x, p2.y, n2.x, n2.y, target);
    }

    public static boolean intersectDir(double p1x, double p1y, double n1x, double n1y, double p2x, double p2y, double n2x, double n2y, Point2 t) {
        double t1 = (p2x - p1x) * n2y - n2x * (p2y - p1y);
        double t2 = n1x * n2y - n2x * n1y;

        if (t2 == 0d) return false;

        t.x = p1x + n1x * t1 / t2;
        t.y = p1y + n1y * t1 / t2;

        return true;
    }

    public static boolean intersectPlane(Point2 p1, Point2 n1, Point2 p2, Point2 n2, Point2 target) {
        return intersectPlane(p1.x, p1.y, n1.x, n1.y, p2.x, p2.y, n2.x, n2.y, target);
    }

    public static boolean intersectPlane(double p1x, double p1y, double n1x, double n1y, double p2x, double p2y, double n2x, double n2y, Point2 t) {
        double t1 = (p2x - p1x) * n2x + (p2y - p1y) * n2y;
        double t2 = n1y * n2x - n1x * n2y;

        if (t2 == 0d) return false;

        t.x = p1x + n1y * t1 / t2;
        t.y = p1y - n1x * t1 / t2;

        return true;
    }
}
