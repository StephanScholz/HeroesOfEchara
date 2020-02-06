package com.github.sirkarpfen.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.github.sirkarpfen.constants.Constants;
import com.github.sirkarpfen.entities.bodies.BodyFactory;
import com.github.sirkarpfen.entities.properties.Properties;

public class Portal extends MovingEntity {
	
	private Texture spriteSheet;
	private Animation animation;
	
	private final int FRAME_COLS = 3;
	private final int FRAME_ROWS = 1;
	private TextureRegion[] frames;
	private float stateTime;
	private TextureRegion currentFrame;
	
	//Destination coordinates. Where does the portal lead to?
	private float destX, destY;
	public float getDestX() {return destX;}
	public float getDestY() {return destY;}
	
	public Portal(float x, float y, float destX, float destY, World world, OrthographicCamera camera) {
		super(world);
		this.x = x;
		this.y = y;
		this.destX = destX;
		this.destY = destY;
		this.prepareTextures();
		this.camera = camera;
		this.createBody();
	}
	
	@Override
	public void createBody() {
		body = BodyFactory.createBody(new Vector2(x,
				y), BodyType.StaticBody);
		CircleShape shape = BodyFactory.createCircleShape(frames[0].getRegionWidth()/4);
		BodyFactory.createFixture(body, shape, new float[]{1, 1, 1}, true, (short)Constants.ENVIRONMENT_GROUP);
		body.setUserData(this);
		body.setFixedRotation(true);
	}
	
	@Override
	public void move() {}

	@Override
	public void updateAnimations() {
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = animation.getKeyFrame(stateTime, true);
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		//camera.unproject(new Vector3(portalBody.getPosition().x, portalBody.getPosition().y, 0));
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, body.getPosition().x - currentFrame.getRegionWidth()/2,
				body.getPosition().y - currentFrame.getRegionHeight()/2);
		spriteBatch.end();
	}

	private void prepareTextures() {
		spriteSheet = new Texture(Properties.getString("portal_spritesheet")); //$NON-NLS-1$
		spriteSheet.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion[][] tmp = TextureRegion.split(
				spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);
		frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
        	for (int j = 0; j < FRAME_COLS; j++) {
        		frames[index++] = tmp[i][j];
        	}
        }
		animation = new Animation(Constants.ANIMATION_VELOCITY, frames);
		stateTime = 0F;
	}
	
	
}
