package com.github.sirkarpfen.constants;

/**
 * Defines some Constants, used in different classes for different purposes.
 * 
 * @author sirkarpfen
 *
 */
public class Constants {
	/** The walking speed used on entities */
	public final static float WALKING_VELOCITY = 2F;
	/** Defines how long an Animation frame stays on the screen. */
	public final static float ANIMATION_VELOCITY = 0.2F;
	/** The actual tile size in pixels. (tile/pixels)*/
	public static final float UNIT_SCALE = 1F;
	/** Used with Box2D to transform pixels to meters */
	public static final float PIXELS_PER_METER = 60F;
	/** The screen's width. */
	public static final int WIDTH = 800;
	/** The screen's height. */
	public static final int HEIGHT = 600;
}
