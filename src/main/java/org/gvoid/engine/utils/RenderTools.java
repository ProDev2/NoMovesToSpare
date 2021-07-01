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

package org.gvoid.engine.utils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import org.gvoid.engine.math.Point2;

public class RenderTools {
	public static final int ALIGN_LEFT = 1;
	public static final int ALIGN_CENTER = 2;
	public static final int ALIGN_RIGHT = 3;
	
	public static Point2 getBounds(String text, Font font, FontRenderContext context) {
		double width = 0;
		double height = 0;
		
		if (font != null && context != null && text != null && text.length() > 0) {
			String[] parts = null;
			if (text.contains("\n"))
				parts = text.split("\n");
			else
				parts = new String[] { text };
			
			if (parts != null) {
				for (String part : parts) {
					Rectangle2D textBounds = font.getStringBounds(part, context);
					if (textBounds == null) continue;
					
					double partWidth = (double) textBounds.getWidth();
					double partHeight = (double) textBounds.getHeight();
					
					if (partWidth > width) width = partWidth;
					if (partHeight > 0) height += partHeight;
				}
			}
		}
		
		return new Point2(width, height);
	}
	
	public static Point2 getBounds(String text, FontMetrics metrics) {
		double width = 0;
		double height = 0;
		
		if (metrics != null && text != null && text.length() > 0) {
			String[] parts = null;
			if (text.contains("\n"))
				parts = text.split("\n");
			else
				parts = new String[] { text };
			
			if (parts != null) {
				for (String part : parts) {
					double partWidth = (double) metrics.stringWidth(part);
					double partHeight = (double) metrics.getHeight() + (metrics.getHeight() - metrics.getMaxAscent());
					
					if (partWidth > width) width = partWidth;
					if (partHeight > 0) height += partHeight;
				}
			}
		}
		
		return new Point2(width, height);
	}
	
	public static Point2 draw(Graphics2D graphics, String text, Point2 pos) {
		return draw(graphics, text, pos, ALIGN_CENTER);
	}
	
	public static Point2 draw(Graphics2D graphics, String text, Point2 pos, int alignment) {
		return draw(graphics, text, pos.x, pos.y, alignment);
	}
	
	public static Point2 draw(Graphics2D graphics, String text, double x, double y, int alignment) {
		double width = 0;
		double height = 0;
		
		if (graphics != null && graphics.getFont() != null && graphics.getFontRenderContext() != null && text != null && text.length() > 0) {
			Font font = graphics.getFont();
			FontRenderContext context = graphics.getFontRenderContext();
			
			String[] parts = null;
			if (text.contains("\n"))
				parts = text.split("\n");
			else
				parts = new String[] { text };
			
			if (parts != null) {
				for (String part : parts) {
					Rectangle2D textBounds = font.getStringBounds(part, context);
					
					double partWidth = (double) textBounds.getWidth();
					if (partWidth > width) width = partWidth;
				}
				
				for (String part : parts) {
					Rectangle2D textBounds = font.getStringBounds(part, context);
					
					double partWidth = (double) textBounds.getWidth();
					
					double partHeight = (double) textBounds.getHeight();
					if (partHeight > 0) height += partHeight;
					
					if (partWidth > 0 && partHeight > 0) {
						double textPosX = x;
						switch (alignment) {
							case ALIGN_LEFT:
								textPosX = x;
								break;
								
							case ALIGN_CENTER:
								textPosX = x + (width / 2) - (partWidth / 2);
								break;
								
							case ALIGN_RIGHT:
								textPosX = x + width - partWidth;
								break;
						}
						
						double textPosY = y + height;
						
						graphics.drawString(part, (int) textPosX, (int) textPosY);
					}
				}
			}
		}
		
		return new Point2(width, height);
	}
}
