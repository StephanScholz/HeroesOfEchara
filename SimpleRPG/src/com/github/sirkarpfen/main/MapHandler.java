package com.github.sirkarpfen.main;

import java.util.Hashtable;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.github.sirkarpfen.constants.Constants;
import com.github.sirkarpfen.entities.Entity;

/**
 * This class is a utility class, to perform all needed operations
 * on tiled maps.
 * 
 * @author sirkarpfen
 *
 */
public class MapHandler implements Viewable{
	
	/*
	 * The OrthographicCamera to use for projection.
	 */
	private OrthographicCamera camera;
	/*
	 * The map-renderer. Renders the maps, accordingly to their respective TiledMap-Objects
	 * TiledMaps are bind to only 1 renderer, so every new Map needs a new OrthogonalTiledMapRenderer-instance
	 */
	private OrthogonalTiledMapRenderer renderer;
	
	/**
	 * Stores all maps for this game.
	 */
	private Hashtable<String, TiledMap> mapTable = new Hashtable<String, TiledMap>();
	
	/**
	 * Gets a map from the mapTable
	 * 
	 * @return A Map found by the specified key or <code>null<code> if no map is found.
	 */
	public TiledMap getMap(String key) {
		return mapTable.get(key);
	}
	
	/**
	 * The Bounds used for collision testing. This 2 Dimensional Array stores
	 * information about which blocks are walkable and which are not.
	 * 
	 * @see com.github.sirkarpfen.main.MapHandler#setCollisionBounds(TiledMapTileLayer)
	 */
	public static float[][] collisionBounds;
	
	/*
	 * Stores all OrthogonalTiledMapRenderer, keyed by their respective TiledMap-Objects.
	 */
	private Hashtable<TiledMap, OrthogonalTiledMapRenderer> renderTable = new Hashtable<TiledMap, OrthogonalTiledMapRenderer>();
	
	/*
	 * References the map, the player is on.
	 */
	private TiledMap map;

	/**
	 * Renders all layers, that the entities should walk on.
	 */
	public void renderBackgroundMap() {
		camera.update();
		renderer.render(new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12});
		renderer.setView(camera);
	}
	
	/**
	 * Renders all layers, that obscure the entities.
	 */
	public void renderForegroundMap() {
		camera.update();
		renderer.render(new int[] {13});
	}

	/**
	 * Get a layer of this map.
	 * 
	 * @param layerName The name of the layer to search for.
	 * @return The layer if found. Otherwise null.
	 */
	public TiledMapTileLayer getTileLayer(String layerName) {
		return (TiledMapTileLayer)map.getLayers().get(layerName);
	}
	
	/**
	 * Loads all Maps from Disk to the mapTable. This Method should only
	 * be invoked once, when starting a new game or loading a savegame.
	 */
	public void loadMaps() {
		if(mapTable.isEmpty()) {
			this.loadMap("StartIsland", "data/maps/StartIsland.tmx");
		}
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
		OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map, Constants.UNIT_SCALE);
		mapTable.put(mapName, map);
		renderTable.put(map, renderer);
	}
	
	/**
	 * Get the currently active map. (Only returns an active Map, the player is on)
	 * 
	 * @return The currently active map.
	 */
	public TiledMap getActiveMap() {
		return map;
	}
	
	/**
	 * Sets the currently active map. This Method is only invoked, when
	 * the Player moves to another map, or spawns after the game launches or
	 * a savegame is loaded.
	 */
	public void setActiveMap(Entity entity, TiledMap map) {
		this.map = map;
		this.renderer = renderTable.get(map);
		TiledMapTileLayer collisionLayer = (TiledMapTileLayer)map.getLayers().get("Meta_data");
		entity.setCollisionLayer(collisionLayer);
		this.setCollisionBounds(collisionLayer);
		entity.getCollisionLayer().setVisible(false);
		map.getLayers().remove(entity.getCollisionLayer());
	}
	
	/**
	 * Sets the collision bounds to a 2 dimensional array.
	 * if one thinks of a map, this 2d array is very equal to the layer,
	 * which is used to specify the walkable tiles. Only here a <code>0</code>
	 * defines that a tile is walkable and a <code>1</code> defines a non
	 * walkable status.
	 * 
	 * @param collisionLayer The layer, where the collision tiles are specified.
	 */
	public void setCollisionBounds(TiledMapTileLayer collisionLayer) {
		collisionBounds = new float[collisionLayer.getWidth()][collisionLayer.getHeight()];
		for(int i = 0; i < collisionLayer.getWidth(); i++) {
			for(int j = 0; j < collisionLayer.getHeight(); j++) {
				if(collisionLayer.getCell(i, j).getTile().getProperties().get("walkable").equals("true")) {
					collisionBounds[i][j] = 0;
				} else {
					collisionBounds[i][j] = 1;
				}
			}
		}
	}

	@Override
	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;		
	}
}
