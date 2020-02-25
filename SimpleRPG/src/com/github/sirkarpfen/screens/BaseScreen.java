package com.github.sirkarpfen.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.sirkarpfen.constants.Constants;
import com.github.sirkarpfen.main.EcharaGame;

public class BaseScreen implements Screen {
	
	protected EcharaGame game;
	
	private Stage stage;

	public BaseScreen(EcharaGame game) {
		this.game = game;
		this.stage = new Stage(Constants.WIDTH, Constants.HEIGHT, true);
	}
	
	@Override
	public void render(float delta) {}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}

}
