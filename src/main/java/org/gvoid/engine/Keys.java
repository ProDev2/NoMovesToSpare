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

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Keys implements KeyListener {
    private static final int RELEASED = 0x1 << 1;
    private static final int USED = 0x1 << 2;

    private final LinkedList<Integer> pressedKeys;

    private final Map<Integer, State> pressMap;

    private Component component;

    public Keys() {
        this.pressedKeys = new LinkedList<>();

        this.pressMap = new HashMap<>();
    }

    public void attachTo(Component component) {
        detach();

        this.component = component;

        if (component != null) {
            component.addKeyListener(this);
        }
    }

    public void detach() {
        if (component == null)
            return;

        component.removeKeyListener(this);

        component = null;
    }

    public void update() {
        synchronized (pressMap) {
            if (pressMap.size() > 0) {
                pressMap.values().removeIf(state -> state == null || state.isHandled(USED));
            }
        }
    }

    public int getPressedCount() {
        synchronized (pressedKeys) {
            return pressedKeys.size();
        }
    }

    public int getPos(int keyCode) {
        synchronized (pressedKeys) {
            return pressedKeys.indexOf(keyCode);
        }
    }

    public boolean isPressed(int keyCode) {
        synchronized (pressedKeys) {
            return pressedKeys.contains(keyCode);
        }
    }

    public boolean isPressed(int[] keyCodes) {
        if (keyCodes == null)
            return false;

        synchronized (pressedKeys) {
            for (Integer pressedKey : pressedKeys) {
                if (pressedKey == null)
                    continue;
                for (int keyCode : keyCodes) {
                    if (pressedKey == keyCode)
                        return true;
                }
            }
            return false;
        }
    }

    public boolean handle(int keyCode) {
        State state;
        synchronized (pressMap) {
            state = pressMap.get(keyCode);
        }
        if (state == null || state.hasFlag(USED))
            return false;
        state.addFlag(USED);
        return true;
    }

    public boolean handle(int[] keyCodes) {
        if (keyCodes == null)
            return false;

        synchronized (pressMap) {
            if (pressMap.size() <= 0)
                return false;

            boolean handle = false;
            for (int keyCode : keyCodes) {
                State state = null;
                for (Map.Entry<Integer, State> pressEntry : pressMap.entrySet()) {
                    Integer pressCode = pressEntry.getKey();
                    if (pressCode != keyCode)
                        continue;

                    state = pressEntry.getValue();
                }
                if (state == null)
                    continue;

                handle |= !state.hasFlag(USED);
                state.addFlag(USED);
            }
            return handle;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        synchronized (pressedKeys) {
            if (!pressedKeys.contains(keyCode))
                pressedKeys.addLast(keyCode);
        }
        synchronized (pressMap) {
            if (!pressMap.containsKey(keyCode))
                pressMap.put(keyCode, new State());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        synchronized (pressedKeys) {
            pressedKeys.remove((Object) keyCode);
        }
        synchronized (pressMap) {
            State state = pressMap.get(keyCode);
            if (state != null)
                state.addFlag(RELEASED);
        }
    }

    private static class State {
        private static final int NONE = 0x0;
        private static final int HANDLED = 0x1 << 31;

        private int state;

        public State() {
            this.state = NONE;
        }

        public State(int state) {
            this.state = state;
        }

        public synchronized boolean isHandled(int ignoreFlag) {
            if ((state & HANDLED) != 0)
                return true;
            if ((state & ~NONE & ~ignoreFlag) != 0)
                state |= HANDLED;
            return false;
        }

        public synchronized boolean hasFlag(int flag) {
            return (state & flag) != 0;
        }

        public synchronized void addFlag(int flag) {
            state |= flag;
        }

        public synchronized void removeFlag(int flag) {
            state &= ~flag;
        }
    }
}
