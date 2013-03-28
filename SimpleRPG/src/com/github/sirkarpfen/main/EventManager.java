package com.github.sirkarpfen.main;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import com.github.sirkarpfen.events.TeleportEvent;
import com.github.sirkarpfen.events.listeners.TeleportListener;

public class EventManager {
	protected List<EventListener> listenerList = new ArrayList<EventListener>();

	public synchronized void addTeleportEventListener(EventListener listener) {
		listenerList.add(listener);
	}

	public synchronized void removeTeleportEventListener(EventListener listener) {
		listenerList.remove(listener);
	}

	public synchronized void fireTeleportEvent(TeleportEvent evt) {
		if(listenerList.isEmpty()) {
			System.out.println("Bitte einen Teleport EventHandler registrieren");
		}
		for (EventListener listener : listenerList) {
			if (listener instanceof TeleportListener) {
				((TeleportListener) listener).onPlayerTeleport(evt);
			} else {
				System.out.println("Bitte einen Teleport EventHandler registrieren");
			}
		}
	}
}
