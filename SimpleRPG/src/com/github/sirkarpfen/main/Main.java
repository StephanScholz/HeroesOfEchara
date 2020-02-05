package com.github.sirkarpfen.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Class containing only the main method, to start the application.
 * 
 * @author sirkarpfen
 * @see com.github.sirkarpfen.main.Game
 */
public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "SimpleRPG";
		config.width = 800;
		config.height = 600;
		//config.useGL20 = true; //this is important
		new LwjglApplication(new RimGame(), config);
	}
}
