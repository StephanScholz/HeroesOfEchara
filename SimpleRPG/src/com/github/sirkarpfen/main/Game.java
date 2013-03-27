package com.github.sirkarpfen.main;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.github.sirkarpfen.constants.Constants;
import com.github.sirkarpfen.entities.Direction;
import com.github.sirkarpfen.entities.Entity;
import com.github.sirkarpfen.entities.Player;
import com.github.sirkarpfen.entities.listeners.EntityContactListener;
import com.github.sirkarpfen.maps.MapHandler;

/**
 * The base game class. Handles rendering/disposal/logic updates and more.
 * 
 * @author sirkarpfen
 * @see com.github.sirkarpfen.main.Main
 */
public class Game implements ApplicationListener {
	/*
	 * The time the last frame was rendered, used for throttling framerate
	 */
	private long lastRender;
	
	/* Helper class for dealing with tiled maps. Credits go to "dpk". */
	private MapHandler mapHandler;
	private KeyInputHandler keyInput;
	
	/* FPS font and the SpriteBatch to use for all rendering. */
	private BitmapFont font;
	private SpriteBatch spriteBatch;
	
	/* Camera used to display all scenes. */
	private OrthographicCamera camera;
	
	/* Represents the player character. */
	private Player player;

	private World world;
	
	public World getWorld() { return world; }
	
	private Box2DDebugRenderer debugRenderer;
	
	/**
	 * Sets up the Map/Player and Camera. Camera is always centered on the player,
	 * when a new game is started, or a savegame loaded.
	 */
	@Override
	public void create() {
		// use GL10 without "power of two" enforcement.
		Texture.setEnforcePotImages(false);
		
		keyInput = new KeyInputHandler();
		Gdx.input.setInputProcessor(keyInput);
		
		// Creates the world, for our Box2D Entities.
		this.createWorld();
		
		// Set up the starting map, and load all maps into ram.
		this.loadMaps();
		
		// Initiates the player, and puts him on the Spawn point.
		this.createPlayer();
		
		// prepares the OrthographicCamera, for projection.
		this.prepareCamera();
		
		// indicates the last render process, and measures the time in between
		// 2 full runs.
		lastRender = System.nanoTime();
		
		font = new BitmapFont();
		// used for fast and smooth rendering.
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(camera.combined);

	}
	
	private void createPlayer() {
		player = new Player("StartIsland", world);
		if(!player.isSpawned()) {
			player.spawnPlayer();
		}
	}

	private void createWorld() {
		world = new World(new Vector2(0,0), true);
		world.setContactListener(new EntityContactListener());
		debugRenderer = new Box2DDebugRenderer();
	}

	private void loadMaps() {
		mapHandler = MapHandler.getInstance();
		mapHandler.setWorld(world);
		mapHandler.loadMaps();
	}

	/*
	 * Prepares the OrthographicCamera and sets it on the startPosition.
	 */
	private void prepareCamera() {
		camera = new OrthographicCamera(Constants.WIDTH, Constants.HEIGHT);
		player.setCamera(camera);
		camera.update();
	}

	@Override
	public void dispose() {
	}
	
	/**
	 * All rendering processes and methods, are called within this method.
	 * it acts as a hook into the different rendering methods, used by all
	 * Entity classes, and the map renderer. A SpriteBatch object is passed on
	 * if a Texture or sprite needs to be drawn.
	 */
	@Override
	public void render() {
		long now = System.nanoTime();
		
		Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// ********** Start rendering area. **********
		mapHandler.renderBackgroundMap(camera);
		
		this.renderWorldBodies();
		player.move();
		mapHandler.renderForegroundMap(camera);
		
		spriteBatch.begin();
		font.setScale(0.05F);
		font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, 0);
		spriteBatch.end();
		// ********** End rendering area. **********
		
		// used for fps-limitation.
		now = System.nanoTime();
		if (now - lastRender < 30000000) { // 30 ms, ~33FPS
			try {
				Thread.sleep(30 - (now - lastRender) / 1000000);
			} catch (InterruptedException e) {
			}
		}
		
		world.step(1, 6, 2);
		/**
		 * Draw this last, so we can see the collision boundaries on top of the
		 * sprites and map.
		 */
		debugRenderer.render(world, camera.combined);

		lastRender = now;
	}
	
	private void renderWorldBodies() {
		
		Iterator<Body> bi = world.getBodies();
        
		// Update all positions
		while (bi.hasNext()){
		    Body b = bi.next();

		    // Get the bodies user data - in this example, our user 
		    // data is an instance of the Entity class
		    Entity e = null;
		    if(b.getUserData() instanceof Entity) {
		    	e = (Entity) b.getUserData();
		    }

		    if (e != null) {
		        if(e instanceof Player) {
		        	((Player)e).updateAnimations();
		        }
		        // Render the Sprite
		        e.render(spriteBatch);
		    }
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	/**
	 * Handles the key input from the player.
	 * 
	 * @author sirkarpfen
	 *
	 */
	private class KeyInputHandler extends InputAdapter {
		
		@Override
		public boolean keyDown(int keycode) {
			if(keycode == Input.Keys.LEFT) {
	            player.setMovingDirection(Direction.LEFT);
	            player.setPressedKey(true);
			} else if(keycode == Input.Keys.RIGHT) {
				player.setMovingDirection(Direction.RIGHT);
				player.setPressedKey(true);
			} else if(keycode == Input.Keys.DOWN) {
				player.setMovingDirection(Direction.DOWN);
				player.setPressedKey(true);
			} else if (keycode == Input.Keys.UP) {
				player.setMovingDirection(Direction.UP);
				player.setPressedKey(true);
			}
			return player.hasPressedKey();
			
		}
		
		@Override
		public boolean keyUp(int keycode) {
			if(keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT ||
			   keycode == Input.Keys.UP || keycode == Input.Keys.DOWN) {
				player.setMovingDirection(Direction.STAND);
				player.setPressedKey(false);
			}
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
