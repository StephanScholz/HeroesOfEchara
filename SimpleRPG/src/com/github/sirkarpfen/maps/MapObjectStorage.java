package com.github.sirkarpfen.maps;

import java.util.ArrayList;
import java.util.Hashtable;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class MapObjectStorage {
	
	private static MapObjectStorage instance;
	
	private Hashtable<TiledMap, ArrayList<MapObject>> teleporterTable = new Hashtable<TiledMap, ArrayList<MapObject>>();
	
	public static MapObjectStorage getInstance() {
		if(instance == null) {
			instance = new MapObjectStorage();
		}
		return instance;
	}
	
	public void setTeleportList(TiledMap map, ArrayList<MapObject> teleporterList) {
		teleporterTable.put(map, teleporterList);
	}
	
	public ArrayList<MapObject> getTeleporterList(TiledMap map) {
		return teleporterTable.get(map);
	}
	
}
