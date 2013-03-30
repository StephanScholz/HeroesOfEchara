package com.github.sirkarpfen.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.sirkarpfen.constants.Constants;
import com.github.sirkarpfen.entities.properties.Properties;

public class Portal extends MovingEntity {
	
	private Texture spriteSheet;
	private Animation animation;
	
	private final int FRAME_COLS = 3;
	private final int FRAME_ROWS = 1;
	private TextureRegion[] frames;
	private float stateTime;
	private TextureRegion currentFrame;
	
	public Portal(float x, float y) {
		this.prepareTextures();
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
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, x - currentFrame.getRegionWidth()/2, y - currentFrame.getRegionHeight()/2);
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
