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

@SuppressWarnings("UnusedReturnValue")
public final class Animation {
    private int[] frames;
    private Integer freezeFrame;

    private int frame;
    private long delay;
    private long startTime;
    private final SpriteSheet sprite;

    private OnAnimate listener;

    public Animation(SpriteSheet sprite, int delay, int... ids) {
        this.sprite = sprite;
        this.delay = delay;
        this.frames = ids;

        this.frame = 0;
        this.startTime = System.currentTimeMillis();
    }

    public Animation(SpriteSheet sprite, int delay) {
        this.sprite = sprite;
        this.delay = delay;
        this.frames = new int[0];

        this.frame = 0;
        this.startTime = System.currentTimeMillis();
    }

    public interface OnAnimate {
        void nextFrame(int frame, int allFrames);

        void finish();
    }

    public Animation setAnimationListener(OnAnimate listener) {
        this.listener = listener;
        return this;
    }

    public void update() {
        int frameCount = frames.length;
        if (freezeFrame != null &&
                frame >= 0 &&
                frame < frameCount &&
                frames[frame] == freezeFrame)
            return;

        if (System.currentTimeMillis() - startTime >= delay) {
            frame++;
            startTime = System.currentTimeMillis();
            if (frame >= frameCount) {
                frame = 0;

                try {
                    if (listener != null)
                        listener.finish();
                } catch (Exception e) {
                }
            }

            try {
                if (listener != null)
                    listener.nextFrame(frame, frames.length);
            } catch (Exception e) {
            }
        }
    }

    public Animation restart() {
        this.frame = 0;
        this.startTime = System.currentTimeMillis();
        return this;
    }

    public boolean hasImage() {
        return sprite != null && frame >= 0 && frames.length > frame;
    }

    public BufferedImage getImage() {
        if (hasImage())
            return sprite.getTexture(frames[frame]);
        return null;
    }

    public SpriteSheet getSprite() {
        return sprite;
    }

    public Animation setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public Animation setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public boolean hasIds() {
        return frames.length > 0;
    }

    public Animation setIds(int... ids) {
        this.frames = ids;
        return this;
    }

    public Animation setIdsAndAnim(int... ids) {
        this.frames = ids;
        this.freezeFrame = null;
        return this;
    }

    public boolean unfreeze() {
        boolean changed = freezeFrame != null;
        freezeFrame = null;
        return changed;
    }

    public boolean setFreezeId(int freezeId) {
        boolean changed = freezeFrame == null || freezeFrame != freezeId;
        freezeFrame = freezeId;
        return changed;
    }

    public boolean freezeFirst() {
        int frameCount = frames.length;
        if (frameCount != 1)
            return false;

        freezeFrame = frames[0];
        return true;
    }

    public boolean freezeNow() {
        int frameCount = frames.length;
        if (frameCount <= 0)
            return false;

        int freezeIndex = Math.max(Math.min(frame, frameCount - 1), 0);
        freezeFrame = frames[freezeIndex];
        return true;
    }

    public boolean freezeAt(int idIndex) {
        int frameCount = frames.length;
        if (frameCount <= 0)
            return false;

        int freezeIndex = Math.max(Math.min(idIndex, frameCount - 1), 0);
        freezeFrame = frames[freezeIndex];
        return true;
    }
}
