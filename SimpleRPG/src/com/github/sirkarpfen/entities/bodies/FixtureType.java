package com.github.sirkarpfen.entities.bodies;

/**
 * Enum defining standard ways of fixture handling, when using BodyFactory.create...() methods.
 * 
 * @author sirkarpfen
 *
 */
public enum FixtureType {
	/**
	 * This type means a default density, friction and restitution.
	 * Typically set to a default value.
	 */
	NORMAL,
	/**
	 * This type means that after you created the body, you need to call BodyFactory.createFixture
	 * in order to create a fixture.
	 */
	CUSTOM,
}
