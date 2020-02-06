package com.github.sirkarpfen.screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.github.sirkarpfen.entities.Entity;
import com.github.sirkarpfen.entities.MovingEntity;
import com.github.sirkarpfen.main.RimGame;
import com.github.sirkarpfen.maps.MapHandler;

/**
 * The base game class. Handles rendering/disposal/logic updates and more.
 * 
 * @author sirkarpfen
 * @see com.github.sirkarpfen.main.Main
 */
public class GameScreen extends BaseScreen {
	
	@SuppressWarnings ("unused")
	private Box2DDebugRenderer debugRenderer;

	private SpriteBatch cameraBatch;

	private SpriteBatch staticBatch;

	private OrthographicCamera camera;

	private long lastRender;

	private BitmapFont font;

	private MapHandler mapHandler;
	
	public GameScreen(RimGame game) {
		super(game);
		lastRender = System.nanoTime();
		font = new BitmapFont();
		this.cameraBatch = game.getCameraBatch();
		this.staticBatch = game.getStaticBatch();
		this.camera = game.getCamera();
		this.mapHandler = MapHandler.getInstance();
		this.debugRenderer = game.getDebugRenderer();
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
	public void render(float delta) {
		long now = System.nanoTime();
		
		Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		World world = RimGame.getWorld();
		world.step(1, 1, 1);
		
		// ********** Start rendering area. **********
		mapHandler.renderBackgroundMap(camera);
		
		this.renderWorldBodies();
		
		mapHandler.renderForegroundMap(camera);
		
		
		
		cameraBatch.begin();
		font.setScale(0.05F);
		font.draw(cameraBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 
				camera.position.x - camera.viewportWidth/2,
				camera.position.y - camera.viewportHeight/2);
		cameraBatch.end();
		// ********** End rendering area. **********
		
		// used for fps-limitation.
		now = System.nanoTime();
		if (now - lastRender < 30000000) { // 30 ms, ~33FPS
			try {
				Thread.sleep(30 - (now - lastRender) / 1000000);
			} catch (InterruptedException e) {
			}
		}
		
		
		// Destroy bodys, flagged as such
		this.destroyBodies();
		
		/*
		 * Draw this last, so we can see the collision boundaries on top of the
		 * sprites and map.
		 */
		debugRenderer.render(world, camera.combined);

		lastRender = now;
	}
	
	/*
	 * Renders the bodies for this world.
	 */
	private void renderWorldBodies() {
		
		cameraBatch.setProjectionMatrix(camera.combined);
		
		Iterator<Body> bi = RimGame.getWorld().getBodies();
        
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
		        if(e instanceof MovingEntity) {
		        	MovingEntity me = (MovingEntity)e;
		        	me.updateAnimations();
		        	me.move();
		        	me.render(cameraBatch);
		        }
		        // Render the Sprite
		        e.render(staticBatch);
		    }
		}
	}
	
	private void destroyBodies() {
		Iterator<Body> bi = RimGame.getWorld().getBodies();
		
		while (bi.hasNext()) {
			Body b = bi.next();
			Entity e = null;
			if (b.getUserData() instanceof Entity) {
				e = (Entity) b.getUserData();
				if(e.isFlaggedForDelete()) {
					RimGame.getWorld().destroyBody(b);
					b.setUserData(null);
					b = null;
					e.setFlaggedForDelete(false);
					System.out.println("deleted body");
				}
				if(e.isFlaggedForCreate()) {
					e.createBody();
					System.out.println("create body");
				}
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

	@Override
	public void show() {
		
		
	}

	@Override
	public void hide() {
		
	}
	

}
