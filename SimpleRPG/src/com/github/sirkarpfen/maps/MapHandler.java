package com.github.sirkarpfen.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * A singleton utility class, to perform all needed operations on tiled maps.
 * 
 * @author sirkarpfen
 *
 */
public class MapHandler {
	
	private static MapHandler instance = null;
	/* The map-renderer. Renders the maps, accordingly to their respective TiledMap-Objects
	 * TiledMaps are bind to only 1 renderer, so every new Map needs a new OrthogonalTiledMapRenderer-instance */
	private OrthogonalTiledMapRenderer renderer;
	// The MapStorage
	private MapStorage mapStorage;
	
	
	private MapHandler() {
		mapStorage = MapStorage.getInstance();
	}
	/**
	 * Gets the instance of this singleton
	 */
	public static MapHandler getInstance() {
		if(instance == null) {
			instance = new MapHandler();
		}
		return instance;
	}
	
	/**
	 * Renders all layers, that the entities should walk on.
	 */
	public void renderBackgroundMap(OrthographicCamera camera) {
		if(renderer == null) {
			renderer = mapStorage.getActiveRenderer();
		}
		renderer.setView(camera);
		renderer.render(new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12});
		camera.update();
	}
	
	/**
	 * Renders all layers, that obscure the entities.
	 */
	public void renderForegroundMap(OrthographicCamera camera) {
		renderer.render(new int[] {13});
		camera.update();
	}
	
	/**
	 * Loads all Maps from Disk to the mapTable. This Method should only
	 * be invoked once, when starting a new game or loading a savegame.
	 */
	public void loadMaps() {
		this.loadMap("StartIsland", "data/maps/StartIsland.tmx");
	}

	/**
	 * Loads the requested tmx map file. The map and the fitting renderer
	 * are stored in the respective Hashtables.
	 * 
	 * @param mapName The name of the map to load.
	 * @param tmxFile The file to load.
	 */
	public void loadMap(String mapName, String tmxFile) {
		TiledMap map = new TmxMapLoader().load(tmxFile);
		OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map, 1);
		mapStorage.putMap(mapName, map);
		mapStorage.putRenderer(map, renderer);
	}
	
}
