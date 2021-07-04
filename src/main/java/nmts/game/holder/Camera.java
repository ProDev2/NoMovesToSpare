package nmts.game.holder;

import org.gvoid.engine.math.Maths;

public class Camera {
    public static float MOVE_T = 0.3f;
    public static float SCALE_T = 0.3f;
    public static float RANGE_T = 0.3f;

    public float x, y;
    public float scale;
    public float range;
    public float rangeSq;

    private float dx, dy;
    private float ds, dr;

    public Camera() {
        y = x = 0.0f;
        dy = dx = 0.0f;
        ds = scale = 1.0f;
        dr = range = Float.MAX_VALUE;
        rangeSq = Float.MAX_VALUE;
    }

    public void setDestPos(float x, float y, boolean force) {
        dx = x;
        dy = y;
        if (force) {
            this.x = x;
            this.y = y;
        }
    }

    public void setDestScale(float scale, boolean force) {
        ds = scale;
        if (force) {
            this.scale = scale;
        }
    }

    public void setDestRange(float range, boolean force) {
        dr = range;
        if (force) {
            this.range = range;
            this.rangeSq = range * range;
        }
    }

    public void update(float deltaTime) {
        float moveDelta = deltaTime / MOVE_T;
        float scaleDelta = deltaTime / SCALE_T;
        float rangeDelta = deltaTime / RANGE_T;

        x = Maths.lerp(x, dx, moveDelta);
        y = Maths.lerp(y, dy, moveDelta);

        scale = Maths.lerp(scale, ds, scaleDelta);

        range = Maths.lerp(range, dr, rangeDelta);
        rangeSq = range * range;
    }
}
