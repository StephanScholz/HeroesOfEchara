package com.github.sirkarpfen.events;

import com.github.sirkarpfen.entities.Portal;

/**
 * Represents a teleport event.
 * 
 * @author sirkarpfen
 *
 */
public class TeleportEvent extends Event {

	private static final long serialVersionUID = 1L;

	private Portal portal;
	public Portal getPortal() {return portal;}
	
	public TeleportEvent(Object source) {
		super(source);
		portal = (Portal)source;
	}
	
	

}
