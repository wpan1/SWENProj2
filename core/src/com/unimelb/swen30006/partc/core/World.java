package com.unimelb.swen30006.partc.core;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.partc.controllers.Controller;
import com.unimelb.swen30006.partc.controllers.KeyboardController;
import com.unimelb.swen30006.partc.core.infrastructure.Light;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;
import com.varunpant.quadtree.Point;
import com.varunpant.quadtree.QuadTree;

public class World implements ISteppable {

	// The visibility of objects within the world
	public static final int VISIBILITY_RADIUS = 100;
	// The maximum world width
	public static final float MAX_WIDTH = 800;
	// The maximum world height
	public static final float MAX_HEIGHT = 800;
	// The maximum distance a position can be from the road before being invalid
	private static final float MAX_ROAD_DISTANCE = 50f;
	// The colour of the world at midnight
	private static final Color MIDNIGHT_COLOUR = new Color(3/255f, 27/255f, 45/255f, 1f);
	// The colour of the world at midday
	private static final Color MIDDAY_COLOUR = Color.WHITE;
	// The 'hour' of midday
	private static final float MIDDAY = 15f;
	// The length of the day
	private static final float DAY_CYCLE = 2*MIDDAY;
	// Whether or not to enable debug control
	private static final boolean DEBUG_ENABLED = true;

	// Data structures for storing roads, objects etc
	public Controller[] controllers;
	public Road[] roads;
	public WorldObject[] staticObjects;
	public ISteppable[] steppableObjects;
	public float worldTime;	
	
	// Private variables for convenience and tracking
	private Intersection[] intersections;
	private WorldObject[] objects;
	private QuadTree worldObjectTree;
	private Car[] cars;	
	private Light[] lights;
	private ArrayList<Rectangle2D.Double> collisions;

	/**
	 * Instantiate a world object, making use of a hard coded test object 
	 * to create the map and retrieve the world objects and roads
	 */
	public World() {
		// Create a map reader
		MapReader reader = new MapReader("test_course.xml");

		// Retrieve the values from the map reader
		this.roads = reader.processRoads();
		this.objects = reader.processWorldObjects();
		this.intersections = reader.processIntersections();

		// Retrieve steppable objects and non stepable objects from world obejcts
		separateObjects();

		// Insert the values into the quadtree
		buildQuadTree();

		// Controllers and cars
		this.controllers = new Controller[1];
		this.cars = new Car[1];
		this.cars[0] = new Car(new Point2D.Double(80,140), 6, 10, Color.CORAL, 25f, 50f, 6f );
		this.controllers[0] = new KeyboardController(cars[0]);

		// Remaning variables
		this.worldTime = MIDDAY;
		this.collisions = new ArrayList<Rectangle2D.Double>();
	}

	public Intersection[] getIntersections() {
		return intersections;
	}

	/**
	 * Retrieve all world objects that exist
	 * @return all objects in the world
	 */
	public WorldObject[] getObjects(){
		return this.objects;
	}

	/**
	 * Find all objects viewable from the given position, depending on the world's VISIBILITY variable
	 * @param pos the position we are viewing things from
	 * @return all objects visible from that point, pos
	 */
	public WorldObject[] objectsAtPoint(Point2D.Double pos){
		// Generate the bounds for the search
		float xmin = (float) pos.x - VISIBILITY_RADIUS;
		float xmax = (float) pos.x + VISIBILITY_RADIUS;
		float ymin = (float) pos.y - VISIBILITY_RADIUS;
		float ymax = (float) pos.y + VISIBILITY_RADIUS;

		// Use the quadtree to find objects
		Point[] points = worldObjectTree.searchIntersect(xmin, ymin, xmax, ymax);

		// Create an array of WorldObjects to return
		WorldObject[] objects = new WorldObject[points.length];
		for(int i=0; i<objects.length; i++){
			Point p = points[i];
			objects[i] = (WorldObject) p.getValue();
		}
		return objects;
	}

	/**
	 * Find whether or not there is a road at a given point
	 * @param pos the point we want to check for
	 * @return the road that exists at that point, or null if none exists
	 */
	public Road roadAtPoint(Point2D.Double pos){
		for(Road r: this.roads){
			if(r.containsPoint(pos)){
				return r;
			}
		}
		return null;
	}

