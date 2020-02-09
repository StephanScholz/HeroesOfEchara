package com.github.sirkarpfen.events.eventhandler;

import com.github.sirkarpfen.events.TeleportEvent;
import com.github.sirkarpfen.events.listeners.TeleportListener;
import com.github.sirkarpfen.main.EcharaGame;

public class TeleportEventHandler implements TeleportListener {

	private EcharaGame game;
	
	public TeleportEventHandler(EcharaGame game) {
		this.game = game;
	}
	
	@Override
	public void onPlayerTeleport(TeleportEvent e) {
		//TODO: Get map change done
		game.getPlayer().teleport(e.getPortal().getDestX(), e.getPortal().getDestY());
	}

}
