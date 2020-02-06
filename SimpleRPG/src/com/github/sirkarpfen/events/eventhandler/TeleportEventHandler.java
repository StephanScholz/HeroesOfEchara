package com.github.sirkarpfen.events.eventhandler;

import com.github.sirkarpfen.events.TeleportEvent;
import com.github.sirkarpfen.events.listeners.TeleportListener;
import com.github.sirkarpfen.main.RimGame;

public class TeleportEventHandler implements TeleportListener {

	private RimGame game;
	
	public TeleportEventHandler(RimGame game) {
		this.game = game;
	}
	
	@Override
	public void onPlayerTeleport(TeleportEvent e) {
		//TODO: Get map change done
		game.getPlayer().teleport(e.getPortal().getDestX(), e.getPortal().getDestY());
	}

}
