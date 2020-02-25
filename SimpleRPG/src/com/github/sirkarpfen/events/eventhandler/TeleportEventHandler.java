package com.github.sirkarpfen.events.eventhandler;

import com.badlogic.gdx.maps.MapObject;
import com.github.sirkarpfen.entities.Portal;
import com.github.sirkarpfen.events.TeleportEvent;
import com.github.sirkarpfen.events.listeners.TeleportListener;
import com.github.sirkarpfen.main.EcharaGame;
import com.github.sirkarpfen.maps.MapStorage;

public class TeleportEventHandler implements TeleportListener {

	private EcharaGame game;
	
	public TeleportEventHandler(EcharaGame game) {
		this.game = game;
	}
	
	@Override
	public void onPlayerTeleport(TeleportEvent e) {
		//TODO: Get map change done
		MapStorage ms = MapStorage.getInstance();
		Portal portal = e.getPortal();
		MapObject o = ms.getActiveMap().getLayers().get("Objekte").getObjects().get(portal.getName());
		ms.setActiveMap(ms.getMap(o.getProperties().get("destination").toString()));
		game.getPlayer().teleport(e.getPortal().getDestX(), e.getPortal().getDestY());
	}

}
