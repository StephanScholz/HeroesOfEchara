package com.github.sirkarpfen.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.github.sirkarpfen.constants.Constants;
import com.github.sirkarpfen.entities.properties.Properties;

/**
 * This class represents the player character along with all respective values and images.
 * Handles animation and rendering, as well as movement, collision detection and processing.
 * 
 * @author sirkarpfen
 *
 */
public class Player extends MovingEntity {
	
	private TiledMap startMap;
	private boolean spawned = false;
	/** Indicates whether the player has already spawned, or not */
	public boolean spawned() { return spawned; }
	/** Sets whether the player has spawned, or not */
	public void setSpawned(boolean spawned) { this.spawned = spawned; }
	
	private boolean pressedKey = false;
	/** Sets if the player has pressed a Button. */
	public void setPressedKey(boolean flag) { this.pressedKey = flag; }
	/** Returns true if the player has pressed a Button */
	public boolean hasPressedKey() { return pressedKey; }
	
	// Indicates the time between frames when rendering the animation.
	private float stateTime;
	// Column amount of the spritesheet, used for animating.
	private final int FRAME_COLS = 3;
	// Row amount of the spritesheet, used for animating.
	private final int FRAME_ROWS = 4;
	// the spritesheet to use for the animation.
	
	private Texture playerSheet;
	// All those arrays are needed to switch animation direction, if the user
	// switches movement direction.
	private TextureRegion[] currentWalkFrames, walkFramesDown, walkFramesLeft, walkFramesRight, walkFramesUp;
	// The current frame to be rendered in this run.
	private TextureRegion currentFrame;
	// The actual Animation object to use for this animation.
	private Animation walkAnimation;
	// the direction the player moves.
	private Direction movingDirection;
	/** Sets the direction this player moves to. */
	public void setMovingDirection(Direction direction) { this.movingDirection = direction; }
	/** Gets the direction this player moves to. */
	public Direction getMovingDirection() { return movingDirection; }
	
	public Player(String startMapName, World world) {
		super(world);
		this.startMap = mapStorage.getMap(startMapName);
	}
	
	/**
	 * Spawns the player on the spawn location, specified inside the maps
	 * object-layer
	 */
	public void spawnPlayer() {
		this.prepareTextures();
		this.findSpawnLocation();
		this.createBody();
		this.setMovingDirection(Direction.DOWN);
		this.setSpawned(true);
	}
	
	@Override
	public void createBody() {
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesnt move we would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
		// Set fixed rotation, so that collision detection will work properly
		bodyDef.fixedRotation = true;
		// Set our body's starting position in the world
		bodyDef.position.set(x, y);
		// Create our body in the world using our body definition
		body = world.createBody(bodyDef);
		/**
	     * Boxes are defined by their "half width" and "half height", hence the
		 * 2 multiplier.
		 */
		CircleShape shape = new CircleShape();
		System.out.println(currentWalkFrames[1]);
		shape.setRadius(currentWalkFrames[1].getRegionWidth() / 2 - 2);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1F;
		fixtureDef.friction = 1F;
		fixtureDef.filter.groupIndex = Constants.PLAYER_GROUP;
		body.createFixture(fixtureDef);
		shape.dispose();
		body.setUserData(this);
		flaggedForCreate = false;
	}
	
	private void prepareTextures() {
		playerSheet = new Texture(Properties.getString("player_spritesheet"));
		playerSheet.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion[][] tmp = TextureRegion.split(playerSheet, playerSheet.getWidth() /
				FRAME_COLS, playerSheet.getHeight() / FRAME_ROWS);
		
		walkFramesDown = new TextureRegion[FRAME_COLS];
		walkFramesLeft = new TextureRegion[FRAME_COLS];
		walkFramesRight = new TextureRegion[FRAME_COLS];
		walkFramesUp = new TextureRegion[FRAME_COLS];
		
		for (int i = 0; i < FRAME_COLS; i++) {
			walkFramesDown[i] = tmp[0][i];
			walkFramesLeft[i] = tmp[1][i];
			walkFramesRight[i] = tmp[2][i];
			walkFramesUp[i] = tmp[3][i];
		}
		currentWalkFrames = walkFramesDown;
		walkAnimation = new Animation(Constants.ANIMATION_VELOCITY, currentWalkFrames);
		stateTime = 0F;
	}
	
	// Searches the object layer, and within the spawn location.
	private void findSpawnLocation() {
		MapObject o = startMap.getLayers().get("Objekte").getObjects().get("SpawnPoint");
		if(o != null) {
			mapStorage.setActiveMap(startMap);
			
			Rectangle rect = ((RectangleMapObject) o).getRectangle();
			float tempX = rect.getX();
			float tempY = rect.getY();
			// Float.parseFloat(object.getProperties().get("y").toString())
			
			this.x = tempX + currentWalkFrames[1].getRegionWidth()/2;
			this.y = tempY + currentWalkFrames[1].getRegionHeight()/2;
			
		}
	}
	
	@Override
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, x - currentFrame.getRegionWidth()/2,
				y - currentFrame.getRegionHeight()/2);
		
		spriteBatch.end();
	}
	
	@Override
	public void updateAnimations() {
		if (flaggedForDelete) return;
		/*
		 * Currently very dirty.
		 * There needs to be a way, to do this, without always initializing a
		 * new animation object.
		 */
		walkAnimation = new Animation(Constants.ANIMATION_VELOCITY, currentWalkFrames);
		camera.position.set(body.getPosition().x, body.getPosition().y, 0);
		camera.update();
		x = body.getPosition().x;
		y = body.getPosition().y;
		if(this.hasPressedKey()) {
			
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = walkAnimation.getKeyFrame(stateTime, true);
			
		} else {
			
			currentFrame = currentWalkFrames[1]; // 1 is the position of the "standing" sprite.
			stateTime = 0;
			this.setMovingDirection(Direction.STAND);
			
		}
	}
	 	
	@Override
	public void move() {
		
		//System.out.println("x: " + x + ", y: " + y);
		
		switch(movingDirection) {
		
		case LEFT:
			currentWalkFrames = walkFramesLeft;
			body.setLinearVelocity(new Vector2(-Constants.WALKING_VELOCITY, 0));
			break;
		case RIGHT:
			
			currentWalkFrames = walkFramesRight;
			body.setLinearVelocity(new Vector2(Constants.WALKING_VELOCITY, 0));
			break;
		case UP:
			
			currentWalkFrames = walkFramesUp;
			body.setLinearVelocity(new Vector2(0, Constants.WALKING_VELOCITY));
			break;
		case DOWN:
			
			currentWalkFrames = walkFramesDown;
			body.setLinearVelocity(new Vector2(0, -Constants.WALKING_VELOCITY));
			break;
		case STAND:
			body.setLinearVelocity(new Vector2(0,0));
			break;
		}
	}
	public void teleport(float destX, float destY) {
		this.x = destX;
		this.y = destY;
		flaggedForDelete = true;
		flaggedForCreate = true;
		camera.update();
		
	}
	
}
