package nmts.game.screen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.InputStream;
import nmts.game.holder.Camera;
import nmts.game.holder.Maze;
import nmts.game.holder.MazeCollider;
import nmts.game.holder.Player;
import nmts.game.time.StopWatch;
import org.gvoid.engine.Game;
import org.gvoid.engine.math.Maths;

import static nmts.game.builder.MazeBuilder.WB;
import static nmts.game.builder.MazeBuilder.WL;
import static nmts.game.builder.MazeBuilder.WR;
import static nmts.game.builder.MazeBuilder.WT;

public class PlayScreen extends Game {
    public static final int FLAG_ROTATE_VIEW = 1;
    public static final int FLAG_INFINITE_MOVES = 2;
    public static final int FLAG_NO_WALL_TOUCH = 4;
    public static final int FLAG_INVERT_KEYS = 8;
    public static final int FLAG_HIDE_KEYS = 16;
    public static final int FLAG_SHOW_ALL = 32;
    public static final int FLAG_HIDE_MARKERS = 64;
    public static final int FLAG_HIDE_WALLS = 128;
    public static final int FLAG_SHOW_TIMER = 256;

    public static final int MIN_MAZE_SIZE_X = 3;
    public static final int MIN_MAZE_SIZE_Y = 3;

    public static final int MAX_MAZE_SIZE_X = 8;
    public static final int MAX_MAZE_SIZE_Y = 8;

    public static float VIEW_DISTANCE = 2.4f;
    public static float VIEW_SCALE = 4f;

    public static float SHOW_T = 0.9f;

    public static float BLINK_ST = 0.11f;
    public static float BLINK_T = 0.8f;

    public static float PULSE_T = 0.6f;

    public static float SHOW_KEY_T = 0.05f;

    public static final Color PULSE_OFF = new Color(0.6f, 0f, 0f, 0.0f);
    public static final Color PULSE_ON = new Color(0.7f, 0f, 0f);

    public static final Color GRID_COLOR = Color.WHITE;
    public static final Color COLLISION_GRID_COLOR = new Color(0.8f, 1f, 1f);

    public static final Color KEY_NORMAL_COLOR = new Color(0.3f, 0.3f, 0.3f);
    public static final Color KEY_DIR_COLOR = new Color(0.7f, 0.7f, 0.7f);
    public static final Color KEY_PRESS_COLOR = new Color(0.98f, 0.98f, 0.98f);

    public static final String FONT_PATH = "/fonts/JetBrainsMono-Bold.ttf";
    public static final float FONT_SCALE = 0.22f;

    public static final Font FONT;

