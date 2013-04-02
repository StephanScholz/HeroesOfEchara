package com.github.sirkarpfen.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.github.sirkarpfen.constants.Constants;
import com.github.sirkarpfen.entities.Direction;
import com.github.sirkarpfen.entities.Player;
import com.github.sirkarpfen.entities.eventhandler.EntityContactEventHandler;
import com.github.sirkarpfen.entities.eventhandler.TeleportEventHandler;
import com.github.sirkarpfen.maps.MapHandler;
import com.github.sirkarpfen.screens.BattleScreen;
import com.github.sirkarpfen.screens.GameScreen;

public class RimGame extends Game {
	
	private KeyInputHandler keyInput;
	/* Helper class for dealing with tiled maps. Credits go to "dpk". */
	private MapHandler mapHandler;
	/* The event manager that handles all event delegation. */
	private EventManager eventManager;
	private GameScreen gameScreen;
	private SpriteBatch cameraBatch;
	public SpriteBatch getCameraBatch() { return cameraBatch; }
	private SpriteBatch staticBatch;
	public SpriteBatch getStaticBatch() { return staticBatch; }
	/* Camera used to display all scenes. */
	private OrthographicCamera camera;
	public OrthographicCamera getCamera() {
		return camera;
	}
	/* Represents the player character. */
	private Player player;
	private Box2DDebugRenderer debugRenderer;
	private BattleScreen battleScreen;
	public Box2DDebugRenderer getDebugRenderer() { return debugRenderer; }
	public Player getPlayer() { return player; }
	/* The world for all box2d bodies */
	private static World world;
	/**
	 * @return The world that contains all Box2D bodies.
	 */
	public static World getWorld() { return world; }
	
	/**
	 * Sets up the Map/Player and Camera. Camera is always centered on the player,
	 * when a new game is started, or a savegame loaded.
	 */
	@Override
	public void create() {
		// use GL10 without "power of two" enforcement.
		Texture.setEnforcePotImages(false);
		
		eventManager = EventManager.getInstance();
		eventManager.addTeleportEventListener(new TeleportEventHandler());
		
		keyInput = new KeyInputHandler();
		Gdx.input.setInputProcessor(keyInput);
		
		// prepares the OrthographicCamera, for projection.
		this.prepareCamera();
		
		// Creates the world, for our Box2D Entities.
		this.createWorld();
		
		// Set up the starting map, and load all maps into ram.
		this.loadMaps();
		
		// Initiates the player, and puts him on the Spawn point.
		this.createPlayer();
		
		// used for fast and smooth rendering.
		cameraBatch = new SpriteBatch();
		cameraBatch.setProjectionMatrix(camera.combined);
				
		staticBatch = new SpriteBatch();
		
		this.createScreens();
		
		this.setScreen(gameScreen);
	}
	
	private void createPlayer() {
		player = new Player("StartIsland", world, camera);
		if(!player.isSpawned()) {
			player.spawnPlayer();
		}
	}

	private void createWorld() {
		world = new World(new Vector2(0,0), true);
		world.setContactListener(new EntityContactEventHandler());
		debugRenderer = new Box2DDebugRenderer();
	}
	
	private void createScreens() {
		gameScreen = new GameScreen(this);
		battleScreen = new BattleScreen(this);
	}
	
	private void loadMaps() {
		mapHandler = MapHandler.getInstance();
		mapHandler.setWorld(world);
		mapHandler.setCamera(camera);
		mapHandler.loadMaps();
	}

	/*
	 * Prepares the OrthographicCamera and sets it on the startPosition.
	 */
	private void prepareCamera() {
		camera = new OrthographicCamera(Constants.WIDTH, Constants.HEIGHT);
		camera.update();
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
			} else if (keycode == Input.Keys.F9) {
				if(getScreen() instanceof GameScreen) {
					setScreen(battleScreen);
				} else if(getScreen() instanceof BattleScreen) {
					setScreen(gameScreen);
				}
				
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
			return false;
		}
	}

}
