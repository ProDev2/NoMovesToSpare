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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import org.gvoid.engine.math.Point2;

public class Cursor implements MouseListener, MouseMotionListener, MouseWheelListener {
    public PointTransform transform;
    public boolean invertMouse;

    private Component component;

    public final Point2 absPos;
    public final Point2 pos;

    public boolean leftPressed = false;
    public boolean middlePressed = false;
    public boolean rightPressed = false;

    private boolean actionSet = false;
    private boolean leftClicked = false;
    private boolean middleClicked = false;
    private boolean rightClicked = false;
    private int wheelMovedBy = 0;

    public Cursor() {
        this(null, false);
    }

    public Cursor(boolean invertMouse) {
        this(null, invertMouse);
    }

    public Cursor(PointTransform transform, boolean invertMouse) {
        this.transform = transform;
        this.invertMouse = invertMouse;

        this.absPos = new Point2(-1d, -1d);
        this.pos = new Point2(-1d, -1d);
    }

    public void attachTo(Component component) {
        detach();

        this.component = component;

        if (component != null) {
            component.addMouseListener(this);
            component.addMouseMotionListener(this);
            component.addMouseWheelListener(this);
        }
    }

    public void detach() {
        if (component == null)
            return;

        component.removeMouseListener(this);
        component.removeMouseMotionListener(this);
        component.removeMouseWheelListener(this);

        component = null;
    }

    public boolean isInBox(double left,
                        double top,
                        double right,
                        double bottom) {
        return pos.x >= left
                && pos.y >= top
                && pos.x <= right
                && pos.y <= bottom;
    }

    public boolean isIn(double x,
                        double y,
                        double width,
                        double height) {
        return pos.x >= x
                && pos.y >= y
                && pos.x < x + width
                && pos.y < y + height;
    }

    public boolean leftClick() {
        if (!leftClicked)
            return false;

        leftClicked = false;
        return true;
    }

    public boolean middleClick() {
        if (!middleClicked)
            return false;

        middleClicked = false;
        return true;
    }

    public boolean rightClick() {
        if (!rightClicked)
            return false;

        rightClicked = false;
        return true;
    }

    public boolean wheelMoved() {
        boolean moved = wheelMovedBy != 0;
        wheelMovedBy = 0;
        return moved;
    }

    public int wheelMovedBy() {
        int movedBy = wheelMovedBy;
        wheelMovedBy = 0;
        return movedBy;
    }

    private void setCoordinates(Point point) {
        if (point == null)
            return;

        absPos.set(point.getX(), point.getY());
        absPos.applyTo(pos);

        if (transform != null) {
            try {
                transform.transform(pos);
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        }
    }

    public void handled() {
        actionSet = false;
        update();
    }

    public void update() {
        leftClicked &= actionSet;
        middleClicked &= actionSet;
        rightClicked &= actionSet;
        wheelMovedBy = actionSet ? wheelMovedBy : 0;
        actionSet = false;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        setCoordinates(event.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        setCoordinates(event.getPoint());
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (event.getButton() == (!invertMouse ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3)) {
            this.leftPressed = true;
            this.leftClicked = true;
            this.actionSet = true;
        } else if (event.getButton() == (!invertMouse ? MouseEvent.BUTTON3 : MouseEvent.BUTTON1)) {
            this.rightPressed = true;
            this.rightClicked = true;
            this.actionSet = true;
        } else if (event.getButton() == MouseEvent.BUTTON2) {
            this.middlePressed = true;
            this.middleClicked = true;
            this.actionSet = true;
        }

        setCoordinates(event.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (event.getButton() == (!invertMouse ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3)) {
            this.leftPressed = false;
        } else if (event.getButton() == (!invertMouse ? MouseEvent.BUTTON3 : MouseEvent.BUTTON1)) {
            this.rightPressed = false;
        } else if (event.getButton() == MouseEvent.BUTTON2) {
            this.middlePressed = false;
        }

        setCoordinates(event.getPoint());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        int wheelRotation = event.getWheelRotation();
        if (wheelRotation != 0) {
            this.wheelMovedBy += wheelRotation;
            this.actionSet = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }
}
