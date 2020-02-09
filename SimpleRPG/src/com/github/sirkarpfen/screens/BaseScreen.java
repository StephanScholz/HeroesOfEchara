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
	public void render(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