	/**
	 * Finds all roads within VISBILITY units from the current position
	 * @param pos the point we are searching from
	 * @return all roads within VISIBILITY units from the position
	 */
	public Road[] roadsAroundPoint(Point2D.Double pos){
		float xmin = (float) pos.x - VISIBILITY_RADIUS;
		float xmax = (float) pos.x + VISIBILITY_RADIUS;
		float ymin = (float) pos.y - VISIBILITY_RADIUS;
		float ymax = (float) pos.y + VISIBILITY_RADIUS;
		Rectangle2D.Double rect = new Rectangle2D.Double();
		rect.setFrameFromDiagonal(xmin, ymin, xmax, ymax);

		ArrayList<Road> visibleRoads = new ArrayList<Road>();
		for(Road r: this.roads){
			if(r.overlaps(rect)){
				visibleRoads.add(r);
			}
		}
		return visibleRoads.toArray(new Road[visibleRoads.size()]);
	}
	
	/**
	 * Find the intersection that exists at a point, for cases where we are not 
	 * on a road but need to navigate through the road structure
	 * @param pos the point we are searching from
	 * @return the intersection present at pos, or null if none is present
	 */
	public Intersection intersectionAtPoint(Point2D.Double pos){
		for(Intersection i: this.intersections){
			if(i.containsPoint(pos)){
				return i;
			}
		}
		return null;
	}

	/**
	 * Finds the closest road to a point and returns this road. Does not consider actual travel distance
	 * to a road, purely the direct distance
	 * @param pos the position to check from
	 * @return the closest road to that position, of null if none are within MAX_ROAD_DISTANCE
	 */
	public Road closestRoad(Point2D.Double pos){
		float minDist = Float.MAX_VALUE;
		Road minRoad = null;
		for(Road r: this.roads){
			float tmpDist = r.minDistanceTo(pos);
			if(tmpDist < minDist){
				minDist = tmpDist;
				minRoad = r;
			}
		}
		return (minDist < MAX_ROAD_DISTANCE) ? minRoad : null;
	}

	/**
	 * Update the world simulation, triggering all controllers, stepabble objects etc.
	 */
	@Override
	public void update(float delta) {
		// Update the world timer
		this.worldTime = (this.worldTime + delta) % DAY_CYCLE;

		// Update the environment
		updateEnvironment();

		// Update all controllers
		for(Controller c: controllers){
			c.update(delta);
		}

		// Update all dynamic objects 
		for(ISteppable s: steppableObjects){
			s.update(delta);
		}

		// FIND THE ROAD WE ARE ON
		if(DEBUG_ENABLED){
			processDebug();
		}

		// Check for car collisions
		for(Car c: this.cars){
			WorldObject[] objects = this.objectsAtPoint(c.getPosition());
			for(WorldObject o: objects){
				if(c.collidesWith(o)){
					this.collisions.add(o.boundary);
					this.collisions.add(c.boundary);
				}
			}
		}
	}

	/**
	 * Render the world using the provided renderer
	 * @param shapeRenderer the shape renderer to render things with
	 */
	public void render(ShapeRenderer shapeRenderer) {
		// Render the environment colour
		Color old = shapeRenderer.getColor();
		shapeRenderer.setColor(getEnvironmentColour());
		shapeRenderer.rect(0, 0, MAX_WIDTH, MAX_HEIGHT);
		shapeRenderer.setColor(old);

		// Render the roads and intersections
		for(Road r: roads){
			r.render(shapeRenderer);
		}
		for(Intersection i: intersections){
			i.render(shapeRenderer);
		}
		// Render all objects
		for(WorldObject o: objects){
			o.render(shapeRenderer);
		}
		// Render all the cars
		for(Car c: cars){
			c.render(shapeRenderer);
		}

		// Render all collisions for debug
		shapeRenderer.setColor(Color.RED);
		for(Rectangle2D.Double r: this.collisions){
			shapeRenderer.rect((float)r.x, (float)r.y, (float)r.getWidth(), (float)r.getHeight());
		}
		this.collisions.clear();
	}
	
