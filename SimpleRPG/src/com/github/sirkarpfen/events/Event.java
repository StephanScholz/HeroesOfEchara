package com.github.sirkarpfen.events;

import java.util.EventObject;

/**
 * Base class for all events.
 * 
 * @author sirkarpfen
 *
 */
public class Event extends EventObject {
	
	private static final long serialVersionUID = 1L;

	public Event(Object source) {
		super(source);
	}
	
}
