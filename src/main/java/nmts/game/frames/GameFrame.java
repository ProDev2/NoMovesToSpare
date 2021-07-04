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

package nmts.game.frames;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Closeable;
import javax.swing.*;
import nmts.game.Config;
import org.gvoid.engine.Game;
import org.gvoid.engine.GamePanel;

public class GameFrame extends JFrame implements Closeable, MouseListener {
    public GridBagLayout layout = new GridBagLayout();

    public GamePanel screen = new GamePanel() {
        @Override
        protected void onUpdate() {
            GameFrame.this.onUpdate();
        }
    };

    public GameFrame() {
        this(null);
    }

    public GameFrame(Game game) {
        super();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (screen != null)
                    screen.setGame(null);
            }
        });

        int frameWidth = 1253;
        int frameHeight = 680;
        setSize(frameWidth, frameHeight);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - getSize().width) / 2;
        int y = (d.height - getSize().height) / 2;
        setLocation(x, y);

        setTitle(Config.TITLE);
        setResizable(true);

        Container cp = getContentPane();
        cp.setLayout(layout);

        // Create components
        screen.setBounds(0, 0, 1047, 508);
        screen.setBackground(Color.WHITE);

        screen.setCps(Config.CPS);
        screen.setAntialias(Config.ANTIALIASING);

        screen.addMouseListener(this);

        screen.setGame(game);

        // Add components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // Screen
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        cp.add(screen, gbc);

        // Show screen
        setVisible(true);
    }

    public void setGame(Game game) {
        if (screen != null) {
            screen.setGame(game);
        }
    }

    public void setAntialias(boolean antialias) {
        screen.setAntialias(antialias);
    }

    @Override
    public void close() {
        try {
            if (screen != null)
                screen.setGame(null);
        } catch (Exception ignored) {
        }

        try {
            setVisible(false);
        } finally {
            dispose();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            if (e != null)
                e.getComponent().requestFocus();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }

    protected void onUpdate() {
    }
}
