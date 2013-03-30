package com.github.sirkarpfen.main;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import com.github.sirkarpfen.events.TeleportEvent;
import com.github.sirkarpfen.events.listeners.TeleportListener;

/**
 * The EventManager handles all event delegation. it is also responsible, for
 * registering listeners and firing events.
 * 
 * @author sirkarpfen
 *
 */
public class EventManager {
	
	protected List<EventListener> listenerList = new ArrayList<EventListener>();
	
	private static EventManager instance = null;

	public static EventManager getInstance() {
		if(instance == null) {
			instance = new EventManager();
		}
		return instance;
	}
	
	/**
	 * Registers the listener to listen to teleport events.
	 * 
	 * @param listener The listener to be registered.
	 */
	public synchronized void addTeleportEventListener(EventListener listener) {
		listenerList.add(listener);
	}

	/**
	 * Removes the listener from this event handler.
	 * 
	 * @param listener The listener to be removed.
	 */
	public synchronized void removeTeleportEventListener(EventListener listener) {
		listenerList.remove(listener);
	}

	/**
	 * Fires a new TeleportEvent. This happens if the player is teleporter by command,
	 * or if the player leaves a map.
	 * 
	 * @param evt The TeleportEvent that occured.
	 */
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
