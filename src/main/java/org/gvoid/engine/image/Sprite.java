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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import static java.lang.Math.max;

@SuppressWarnings("unused")
public class Sprite implements Closeable {
    public static final int NO_SIZE = Integer.MIN_VALUE;
    public static int DEFAULT_SCALE_HINTS = Image.SCALE_DEFAULT;

    /* -------------- Initialization -------------- */
    public static Sprite open(File file) {
        return new Sprite().tryImage(file);
    }

    public static Sprite open(URL url) {
        return new Sprite().tryImage(url);
    }

    public static Sprite open(String name) {
        return new Sprite().tryImage(name);
    }

    public static Sprite open(InputStream in) {
        return new Sprite().tryImage(in);
    }

    public static Sprite open(ImageInputStream in) {
        return new Sprite().tryImage(in);
    }

    /* ------------------ Sprite ------------------ */
    private volatile BufferedImage mImage;
    private volatile boolean mWillTile;
    private int mcc, mrc;
    private int mcs, mrs;
    private int mtw, mth;
    private int mox, moy;

    private boolean mWillCache;
    private Image[] mSubImages;
    private boolean mCached;
    private volatile int mCount;

    public Sprite() {
        mImage = null;
        mWillTile = false;

        mWillCache = false;
        mSubImages = null;
        mCached = false;
        mCount = -1;
    }

    public Sprite tryImage(File file) {
        try {
            return withImage(file);
        } catch (IOException ex) {
            ex.printStackTrace();
            return this;
        }
    }

    public Sprite tryImage(URL url) {
        try {
            return withImage(url);
        } catch (IOException ex) {
            ex.printStackTrace();
            return this;
        }
    }

    public Sprite tryImage(String name) {
        try {
            return withImage(name);
        } catch (IOException ex) {
            ex.printStackTrace();
            return this;
        }
    }

    public Sprite tryImage(InputStream in) {
        try {
            return withImage(in);
        } catch (IOException ex) {
            ex.printStackTrace();
            return this;
        }
    }

    public Sprite tryImage(ImageInputStream in) {
        try {
            return withImage(in);
        } catch (IOException ex) {
            ex.printStackTrace();
            return this;
        }
    }

    public Sprite withImage(File file) throws IOException {
        return withImage(ImageIO.read(file));
    }

    public Sprite withImage(URL url) throws IOException {
        return withImage(ImageIO.read(url));
    }

    public Sprite withImage(String name) throws IOException {
        ClassLoader cl = getClass().getClassLoader();
        return withImage(cl.getResourceAsStream(name));
    }