	/** 
	 * Find the point that the camera should focus on if it is in tracking mode
	 * @return the point of interest
	 */
	public Point2D.Double getPointOfInterest(){
		return cars[0].getPosition();
	}

	/**
	 * Process all debug output and print to console with the debug helper strings.
	 */
	private void processDebug(){
		if(Gdx.input.isKeyPressed(Keys.R)){
			Road r = closestRoad(cars[0].getPosition());
			if(r!=null){
				System.out.println("Closest Road: " + r + " at distance " + r.minDistanceTo(cars[0].getPosition()));
			} else {
				System.out.println("No road found within " + MAX_ROAD_DISTANCE + " of " + cars[0].getPosition());
			}
		}
		if(Gdx.input.isKeyPressed(Keys.P)){
			System.out.println("Road we are on: " + roadAtPoint(cars[0].getPosition()));
		}
		if(Gdx.input.isKeyPressed(Keys.I)){
			System.out.println("Intersection we are on: " + intersectionAtPoint(cars[0].getPosition()));
		}
		if(Gdx.input.isKeyPressed(Keys.U)){
			Road[] rs = roadsAroundPoint(cars[0].getPosition());
			System.out.println("Found " + rs.length + " roads around " + cars[0].getPosition() + ":");
			for(Road r: rs){
				System.out.println("Road: " + r + " at distance " + r.minDistanceTo(cars[0].getPosition()));
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.O)){
			WorldObject[] objs = objectsAtPoint(cars[0].getPosition());
			System.out.println("Found " + objs.length + " objects within " + VISIBILITY_RADIUS + " of " + cars[0].getPosition() + ":");
			for(WorldObject o: objs){
				System.out.println("Object: " + o + " at " + o.getPosition());
			}
		}

	}

	/**
	 * Update the world environment, including lighting.
	 */
	private void updateEnvironment(){
		if(((this.worldTime) < (MIDDAY/2)) || ((this.worldTime) > (MIDDAY + MIDDAY/2))){
			for(Light l: this.lights){
				l.turnOn();
			}
		} else {
			for(Light l: this.lights){
				l.turnOff();
			}
		}
	}

	/**
	 * Get the global colour for the whole environment for use in rendering the sky's
	 * transitionary colours
	 * @return
	 */
	public Color getEnvironmentColour(){
		Color env;
		if((this.worldTime) < MIDDAY){
			env = MIDNIGHT_COLOUR.cpy().lerp(MIDDAY_COLOUR, (this.worldTime)/MIDDAY);
		} else {
			env = MIDDAY_COLOUR.cpy().lerp(MIDNIGHT_COLOUR, ((this.worldTime)-MIDDAY)/MIDDAY);
		}
		return new Color(env.r, env.g, env.b, 1f);
	}

	/**
	 * Separate out all of the various World Object elements into their respective data structures
	 * for reference and manipulation independently of eachother. Only stores references, does not 
	 * manipulate the core data structure
	 */
	private void separateObjects (){
		ArrayList<ISteppable> tempSteppable = new ArrayList<ISteppable>();
		ArrayList<WorldObject> tempWorldObject = new ArrayList<WorldObject>();
		ArrayList<Light> tempLights = new ArrayList<Light>();
		// Sort obejcts
		for(WorldObject o: this.objects){
			if(o instanceof ISteppable){
				tempSteppable.add((ISteppable) o);
			} else {
				if(o instanceof Light){
					tempLights.add((Light)o);
				}
				tempWorldObject.add(o);
			}
		}

		// Assign arrays
		this.lights = tempLights.toArray(new Light[tempLights.size()]);
		this.steppableObjects = tempSteppable.toArray(new ISteppable[tempSteppable.size()]);
		this.staticObjects = tempWorldObject.toArray(new WorldObject[tempWorldObject.size()]);
	}

	/**
	 * Builds the Quadtree used for object collision and fast retrieval of objects for roadsAtPoint
	 */
	private void buildQuadTree(){
		worldObjectTree = new QuadTree(0, 0, MAX_WIDTH, MAX_HEIGHT);

		// After generating all world objects, add them to the quad tree so we can find them
		for(WorldObject o: this.objects){
			this.worldObjectTree.set(o.getPosition().x, o.getPosition().y, o);
		}

	}	

}
