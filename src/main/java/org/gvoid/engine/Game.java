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

package org.gvoid.engine;

import org.gvoid.engine.interfaces.PointTransform;
import java.awt.*;
import java.awt.geom.AffineTransform;
import org.gvoid.engine.math.Point2;

public class Game {
    public final PointTransform transform;

    public final Point2 offset;
    public final Point2 scale;

    public double zoom;

    public final Point2 absSize;
    public final Point2 size;

    public final Cursor cursor;
    public final Keys keys;

    private Component component;

    private long lastUpdate;
    private float deltaTime;
    private boolean updated;
    private boolean handled;

    public Game() {
        this.transform = this::transform;

        this.offset = new Point2(0d, 0d);
        this.scale = new Point2(1d, 1d);

        this.zoom = 1d;

        this.absSize = new Point2(0d, 0d);
        this.size = new Point2(0d, 0d);

        this.cursor = new Cursor(transform, false);
        this.keys = new Keys();

        this.component = null;

        this.lastUpdate = -1;
        this.deltaTime = -1f;
        this.updated = false;
        this.handled = false;
    }

    protected final void setOffset(Point2 offset) {
        if (offset != null)
            offset.applyTo(this.offset);
        else
            this.offset.set(0d, 0d);
    }

    protected final void setRelScale(Point2 scale) {
        if (scale != null)
            scale.applyTo(this.scale);
        else
            this.scale.set(1d, 1d);
    }

    protected boolean isAttached() {
        return component != null;
    }

    public final void detach() {
        try {
            if (this.component != null)
                onDetachFrom(this.component);
        } catch (Throwable tr) {
            tr.printStackTrace();
        }

        if (this.component != null) {
            this.cursor.detach();
            this.keys.detach();
        }
        this.component = null;

        this.absSize.set(0d, 0d);
        this.size.set(0d, 0d);

        this.lastUpdate = -1;
        this.deltaTime = -1f;
        this.updated = false;
    }

    public final void attachTo(Component component) {
        detach();
        this.component = component;
        if (this.component != null) {
            this.cursor.attachTo(component);
            this.keys.attachTo(component);
        }

        updateSize();

        this.lastUpdate = -1;
        this.deltaTime = -1f;
        this.updated = false;

        try {
            if (this.component != null)
                onAttachTo(this.component);
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
    }

    public void transform(Point2 point) {
        if (point == null)
            return;

        double anchorX = (absSize.x - absSize.x * zoom) / 2d;
        double anchorY = (absSize.y - absSize.y * zoom) / 2d;

        point.sub(offset);
        point.sub(anchorX, anchorY);
        point.mul(scale);
        point.div(zoom, zoom);
    }

    protected final void updateSize() {
        if (component == null) {
            absSize.set(0d, 0d);
            size.set(0d, 0d);
            return;
        }

        double pw, ph;
        pw = size.x;
        ph = size.y;

        absSize.set(
                component.getWidth(),
                component.getHeight()
        );
        absSize.applyTo(size);
        size.mul(scale);

        if (pw != size.x || ph != size.y) {
            onResize(size);
        }
    }

    public final void update() {
        try {
            if (!isAttached())
                return;
        } catch (Throwable tr) {
            tr.printStackTrace();
        }

        updateSize();

        long time = System.currentTimeMillis();
        long delta = lastUpdate >= 0L ? Math.max(time - lastUpdate, 0L) : 0L;
        deltaTime = (float) delta / 1000f;
        lastUpdate = time;

        if (!handled) {
            try {
                cursor.update();
                keys.update();
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
            handled = true;
        }

        try {
            onUpdate(deltaTime);
        } catch (Throwable tr) {
            tr.printStackTrace();
        } finally {
            updated = true;
        }
    }

    public final void render(Graphics2D graphics) {
        try {
            if (graphics == null || !updated)
                return;

            try {
                if (!isAttached())
                    return;
            } catch (Throwable tr) {
                tr.printStackTrace();
            }

            updateSize();

            try {
                onPreRender(graphics);
            } catch (Throwable tr) {
                tr.printStackTrace();
            }

            AffineTransform transform = null;
            try {
                transform = graphics.getTransform();

                double anchorX = (absSize.x - absSize.x * zoom) / 2d;
                double anchorY = (absSize.y - absSize.y * zoom) / 2d;

                graphics.translate(offset.x + anchorX, offset.y + anchorY);
                graphics.scale(1d / scale.x * zoom, 1d / scale.y * zoom);

                try {
                    onRender(graphics);
                } catch (Throwable tr) {
                    tr.printStackTrace();
                }

                try {
                    onRenderOverlay(graphics);
                } catch (Throwable tr) {
                    tr.printStackTrace();
                }
            } catch (Throwable tr) {
                tr.printStackTrace();
            } finally {
                try {
                    graphics.setTransform(transform);
                } catch (Throwable tr) {
                    tr.printStackTrace();
                }
            }

            try {
                onPostRender(graphics);
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        } finally {
            handled = false;
        }
    }

    protected final Component getComponent() {
        return component;
    }

    public final long getLastUpdate() {
        return lastUpdate;
    }

    public final float getDeltaTime() {
        return deltaTime;
    }

    protected void onResize(Point2 newSize) {
    }

    protected void onUpdate(float deltaTime) {
    }

    protected void onRender(Graphics2D graphics) {
    }

    protected void onRenderOverlay(Graphics2D graphics) {
    }

    protected void onPreRender(Graphics2D graphics) {
    }

    protected void onPostRender(Graphics2D graphics) {
    }

    protected void onAttachTo(Component component) {
    }

    protected void onDetachFrom(Component component) {
    }
}
