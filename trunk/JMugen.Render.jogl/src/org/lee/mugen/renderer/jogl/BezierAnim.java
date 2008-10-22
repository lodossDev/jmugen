/**
 * Copyright (c) 2007, Sun Microsystems, Inc.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following 
 *     disclaimer in the documentation and/or other materials provided 
 *     with the distribution.
 *   * Neither the name of the BezierAnim3D project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.lee.mugen.renderer.jogl;

import static java.awt.Color.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.GeneralPath;
import javax.swing.*;

/**
 * Based on the version found in Java2Demo...
 */
public class BezierAnim {

    private static final int NUMPTS = 6;
    private BasicStroke solid =
        new BasicStroke(10.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
    private BasicStroke dashed =
        new BasicStroke(10.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
                        10, new float[] {5}, 0);
    private float animpts[] = new float[NUMPTS * 2];
    private float  deltas[] = new float[NUMPTS * 2];
    private Paint fillPaint, drawPaint;
    private boolean doFill = true;
    private boolean doDraw = true;
    private GradientPaint gradient;
    private BasicStroke stroke;

    public BezierAnim() {
        gradient = new GradientPaint(0,0,RED,200,200,YELLOW);
        fillPaint = gradient;
        drawPaint = BLUE;
        stroke = solid;
    }

    private void animate(float[] pts, float[] deltas, int index, int limit) {
        float newpt = pts[index] + deltas[index];
        if (newpt <= 0) {
            newpt = -newpt;
            deltas[index] =   (float) (Math.random() * 4.0 + 2.0);
        } else if (newpt >= (float) limit) {
            newpt = 2.0f * limit - newpt;
            deltas[index] = - (float) (Math.random() * 4.0 + 2.0);
        }
        pts[index] = newpt;
    }

    public void reset(int w, int h) {
        for (int i = 0; i < animpts.length; i += 2) {
            animpts[i + 0] = (float) (Math.random() * w);
            animpts[i + 1] = (float) (Math.random() * h);
             deltas[i + 0] = (float) (Math.random() * 6.0 + 4.0);
             deltas[i + 1] = (float) (Math.random() * 6.0 + 4.0);
            if (animpts[i + 0] > w / 2.0f) {
                deltas[i + 0] = -deltas[i + 0];
            }
            if (animpts[i + 1] > h / 2.0f) {
                deltas[i + 1] = -deltas[i + 1];
            }
        }
        gradient = new GradientPaint(0,0,RED,w*.7f,h*.7f,YELLOW);
    }

    public void step(int w, int h) {
        for (int i = 0; i < animpts.length; i += 2) {
            animate(animpts, deltas, i + 0, w);
            animate(animpts, deltas, i + 1, h);
        }
    }

    public void render(int w, int h, Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, w, h);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        float[] ctrlpts = animpts;
        int len = ctrlpts.length;
        float prevx = ctrlpts[len - 2];
        float prevy = ctrlpts[len - 1];
        float  curx = ctrlpts[0];
        float  cury = ctrlpts[1];
        float  midx = (curx + prevx) / 2.0f;
        float  midy = (cury + prevy) / 2.0f;
        GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        gp.moveTo(midx, midy);
        for (int i = 2; i <= ctrlpts.length; i += 2) {
            float x1 = (midx + curx) / 2.0f;
            float y1 = (midy + cury) / 2.0f;
            prevx = curx;
            prevy = cury;
            if (i < ctrlpts.length) {
                curx = ctrlpts[i + 0];
                cury = ctrlpts[i + 1];
            } else {
                curx = ctrlpts[0];
                cury = ctrlpts[1];
            }
            midx = (curx + prevx) / 2.0f;
            midy = (cury + prevy) / 2.0f;
            float x2 = (prevx + midx) / 2.0f;
            float y2 = (prevy + midy) / 2.0f;
            gp.curveTo(x1, y1, x2, y2, midx, midy);
        }
        gp.closePath();

        if (doDraw) {
            g2.setPaint(drawPaint);
            g2.setStroke(stroke);
            g2.draw(gp);
        }

        if (doFill) {
            g2.setPaint(fillPaint);
            g2.fill(gp);
        }
    }
}
