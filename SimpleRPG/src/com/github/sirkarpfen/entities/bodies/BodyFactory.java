package com.github.sirkarpfen.entities.bodies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.github.sirkarpfen.main.EcharaGame;

public class BodyFactory {
	
	private static World world = EcharaGame.getWorld();
	
	/**
	 * Creates a Polygon-formed body. Bodies returned with this method, are always boxes.
	 * If a polygon that is no box is needed, you have to use <code>FixtureType.CUSTOM</code> and create
	 * the shape on your own.
	 * <p>
	 * In case of <code>FixtureType.CUSTOM</code> no shape is created. You have to call createPolygonShape()
	 * to create a shape, you can then use the shape in the method createFixture().
	 * 
	 * @param location The location of this body in the world.
	 * @param boxWidth The width of the box to be created
	 * @param boxHeight The height of the box to be created
	 * @param bodyType The <code>BodyType</code> of the polygon shape.
	 * @param fixtureType The <code>FixtureType</code> to use.
	 * @return The created body.
	 * 
	 * @see com.github.sirkarpfen.entities.bodies.FixtureType
	 */
	public static Body createPolygonBody(
			Vector2 location, float boxWidth, float boxHeight, BodyType bodyType, FixtureType fixtureType) {
		
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesnt move we would set it to StaticBody
		bodyDef.type = bodyType;
		// Set our body's starting position in the world
		bodyDef.position.set(location);
		// Create our body in the world using our body definition
		Body body = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(boxWidth, boxHeight);
		
		switch(fixtureType) {
		
		case NORMAL:
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = shape;
			fixtureDef.density = 1F;
			fixtureDef.friction = 1F;
			body.createFixture(fixtureDef);
			break;
		case CUSTOM:
			break;
		}
		
		shape.dispose();
		return body;
		
	}
	
	/**
	 * Creates a circle shaped body.
	 * <p>
	 * In case of <code>FixtureType.CUSTOM</code> no shape is created. You have to call createCircleShape()
	 * to create a shape, you can then use the shape in the method createFixture().
	 * 
	 * @param location The location of this body in the world
	 * @param radius The radius of the circle
	 * @param bodyType The <code>BodyType</code> of the circle shape
	 * @param fixtureType The <code>FixtureType</code> of the circle shape.
	 * @return The created body
	 */
	public static Body createCircleBody(Vector2 location, float radius, BodyType bodyType, FixtureType fixtureType) {
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesnt move we would set it to StaticBody
		bodyDef.type = bodyType;
		// Set our body's starting position in the world
		bodyDef.position.set(location);
		// Create our body in the world using our body definition
		Body body = world.createBody(bodyDef);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(radius);
		
		switch(fixtureType) {
		
		case NORMAL:
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = shape;
			fixtureDef.density = 1F;
			fixtureDef.friction = 1F;
			body.createFixture(fixtureDef);
			break;
		case CUSTOM:
			break;
		}
		
		shape.dispose();
		return body;
	}
	
	/**
	 * Creates a body without shape. To create a shape, you need to call one
	 * of the shape methods (e.g. createCircleShape())
	 * 
	 * @param location The location of the body
	 * @param bodyType The <code>BodyType</code> of the body
	 * @return The created body
	 */
	public static Body createBody(Vector2 location, BodyType bodyType) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.position.set(location);
		return world.createBody(bodyDef);
	}
	
	/**
	 * Creates an edge shaped body.
	 * <p>
	 * In case of <code>FixtureType.CUSTOM</code> no shape is created. You have to call createEdgeShape()
	 * to create a shape, you can then use the shape in the method createFixture().
	 * 
	 * @param location The location of this body in the world
	 * @param v1 The first vertex of this edge shape
	 * @param v2 The second vertex of this edge shape
	 * @param bodyType The <code>BodyType</code> of this edge shape
	 * @param fixtureType The <code>FixtureType</code> of this edge shape
	 * @return The created body
	 */
	public static Body createEdgeBody(
			Vector2 location, Vector2 v1, Vector2 v2, BodyType bodyType, FixtureType fixtureType) {
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesnt move we would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(location);
		// Create our body in the world using our body definition
		Body body = world.createBody(bodyDef);
		
		EdgeShape shape = new EdgeShape();
		shape.set(v1, v2);
		
		switch(fixtureType) {
		
		case NORMAL:
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = shape;
			fixtureDef.density = 1F;
			fixtureDef.friction = 1F;
			body.createFixture(fixtureDef);
			break;
		case CUSTOM:
			break;
		}
		
		shape.dispose();
		return body;
	}
	
	/**
	 * Create a custom fixture with the given params. This method has to be called,
	 * when choosing <code>FixtureType.CUSTOM</code>, if a fixture has to be created.
	 * 
	 * The fixtureConfigs array contains the density, friction and restitution, that
	 * this fixture will be created with. It has to be delivered as followed:
	 * <p>
	 * fixtureConfigs[0] = density<br>
	 * fixtureConfigs[1] = friction<br>
	 * fixtureConfigs[2] = restitution
	 * 
	 * @param body The body for this fixture
	 * @param fixtureConfigs The Array containing the density, friction and restitution to use.
	 * @param isSensor If the body should be a sensor.
	 * @param bodyGroup The group this body has to be assigned to. 0 if no group has to be assigned.
	 */
	public static Fixture createFixture(Body body, Shape shape, float[] fixtureConfigs, boolean isSensor, short bodyGroup) {
		FixtureDef def = new FixtureDef();
		def.shape = shape;
		def.density = fixtureConfigs[0];
		def.friction = fixtureConfigs[1];
		def.restitution = fixtureConfigs[2];
		def.isSensor = isSensor;
		def.filter.groupIndex = bodyGroup;
		return body.createFixture(def);
	}
	
	/**
	 * Creates a polygonal shape.
	 * 
	 * @param vertices The vertices used for the polygon
	 * @param offset The offset used for this polygon
	 * @param length The length used for this polygon
	 * @return The polygon shape.
	 */
	public static PolygonShape createPolygonShape(float[] vertices, int offset, int length) {
		PolygonShape shape = new PolygonShape();
		if(offset == 0 || length == 0) {
			shape.set(vertices);
		} else {
			shape.set(vertices, offset, length);
		}
		return shape;
	}
	
	/**
	 * Creates a polygonal box.
	 * 
	 * @param halfWidth The half width of the box
	 * @param halfHeight The half height of the box
	 * @return The polygonal box shape
	 */
	public static PolygonShape createBoxShape(float halfWidth, float halfHeight) {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(halfWidth, halfHeight);
		return shape;
	}
	
	/**
	 * Creates a circle shape
	 * 
	 * @param radius The radius used for this shape.
	 * @return The circle shape.
	 */
	public static CircleShape createCircleShape(float radius) {
		CircleShape shape = new CircleShape();
		shape.setRadius(radius);
		return shape;
	}
	
	/**
	 * Creates an edged shape.
	 * 
	 * @param v1 The first vertex
	 * @param v2 The second vertex
	 * @return The edge shape.
	 */
	public static EdgeShape createEdgeShape(Vector2 v1, Vector2 v2) {
		EdgeShape shape = new EdgeShape();
		shape.set(v1, v2);
		return shape;
	}
	
}
