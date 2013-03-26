package com.github.sirkarpfen.maps;

import java.util.Hashtable;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Singleton class to store all maps in.
 * 
 * @author sirkarpfen
 *
 */
public class MapStorage {
	
	private static MapStorage instance = null;
	
	/** Stores all maps for this game. */
	private Hashtable<String, TiledMap> mapTable = new Hashtable<String, TiledMap>();
	/* Stores all OrthogonalTiledMapRenderer, keyed by their respective TiledMap-Objects. */
	private Hashtable<TiledMap, OrthogonalTiledMapRenderer> renderTable = new Hashtable<TiledMap, OrthogonalTiledMapRenderer>();
	/* References the map, the player is on. */
	private TiledMap activeMap;
	private OrthogonalTiledMapRenderer activeRenderer;
	
	private MapStorage() {}
	
	/**
	 * @return The instance of this class.
	 */
	public static MapStorage getInstance() {
		if(instance == null) {
			instance = new MapStorage();
		}
		return instance;
	}
	
	/**
	 * Gets a map from the mapTable
	 * 
	 * @return A Map found by the specified key or <code>null<code> if no map is found.
	 */
	public TiledMap getMap(String key) {
		return mapTable.get(key);
	}
	
	/**
	 * Get a layer of this map.
	 * 
	 * @param layerName The name of the layer to search for.
	 * @return The layer if found. Otherwise null.
	 */
	public TiledMapTileLayer getTileLayer(String layerName) {
		return (TiledMapTileLayer)activeMap.getLayers().get(layerName);
	}
	
	/**
	 * @return the mapTable
	 */
	public Hashtable<String, TiledMap> getMapTable() {
		return mapTable;
	}

	/**
	 * @param mapTable the mapTable to set
	 */
	public void setMapTable(Hashtable<String, TiledMap> mapTable) {
		this.mapTable = mapTable;
	}

	public void putMap(String key, TiledMap value) {
		mapTable.put(key, value);
	}
	
	public void removeMap(String key) {
		mapTable.remove(key);
	}
	
	public void putRenderer(TiledMap map, OrthogonalTiledMapRenderer renderer) {
		renderTable.put(map, renderer);
	}
	
	public OrthogonalTiledMapRenderer getActiveRenderer() {
		return activeRenderer;
	}

	/**
	 * Get the currently active map. (Only returns an active Map, the player is on)
	 * 
	 * @return The currently active map.
	 */
	public TiledMap getActiveMap() {
		return activeMap;
	}
	
	/**
	 * Sets the currently active map. This Method is only invoked, when
	 * the Player moves to another map, or spawns after the game launches or
	 * a savegame is loaded.
	 */
	public void setActiveMap(TiledMap map) {
		this.activeMap = map;
		this.activeRenderer = renderTable.get(map);
	}
	
}
