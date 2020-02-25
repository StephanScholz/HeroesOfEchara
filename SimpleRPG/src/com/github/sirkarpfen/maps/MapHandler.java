package com.github.sirkarpfen.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.github.sirkarpfen.constants.Constants;

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
	private World world;
	public void setWorld(World world) { this.world = world; }
	
	private int[] backgroundLayerArray;
	private TiledMapTileLayer foregroundLayer;
	/**
	 * Sets the maximum amount of Layers. This amount is then used by the renderer, to render the
	 * Foreground and Background in the correct order. For simplicity, it is always necessary to declare the
	 * Foreground-layer as last. Using tile to create maps, you should strife to make the foreground the very last layer
	 * otherwise unforeseen things might happen.
	 * 
	 * @param count The maximum amount of Layers of the active map.
	 */
	public void setRenderLayers(int count) {
		backgroundLayerArray = new int[count-1];
		for (int i = 0; i < count-1; i++) {
			backgroundLayerArray[i] = i+1;
			System.out.println(backgroundLayerArray[i]);
		}
		foregroundLayer = mapStorage.getTileLayer("foreground");
	}
	
	
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
		renderer.render();
		camera.update();
	}
	
	/**
	 * Renders all layers, that obscure the entities.
	 */
	public void renderForegroundMap(OrthographicCamera camera) {
		 renderer.getMap().getLayers().get("Meta_data").setVisible(false);
		 renderer.renderTileLayer(foregroundLayer);
		 camera.update();
	}
	
	/**
	 * Loads all Maps from Disk to the mapTable. This Method should only
	 * be invoked once, when starting a new game or loading a savegame.
	 */
	public void loadMaps() {
		this.loadMap("StartIsland", "data/maps/StartIsland.tmx");
		this.loadMap("StartIslandInterior", "data/maps/interior/StartIslandInterior.tmx");
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
		this.createBodyTiles(map);
		mapStorage.putMap(mapName, map);
		mapStorage.putRenderer(map, renderer);
	}
	
	/**
	 * For every object, specified in the Maps "meta_data layer" that should not be walked over,
	 * or that can be manipulated in any way, this method creates a rigid Body (Static, non movable)
	 * to act collision detection on. Future objects may include npcs, chests, quest items and many more.
	 * 
	 * @param map The loaded map
	 */
	public void createBodyTiles(TiledMap map) {
		TiledMapTileLayer collisionLayer = (TiledMapTileLayer)map.getLayers().get("Meta_data");
		//StringBuffer buffer = new StringBuffer();
		//int tileCount = 0;
		for(int y = 0; y < collisionLayer.getWidth(); y++) {
			for(int x = 0; x < collisionLayer.getHeight(); x++) {
				Cell cell = collisionLayer.getCell(x, y);
				if(cell != null && 
						cell.getTile().getProperties().get("walkable") != null) {
					// Create our body definition
					BodyDef groundBodyDef = new BodyDef();
					groundBodyDef.type = BodyDef.BodyType.StaticBody;
					if(x == 0 && y == 0) {
						groundBodyDef.position.x = 0;
						groundBodyDef.position.y = 0;
					} else if(x == 0) {
						groundBodyDef.position.x = 0;
						groundBodyDef.position.y = y * collisionLayer.getTileHeight() + 7;
					} else if(y == 0){
						groundBodyDef.position.x = x * collisionLayer.getTileWidth() + 8;
						groundBodyDef.position.y = 0;
					} else {
						groundBodyDef.position.x = x * collisionLayer.getTileWidth()+8;
						groundBodyDef.position.y = y * collisionLayer.getTileHeight()+7;
					}
					//buffer.append("Tile Nr." + tileCount + " x: " + groundBodyDef.position.x + ", y: " + groundBodyDef.position.y + "\n");
					Body groundBody = world.createBody(groundBodyDef);
					FixtureDef def = new FixtureDef();
					PolygonShape shape = new PolygonShape();
					shape.setAsBox(collisionLayer.getTileWidth()/2,
							collisionLayer.getTileHeight()/2);
					def.shape = shape;
					def.filter.groupIndex = Constants.ENVIRONMENT_GROUP;
					groundBody.createFixture(shape, 20F);
					groundBody.setUserData(collisionLayer.getCell(x, y));
					groundBody.setSleepingAllowed(false);
					shape.dispose();
					//tileCount++;
				}
			}
		}
		//System.out.println(buffer);
	}
	
}
