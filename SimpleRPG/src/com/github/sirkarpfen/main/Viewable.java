package com.github.sirkarpfen.main;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Symbolizes a viewable class, on which a camera object could be used.
 * 
 * @author sirkarpfen
 *
 */
public interface Viewable {
	/**
	 * Sets the camera for this map. Has to be called, before starting
	 * to render anything.
	 * 
	 * @param camera The camera to use for projecting.
	 */
	abstract void setCamera(OrthographicCamera camera);
}
