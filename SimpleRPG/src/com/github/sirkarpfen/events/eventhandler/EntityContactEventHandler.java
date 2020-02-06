package com.github.sirkarpfen.events.eventhandler;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.github.sirkarpfen.entities.Portal;
import com.github.sirkarpfen.events.TeleportEvent;
import com.github.sirkarpfen.main.EventManager;

public class EntityContactEventHandler implements ContactListener {
	
	@Override
	public void beginContact(Contact contact) {
		Object actorA = contact.getFixtureA().getBody().getUserData();
		Object actorB = contact.getFixtureB().getBody().getUserData();
		if (actorA instanceof Portal) {
			EventManager.getInstance().fireTeleportEvent(new TeleportEvent(actorA));
		}
		if (actorB instanceof Portal) {
			EventManager.getInstance().fireTeleportEvent(new TeleportEvent(actorB));
		}
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}
