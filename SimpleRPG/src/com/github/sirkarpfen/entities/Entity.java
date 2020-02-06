package com.github.sirkarpfen.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.github.sirkarpfen.main.Viewable;
import com.github.sirkarpfen.maps.MapStorage;

/**
 * The base class of all entities.
 * 
 * @author sirkarpfen
 *
 */
public abstract class Entity implements Viewable {

	/** The current x location of this entity */
	protected float x;
	/** @param x the x to set */
	public void setX(float x) { this.x = x;	}
	/** @return The x location of this entity */
	public float getX() { return x; }
	/** The current y location of this entity */
	protected float y;
	/** @param y the y to set */
	public void setY(float y) { this.y = y; }
	/** @return The y location of this entity */
	public float getY() { return y; }
	/** the world for our bodies. */
	protected World world;
	/** The OrthographicCamera used to view all Entities */
	protected OrthographicCamera camera;
	/** The storage containing all maps for use with every entity */
	protected MapStorage mapStorage;
	
	protected Body body;
	
	protected boolean flaggedForDelete = false;
	public boolean isFlaggedForDelete() {return flaggedForDelete;}
	public void setFlaggedForDelete(boolean flag) {this.flaggedForDelete = flag;}
	protected boolean flaggedForCreate = false;
	public boolean isFlaggedForCreate() {return flaggedForCreate;}
	
	protected Entity(World world) {
		this.mapStorage = MapStorage.getInstance();
		this.world = world;
	}
	
	@Override
	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
		camera.position.set(this.getX(), this.getY(), 0);
	}
	
	/**
	 * Renders the Entity according to the SpriteBatch.
	 * 
	 * @param SpriteBatch The SpriteBatch to use for rendering.
	 */
	public abstract void render(SpriteBatch spriteBatch);
	
	public abstract void createBody();
	
}
