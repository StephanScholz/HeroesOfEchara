package com.github.sirkarpfen.events.listeners;

import java.util.EventListener;

import com.github.sirkarpfen.events.TeleportEvent;

public interface TeleportListener extends EventListener {
	/**
	 * Called when a teleport happens for the player.
	 * 
	 * @param The TeleportEvent that has occurred.
	 */
	void onPlayerTeleport(TeleportEvent e);
}
