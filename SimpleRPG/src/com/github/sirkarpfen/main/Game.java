package com.github.sirkarpfen.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.sirkarpfen.entities.MovingDirection;
import com.github.sirkarpfen.entities.Player;

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

	/*
	 * The screen's width and height. This may not match that computed by
	 * libgdx's gdx.graphics.getWidth() / getHeight() on devices that make use
	 * of on-screen menu buttons.
	 */
	private int screenWidth;
	private int screenHeight;
	
	/* FPS font and the SpriteBatch to use for all rendering. */
	private BitmapFont font;
	private SpriteBatch spriteBatch;
	
	/* Camera used to display all scenes. */
	private OrthographicCamera camera;
	
	/* Represents the player character. */
	private Player player;
	
	public Game(int width, int height) {
		super();
		
		screenWidth = width;
		screenHeight = height;
	}
	
	/**
	 * Sets up the Map/Player and Camera. Camera is always centered on the player,
	 * when a new game is started, or a savegame loaded.
	 */
	@Override
	public void create() {
		// use GL10 without "power of two" enforcement.
		Texture.setEnforcePotImages(false);
		
		// Set up the starting map, and load all maps into ram.
		mapHandler = new MapHandler();
		keyInput = new KeyInputHandler();
		Gdx.input.setInputProcessor(keyInput);
		mapHandler.loadMaps();
		
		// Initiates the player, and puts him on the Spawn point.
		player = new Player(mapHandler);
		if(!player.isSpawned()) {
			// Saves the current map, the player is on.
			player.setCurrentMapName("StartIsland");
			player.spawnPlayer();
		}
		
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
	
	/*
	 * Prepares the OrthographicCamera and sets it on the startPosition.
	 */
	private void prepareCamera() {
		camera = new OrthographicCamera(screenWidth/22, screenHeight/22);
		player.setCamera(camera);
		mapHandler.setCamera(camera);
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
		
		this.updateAnimations();
		
		// ********** Start rendering area. **********
		mapHandler.renderBackgroundMap();
		
		player.move();
		player.render(spriteBatch);
		
		mapHandler.renderForegroundMap();
		
		spriteBatch.begin();
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

		lastRender = now;
	}
	
	private void updateAnimations() {
		player.updateAnimations();
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
	            player.setMovingDirection(MovingDirection.LEFT);
	            player.setPressedKey(true);
			} else if(keycode == Input.Keys.RIGHT) {
				player.setMovingDirection(MovingDirection.RIGHT);
				player.setPressedKey(true);
			} else if(keycode == Input.Keys.DOWN) {
				player.setMovingDirection(MovingDirection.DOWN);
				player.setPressedKey(true);
			} else if (keycode == Input.Keys.UP) {
				player.setMovingDirection(MovingDirection.UP);
				player.setPressedKey(true);
			}
			return player.hasPressedKey();
			
		}
		
		@Override
		public boolean keyUp(int keycode) {
			if(keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT ||
			   keycode == Input.Keys.UP || keycode == Input.Keys.DOWN) {
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
