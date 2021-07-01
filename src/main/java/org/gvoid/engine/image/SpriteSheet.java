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

package org.gvoid.engine.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.gvoid.engine.math.Point2;

public class SpriteSheet {
    private final Point2 size;
    private final int columns;
    private final int rows;

    private BufferedImage sprite;

    public SpriteSheet(File image, Point2 size, int columns, int rows) {
        try {
            if (image != null)
                sprite = ImageIO.read(image);
        } catch (Throwable tr) {
            tr.printStackTrace();
        }

        this.size = size;

        this.columns = columns;
        this.rows = rows;
    }

    public SpriteSheet(String resName, Point2 size, int columns, int rows) {
        InputStream stream = null;
        try {
            stream = getClass().getClassLoader().getResourceAsStream(resName);
            if (stream != null)
                sprite = ImageIO.read(stream);
        } catch (Throwable tr) {
            tr.printStackTrace();
        } finally {
            try {
                if (stream != null)
                    stream.close();
            } catch (Throwable ignored) {
            }
        }

        this.size = size;

        this.columns = columns;
        this.rows = rows;
    }

    public SpriteSheet(InputStream stream, Point2 size, int columns, int rows) {
        try {
            if (stream != null)
                sprite = ImageIO.read(stream);
        } catch (Throwable tr) {
            tr.printStackTrace();
        } finally {
            try {
                if (stream != null)
                    stream.close();
            } catch (Throwable ignored) {
            }
        }

        this.size = size;

        this.columns = columns;
        this.rows = rows;
    }

    public SpriteSheet(BufferedImage image, Point2 size, int columns, int rows) {
        this.sprite = image;

        this.size = size;

        this.columns = columns;
        this.rows = rows;
    }

    public BufferedImage getTexture(int id) {
        if (sprite == null)
            return null;

        int count = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                if (count == id) {
                    return getTexture(x, y);
                }

                count++;
            }
        }

        return null;
    }

    public BufferedImage getTexture(int column, int row) {
        if (sprite == null)
            return null;

        int startX = column * size.xInt();
        int startY = row * size.yInt();

        return sprite.getSubimage(startX, startY, size.xInt(), size.yInt());
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public int getFrames() {
        return columns * rows;
    }

    public Point2 getSize() {
        return size;
    }
}
