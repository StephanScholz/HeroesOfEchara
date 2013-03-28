package com.github.sirkarpfen.events.listeners;

import java.util.EventListener;

import com.github.sirkarpfen.events.TeleportEvent;

public interface TeleportListener extends EventListener {
	void onPlayerTeleport(TeleportEvent e);
}
