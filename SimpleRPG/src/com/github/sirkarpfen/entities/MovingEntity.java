package com.github.sirkarpfen.entities;




/**
 * A MovingEntity represents any element that appears in the game and is actually movable.
 * The entity is responsible for resolving collisions and movement
 * based on a set of properties defined either by subclass or externally.
 * 
 * @author sirkarpfen
 *
 */
public abstract class MovingEntity extends Entity {
	
	/**
	 * Do the logic associated with this entity. This method
	 * will be called periodically based on game events
	 * <p>
	 * <b>THIS METHOD IS CURRENTLY UNUSED</b>
	 * <p>
	 * Implementation might follow. Currently not sure about that.
	 */
	public void doLogic() {}

	/**
	 * Moves this entity using the direction provided by the input handler.
	 * 
	 * @see com.github.sirkarpfen.main.Game.KeyInputHandler
	 */
	public abstract void move();
	
	/**
	 * Updates all animations.
	 */
	public abstract void updateAnimations();
	
}