    static {
        Font font = null;
        try (InputStream in = PlayScreen.class.getResourceAsStream(FONT_PATH)) {
            if (in == null) throw new NullPointerException("Resource not found");
            font = Font.createFont(Font.TRUETYPE_FONT, in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FONT = font;
    }

    private final int flags;

    private final Maze maze;
    private final MazeCollider collider;
    private final Camera camera;
    private final Player player;

    private float show;
    private boolean change;

    private float blinkTime;
    private boolean blinkOn;
    private Color blinkColor;

    private float pulse, pulseI;
    private boolean pulseOn;

    private float showKey;

    private double angle;

    private Font lastFont;
    private float lastFontSize;

    private final StopWatch stopWatch;

    public PlayScreen() {
        this(0);
    }

    public PlayScreen(int flags) {
        this.flags = flags;

        maze = new Maze(null);
        maze.MAZE_WIDTH = MIN_MAZE_SIZE_X;
        maze.MAZE_HEIGHT = MIN_MAZE_SIZE_Y;

        maze.buildMaze();
        if ((flags & FLAG_INFINITE_MOVES) == 0) {
            maze.countDirs();
        }

        collider = new MazeCollider(20);

        camera = new Camera();
        if ((flags & FLAG_SHOW_ALL) == 0) {
            camera.setDestRange(VIEW_DISTANCE, true);
        }
        camera.setDestScale(VIEW_SCALE, true);

        player = new Player(maze, collider, camera);
        angle = player.angle;

        show = 0f;
        change = false;

        pulseI = pulse = 0f;
        pulseOn = false;

        blink(Color.GREEN);

        showKey = -1f;

        lastFont = null;
        lastFontSize = -1f;

        if ((flags & FLAG_SHOW_TIMER) != 0) {
            stopWatch = new StopWatch();
        } else stopWatch = null;
    }

    public void next(boolean ch) {
        if (show >= 0f) return;
        if (show > -1f) {
            show = Math.abs(show);
            show = Math.min(show, 1f);
            return;
        }
        show = 1f;
        change = ch;
    }

    public void reset(boolean change) {
        if (change) {
            maze.MAZE_WIDTH = Math.min(maze.MAZE_WIDTH + 2, MAX_MAZE_SIZE_X);
            maze.MAZE_HEIGHT = Math.min(maze.MAZE_HEIGHT + 2, MAX_MAZE_SIZE_Y);

            maze.buildMaze();
        }
        if ((flags & FLAG_INFINITE_MOVES) == 0) {
            maze.countDirs();
        }
        player.reset();
        angle = player.angle;

        if ((flags & FLAG_SHOW_TIMER) != 0 && change) {
            stopWatch.reset();
        }
    }

    public void blink(Color color) {
        if (color == null) {
            color = Color.BLACK;
        }
        if (blinkTime < 0f) {
            blinkTime = BLINK_T;
            blinkOn = true;
        }
        blinkColor = color;
    }

    public int rot(int dir, boolean invert) {
        if ((flags & FLAG_ROTATE_VIEW) != 0) {
            double rot = Math.toDegrees(angle) - 45d;
            while (rot < 0d) rot += 360d;
            int sec = (int) (rot / 90d);
            dir -= invert ? -sec : sec;
        }

        if ((flags & FLAG_INVERT_KEYS) != 0) {
            dir += invert ? -2 : 2;
        }

        return (dir %= 4) < 0 ? 4 + dir : dir;
    }

    public void setDir(int dir) {
        if (show >= -0.3f) return;
        dir = rot(dir, false);

        boolean wasDir = player.dir == dir;

        player.dir = dir;
        showKey = SHOW_KEY_T;

        if (wasDir) return;

        if ((flags & FLAG_INFINITE_MOVES) == 0) {
            int rem = --maze.dirs[dir];
            if (rem < 0) {
                blink(Color.ORANGE);
                next(false);
            }
        }
    }

    @Override
    protected void onUpdate(float deltaTime) {
        if (show > -1f) {
            float nds = Math.max(show - deltaTime / SHOW_T, -1f);
            if ((show >= 0f) != (nds >= 0f)) reset(change);
            show = nds;
        } else change = false;

        if (blinkTime >= 0f) {
            blinkOn = (int) (blinkTime / BLINK_ST) % 2 == 0;
            blinkTime = Math.max(blinkTime - deltaTime, -1f);
        } else blinkOn = false;

        pulse += (deltaTime / PULSE_T) * (pulseOn ? 1f : -1f);
        if (pulse < 0f || pulse > 1f) {
            pulseOn = !pulseOn;
            pulse = Maths.clamp(pulse, 0f, 1f);
        }
        pulseI = Maths.interpolateSlowInSlowOut(pulse);

        if (showKey >= 0f) showKey -= deltaTime;
        else showKey = -1f;

        camera.update(deltaTime);

        if (keys.isPressed(new int[] {KeyEvent.VK_A, KeyEvent.VK_LEFT})) setDir(0);
        else if (keys.isPressed(new int[] {KeyEvent.VK_W, KeyEvent.VK_UP})) setDir(1);
        else if (keys.isPressed(new int[] {KeyEvent.VK_D, KeyEvent.VK_RIGHT})) setDir(2);
        else if (keys.isPressed(new int[] {KeyEvent.VK_S, KeyEvent.VK_DOWN})) setDir(3);
        else if (showKey == -1f) angle = player.absAngle;

        if (keys.handle(new int[] {KeyEvent.VK_R, KeyEvent.VK_NUMPAD0, KeyEvent.VK_CONTROL})) {
            next(false);
        }

        int state = player.update(deltaTime);

        if (show < 0f) {
            boolean reset = false, change = false;
            switch (state) {
                case Player.AT_GOAL -> {
                    reset = true;
                    change = true;
                    blink(Color.GREEN);
                }
                case Player.OFF_PATH -> {
                    reset = true;
                    blink(Color.RED);
                }
                case Player.FROZEN -> {
                    reset = true;
                    blink(Color.CYAN);
                }
                case Player.AT_WALL -> {
                    if ((flags & FLAG_NO_WALL_TOUCH) == 0) break;
                    reset = true;
                    blink(Color.MAGENTA);
                }
            }

            if (reset) next(change);
        }
    }

    public Font getFont(Float size) {
        if (size == null) return lastFont;
        if (FONT == null) return null;
        Font font = lastFontSize == size ? lastFont : null;
        if (font == null) {
            font = FONT.deriveFont(size);
            lastFont = font;
            lastFontSize = size;
        }
        return font;
    }

    @Override
    protected void onRender(Graphics2D graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, size.xInt(), size.yInt());

        int[] d = maze.data;
        int[][] m = maze.maze;
        if (m == null) return;

        float scale = camera.scale;
        float ox = camera.x, oy = camera.y;
        float rangeSq = camera.rangeSq;

        float sd = Math.abs(show);
        sd = Math.min(sd, 1f);

        int mw = maze.width, mh = maze.height;

        float cx = size.xFloat() / 2f, cy = size.yFloat() / 2f;
        float cw = size.xFloat() / mw, ch = size.yFloat() / mh;

        float rgs = (float) (mw + mh) / 2f / scale;
        float gs = Math.min(cw, ch) * 0.9f * rgs;
        //float gs = Math.min(cw, ch) * 0.9f * scale;

        float ws = gs / 16f, ows = ws - 1f;
        float tws = ws * 2f, ews = tws - 2f;

        float sw = ws / 2f, tsw = sw * 2f;
        int wcr = (int) (tws * 0.9f);
        int fcr = (int) (tws * 0.3f);

        float aox = ox * gs, aoy = oy * gs;

        //float left = cx - ((float) mw / 2f) * gs;
        //float top = cy - ((float) mh / 2f) * gs;

        float left = cx - aox;
        float top = cy - aoy;

        float pfx = left + (float) (int) player.x * gs;
        float pfy = top + (float) (int) player.y * gs;

        AffineTransform transform = null;
        try {
            if ((flags & FLAG_ROTATE_VIEW) != 0) {
                transform = graphics.getTransform();
                graphics.rotate(player.angle - Math.PI / 2d, cx, cy);
            }

            graphics.setStroke(new BasicStroke(sw, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f));

            if ((flags & FLAG_HIDE_MARKERS) == 0) {
                graphics.setColor(Color.DARK_GRAY);
                graphics.fillRoundRect((int) (left + gs * (double) d[0]), (int) (top + gs * (double) d[1]), (int) gs, (int) gs, fcr, fcr);
                graphics.setColor(Color.LIGHT_GRAY);
                graphics.fillRoundRect((int) (left + gs * (double) d[3]), (int) (top + gs * (double) d[4]), (int) gs, (int) gs, fcr, fcr);
            }

            graphics.setColor(mixColors(PULSE_OFF, PULSE_ON, pulseI));
            graphics.drawRoundRect((int) (pfx + ws + sw), (int) (pfy + ws + sw), (int) (gs - tws - tsw), (int) (gs - tws - tsw), fcr, fcr);

            float apx = player.x, apy = player.y;
            float plx = left + apx * gs;
            float ply = top + apy * gs;
            float pr = player.radius * gs, tpr = pr * 2f;
            //float pcr = pr / 4f;

            float offAngle = 20f;
            float angle = (float) Math.toDegrees(player.angle) + offAngle;
            float arcAngle = 360f - offAngle * 2f;

            graphics.setColor(Color.ORANGE);
            //graphics.fillRoundRect((int) (plx - pr), (int) (ply - pr), (int) tpr, (int) tpr, (int) pcr, (int) pcr);
            graphics.fillArc((int) (plx - pr), (int) (ply - pr), (int) tpr, (int) tpr, (int) angle, (int) arcAngle);

            if ((flags & FLAG_HIDE_WALLS) == 0) {
                if (blinkOn) {
                    graphics.setColor(blinkColor);
                } else {
                    graphics.setColor(GRID_COLOR);
                }

                float wpx, wpy, wpd;
                int wfy, wf;
                for (int y = 0; y < mh; y++) {
                    final int[] row = m[y];
                    wfy = y == mh - 1 ? WB : 0;
                    wfy |= y == 0 ? WT : 0;
                    for (int x = 0; x < mw; x++) {
                        wf = wfy;
                        wf |= x == mw - 1 ? WR : 0;
                        wf |= x == 0 ? WL : 0;

                        wpx = apx - (float) (x) - 0.5f;
                        wpy = apy - (float) (y) - 0.5f;
                        wpd = wpx * wpx + wpy * wpy;
                        if (wpd > rangeSq) continue;

                        final int t = row[x];

                        final float ax = left + gs * x;
                        final float ay = top + gs * y;

                        final float s = gs * sd;

                        if ((t & WL) != 0) {
                            final float os = (wf & WL) != 0 ? gs : s;
                            graphics.fillRoundRect((int) (ax - ws),
                                                   (int) (ay - ows),
                                                   (int) tws,
                                                   (int) (os + ews),
                                                   wcr, wcr);
                        }
                        if ((t & WT) != 0) {
                            final float os = (wf & WT) != 0 ? gs : s;
                            graphics.fillRoundRect((int) (ax - ows),
                                                   (int) (ay - ws),
                                                   (int) (os + ews),
                                                   (int) tws,
                                                   wcr, wcr);
                        }
                        if ((t & WR) != 0) {
                            final float os = (wf & WR) != 0 ? gs : s;
                            graphics.fillRoundRect((int) (ax + gs - ws),
                                                   (int) (ay - ows),
                                                   (int) tws,
                                                   (int) (os + ews),
                                                   wcr, wcr);
                        }
                        if ((t & WB) != 0) {
                            final float os = (wf & WB) != 0 ? gs : s;
                            graphics.fillRoundRect((int) (ax - ows),
                                                   (int) (ay + gs - ws),
                                                   (int) (os + ews),
                                                   (int) tws,
                                                   wcr, wcr);
                        }
                    }
                }

                graphics.setColor(COLLISION_GRID_COLOR);
                //collider.render(graphics, left, top, gs);
            }
        } finally {
            if (transform != null) {
                try {
                    graphics.setTransform(transform);
                } catch (Throwable tr) {
                    tr.printStackTrace();
                }
            }
        }

        if ((flags & FLAG_HIDE_KEYS) == 0) {
            drawInputs(graphics, gs * 0.6f, gs * 0.35f, gs * 0.09f, gs * 0.15f);
        }

        if ((flags & FLAG_SHOW_TIMER) != 0) {
            Font font = getFont(gs * FONT_SCALE);
            graphics.setFont(font);

            stopWatch.update();
            graphics.setColor(Color.WHITE);
            graphics.drawString(stopWatch.toString(),
                                gs * 0.1f,
                                gs * 0.1f + graphics.getFontMetrics().getHeight() / 2f);
        }
    }

    public void drawInputs(Graphics2D graphics, float sizeX, float sizeY, float offset, float inset) {
        float left = this.size.xFloat() - offset * 2f - sizeX * 3f - inset, ll;
        float top = this.size.yFloat() - offset - sizeY * 2f - inset, lt;

        float gsx = sizeX + offset, hGsx = sizeX / 2f;
        float gsy = sizeY + offset, hGsy = sizeY / 2f;

        float mhs = Math.min(hGsx, hGsy) / 2f;
        int cr = (int) (mhs / 2f);

        // Up key
        if (drawDir(graphics, 1, (ll = left + gsx) + hGsx, (lt = top) + hGsy, mhs)) {
            graphics.drawRoundRect((int) ll, (int) lt, (int) sizeX, (int) sizeY, cr, cr);
        }

        // Left key
        if (drawDir(graphics, 0, (ll = left) + hGsx, (lt = top + gsy) + hGsy, mhs)) {
            graphics.drawRoundRect((int) ll, (int) lt, (int) sizeX, (int) sizeY, cr, cr);
        }

        // Down key
        if (drawDir(graphics, 3, (ll = left + gsx) + hGsx, (lt = top + gsy) + hGsy, mhs)) {
            graphics.drawRoundRect((int) ll, (int) lt, (int) sizeX, (int) sizeY, cr, cr);
        }

        // Right key
        if (drawDir(graphics, 2, (ll = left + gsx * 2) + hGsx, (lt = top + gsy) + hGsy, mhs)) {
            graphics.drawRoundRect((int) ll, (int) lt, (int) sizeX, (int) sizeY, cr, cr);
        }
    }

    private boolean drawDir(Graphics2D graphics, int dir, float x, float y, float size) {
        int tDir = rot(dir, false);

        boolean isDir = player.dir == tDir;
        boolean isDirPressed = isDir && showKey != -1f;

        int amount;
        if ((flags & FLAG_INFINITE_MOVES) == 0) {
            amount = maze.dirs[tDir];
        } else amount = 1;

        if (amount <= 0 && !isDirPressed) {
            return false;
        }

        graphics.setColor(isDir
                          ? (isDirPressed
                             ? KEY_PRESS_COLOR
                             : KEY_DIR_COLOR)
                          : KEY_NORMAL_COLOR
        );

        float dx = Maze.dirX(dir) * size;
        float dy = Maze.dirY(dir) * size;

        float sx = x + dx / 2f, ex = sx - dx;
        float sy = y + dy / 2f, ey = sy - dy;

        graphics.drawLine((int) sx, (int) sy, (int) (ex + dy), (int) (ey + dx));
        graphics.drawLine((int) sx, (int) sy, (int) (ex - dy), (int) (ey - dx));

        return true;
    }

    public Color mixColors(Color color1, Color color2, double percent) {
        double inverse_percent = 1.0d - percent;
        int redPart = (int) (color1.getRed() * percent + color2.getRed() * inverse_percent);
        int greenPart = (int) (color1.getGreen() * percent + color2.getGreen() * inverse_percent);
        int bluePart = (int) (color1.getBlue() * percent + color2.getBlue() * inverse_percent);
        int alphaPart = (int) (color1.getAlpha() * percent + color2.getAlpha() * inverse_percent);
        return new Color(redPart, greenPart, bluePart, alphaPart);
    }
}
