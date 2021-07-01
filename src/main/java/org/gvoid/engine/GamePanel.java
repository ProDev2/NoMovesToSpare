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
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

@SuppressWarnings("UnusedReturnValue")
public class GamePanel extends JPanel {
    private Game game;
    private long fps, cps;
    private boolean antialias;

    private Timer timer;

    private TimerTask updateTask;
    private TimerTask paintTask;

    private long lastRepaint = -1L;

    public GamePanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        init();
    }

    public GamePanel(LayoutManager layout) {
        super(layout);
        init();
    }

    public GamePanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        init();
    }

    public GamePanel() {
        super();
        init();
    }

    private void init() {
        try {
            setFocusable(true);
            requestFocus();
        } catch (Throwable tr) {
            tr.printStackTrace();
        }

        fps = -1L;
        cps = 30L;
        antialias = false;

        update();
    }

    public void setGame(Game game) {
        if (this.game != null) {
            try {
                this.game.detach();
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        }
        this.game = game;
        if (this.game != null) {
            try {
                this.game.attachTo(this);
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        }

        update();
    }

    public void setFps(long fps) {
        this.fps = fps;

        try {
            repaint();
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
    }

    public void setCps(long cps) {
        this.cps = cps;
        update();
    }

    public void setAntialias(boolean antialias) {
        this.antialias = antialias;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        update();
    }

    private boolean stopUpdate() {
        Timer timer = this.timer;
        if (timer != null) {
            try {
                if (updateTask != null) {
                    updateTask.cancel();
                    updateTask = null;
                }
            } catch (Throwable tr) {
                tr.printStackTrace();
                return false;
            }
            try {
                if (paintTask != null) {
                    paintTask.cancel();
                    paintTask = null;
                }
            } catch (Throwable tr) {
                tr.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean update() {
        if (!stopUpdate())
            return false;
        Timer timer = this.timer;
        if (game != null && timer == null) {
            try {
                this.timer = timer = new Timer(true);
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        }

        if (game == null || cps <= 0 || timer == null || !isEnabled()) {
            this.timer = null;
            try {
                if (timer != null) {
                    timer.cancel();
                }
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
            return false;
        }

        try {
            updateTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        Game game = GamePanel.this.game;
                        if (game == null) {
                            stopUpdate();
                            return;
                        }

                        onUpdate();
                        game.update();
                    } catch (Throwable tr) {
                        tr.printStackTrace();
                    }
                }
            };
            timer.schedule(updateTask, 50L, 1000L / cps);

            try {
                repaint();
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
            return true;
        } catch (Throwable tr) {
            tr.printStackTrace();
            return false;
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (graphics == null) return;
        if (!(graphics instanceof Graphics2D)) return;

        Game game = this.game;
        if (game == null)
            return;

        try {
            if (!isEnabled())
                return;
        } catch (Throwable tr) {
            tr.printStackTrace();
        }

        try {
            try {
                if (antialias)
                    ((Graphics2D) graphics).setRenderingHint(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON
                    );
            } catch (Throwable ignored) {
            }

            Graphics2D graphics2D = (Graphics2D) graphics;
            onPreRender(graphics2D);
            game.render(graphics2D);
            onPostRender(graphics2D);
        } catch (Throwable tr) {
            tr.printStackTrace();
        } finally {
            long time = System.currentTimeMillis();
            long delta = lastRepaint >= 0L ? Math.max(time - lastRepaint, 0L) : 0L;
            long remTime = fps > 0L ? Math.max((1000L / fps) - delta, 0L) : 0L;
            lastRepaint = time;

            if (remTime <= 0) {
                repaint();
            } else {
                try {
                    if (paintTask != null)
                        paintTask.cancel();
                } catch (Throwable tr) {
                    tr.printStackTrace();
                }

                try {
                    paintTask = new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                repaint();
                            } catch (Throwable tr) {
                                tr.printStackTrace();
                            }
                        }
                    };
                    Timer timer = this.timer;
                    if (timer != null) {
                        timer.schedule(paintTask, Math.max(remTime - 1, 0));
                    }
                } catch (Throwable tr) {
                    tr.printStackTrace();

                    repaint();
                }
            }
        }
    }

    protected void onUpdate() {
    }
    protected void onPreRender(Graphics2D graphics) {
    }
    protected void onPostRender(Graphics2D graphics) {
    }
}