    public Sprite withImage(InputStream in) throws IOException {
        try {
            return withImage(ImageIO.read(in));
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Sprite withImage(ImageInputStream in) throws IOException {
        try {
            return withImage(ImageIO.read(in));
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Sprite withImage(BufferedImage image) {
        if (mImage != image) invalidate();
        if (mWillTile) calc(image);
        mImage = image;
        return this;
    }

    public final BufferedImage getImage() {
        return mImage;
    }

    public final BufferedImage getImageOrThrow() {
        BufferedImage image = mImage;
        if (image == null) {
            throw new IllegalStateException("no image");
        }
        return image;
    }

    public boolean hasImage() {
        return mImage != null;
    }

    public int imageWidth() {
        BufferedImage image = mImage;
        return image != null ? image.getWidth() : 0;
    }

    public int imageHeight() {
        BufferedImage image = mImage;
        return image != null ? image.getHeight() : 0;
    }

    public boolean isTiled() {
        return mWillTile;
    }

    public int tileWidth() {
        return mWillTile ? mtw : 0;
    }

    public int tileHeight() {
        return mWillTile ? mth : 0;
    }

    public int columns() {
        return mWillTile ? mcc : -1;
    }

    public int rows() {
        return mWillTile ? mrc : -1;
    }

    public int columnWidth() {
        return mWillTile ? mcs : 0;
    }

    public int rowHeight() {
        return mWillTile ? mrs : 0;
    }

    public int tOffXStart() {
        return mWillTile ? mox : 0;
    }

    public int tOffYStart() {
        return mWillTile ? moy : 0;
    }

    public int tOffXEnd() {
        return mWillTile ? mox + mtw : 0;
    }

    public int tOffYEnd() {
        return mWillTile ? moy + mth : 0;
    }

    protected final int calc(BufferedImage image) {
        mWillTile = false;
        int w = 0, h = 0;
        if (image != null) {
            w = image.getWidth();
            h = image.getHeight();
        }
        int cc, rc;
        mcc = cc = w / mcs;
        mrc = rc = h / mrs;
        mWillTile = (mcc | mrc | mcs | mrs) >= 0;
        return cc * rc;
    }

    public Sprite withNoTiling() {
        mWillTile = false;
        return this;
    }

    public Sprite withTiling(int width, int height) {
        mWillTile = false;
        mcs -= mtw;
        mrs -= mth;
        mcs += mtw = width;
        mrs += mth = height;
        calc(mImage);
        invalidate();
        return this;
    }

    public Sprite withTilingOffset(int left, int top, int right, int bottom) {
        mWillTile = false;
        mcs = (mox = left) + mtw + right;
        mrs = (moy = top) + mth + bottom;
        calc(mImage);
        invalidate();
        return this;
    }

    public Sprite resize() {
        return resize(true);
    }

    public Sprite resize(boolean willCache) {
        BufferedImage image = mImage;
        int len = mWillTile ? calc(image) : 0;
        return withCount(len, willCache);
    }

    public Sprite withCount(int size) {
        return withCount(size, true);
    }

    public Sprite withCount(int count, boolean willCache) {
        cache:
        if (willCache && count > 0) {
            int len = max(mCount, 0);
            Image[] ci = mSubImages;
            int cap = mWillCache ? ci.length : 0;
            if (cap >= count) {
                for (int i = len; i < count; i++) {
                    ci[i] = null;
                }
                break cache;
            }
            Image[] nci = new Image[count];
            if (cap > 0) System.arraycopy(ci, 0, nci, 0, len);
            mSubImages = nci;
        } else {
            mSubImages = null;
            mCached = false;
        }
        mWillCache = willCache;
        mCount = count;
        return this;
    }

    public void invalidate() {
        int len = mCount;
        if (mWillCache && mCached) {
            mCount = -1;
            for (int i = 0; i < len; i++) {
                mSubImages[i] = null;
            }
            mCached = false;
            mCount = len;
        }
    }

    public void invalidate(int id) {
        int len = mCount;
        if (id < 0 || id >= len) {
            throw new IndexOutOfBoundsException(
                    "invalid id: " + id
            );
        }
        if (mWillCache && mCached) {
            mCount = -1;
            mSubImages[id] = null;
            mCached = len != 1;
            mCount = len;
        }
    }

    public BufferedImage getTile(int id) {
        return (BufferedImage) getTile(id, NO_SIZE, NO_SIZE, 0);
    }

    public Image getTile(int id, int width, int height) {
        return getTile(id, width, height, DEFAULT_SCALE_HINTS);
    }

    public Image getTile(int id, int width, int height, int hints) {
        BufferedImage image = getImageOrThrow();
        if (!mWillTile) return image;

        int len = mCount;
        if (id < 0 || id >= len) {
            throw new IndexOutOfBoundsException(
                    "invalid id: " + id
            );
        }

        boolean sized = width != NO_SIZE
                && height != NO_SIZE;

        Image subImage;
        useCache:
        if (mWillCache && mCached && (subImage = mSubImages[id]) != null) {
            if (sized && width != subImage.getWidth(null)
                    | height != subImage.getHeight(null)) {
                break useCache;
            } else if (!sized && !(subImage instanceof BufferedImage)) {
                break useCache;
            }
            return subImage;
        }

        int ci = id % mcc, left = ci * mcs + mox;
        int ri = id / mrc, top = ri * mrs + moy;
        subImage = image.getSubimage(left, top, mtw, mth);

        if (sized && subImage != null) {
            subImage = subImage.getScaledInstance(width, height, hints);
        }

        if (subImage == null) {
            throw new IllegalStateException("no sub image");
        } else if (mWillCache) {
            mCount = -1;
            mSubImages[id] = subImage;
            mCached = true;
            mCount = len;
        }

        return subImage;
    }

    @Override
    public void close() {
        mImage = null;

        mWillCache = false;
        mSubImages = null;
        mCached = false;
        mCount = -1;
    }
}
