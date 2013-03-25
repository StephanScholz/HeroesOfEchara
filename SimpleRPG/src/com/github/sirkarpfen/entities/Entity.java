package com.github.sirkarpfen.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.github.sirkarpfen.main.Viewable;

/**
 * An entity represents any element that appears in the game and is actually movable.
 * The entity is responsible for resolving collisions and movement
 * based on a set of properties defined either by subclass or externally.
 * 
 * @author sirkarpfen
 *
 */
public abstract class Entity implements Viewable {
	/** The current x location of this entity */
	protected float x;
	/** The current y location of this entity */
	protected float y;
	/** The movement speed of this entity */
	protected float movementSpeed;
	/** The OrthographicCamera used to view this Entity */
	protected OrthographicCamera camera;
	
	protected TiledMapTileLayer collisionLayer;
	/**
	 * Gets the collisionLayer to use, when testing collisions with this entity.
	 * @return The collisionLayer
	 */
	public TiledMapTileLayer getCollisionLayer() { return collisionLayer; }
	/** Sets the collisionLayer to use, when testing collisions with this entity. */
	public void setCollisionLayer(TiledMapTileLayer collisionLayer) { this.collisionLayer = collisionLayer; }
	
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
	 * Get the x location of this entity
	 * 
	 * @return The x location of this entity
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * Get the y location of this entity
	 * 
	 * @return The y location of this entity
	 */
	public float getY() {
		return y;
	}
	
	@Override
	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
		camera.position.set(this.getX(), this.getY(), 0);
	}

	/**
	 * Moves this entity using the direction provided by the input handler.
	 * 
	 * @see com.github.sirkarpfen.main.Game.KeyInputHandler
	 */
	public abstract void move();
	
}
