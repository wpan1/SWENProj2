package com.unimelb.swen30006.partc.core;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.unimelb.swen30006.partc.core.infrastructure.Light;
import com.unimelb.swen30006.partc.core.infrastructure.Sign;
import com.unimelb.swen30006.partc.core.infrastructure.TrafficLight;
import com.unimelb.swen30006.partc.core.objects.Building;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;
/**
 * This class provides functionality for use within the simulation system. It is NOT intended to be
 * read or understood for SWEN30006 Part C. Comments have been intentionally removed to reinforce
 * this. We take no responsibility if you use all your time trying to understand this code.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public class MapReader {

	// Private variables for storing state and where to get data from
	private String fileName;
	private boolean intialised;

	// Private data structures for loading things
	private ArrayList<WorldObject> objects;
	private ArrayList<Road> roads;
	private HashMap<String, Intersection> intersections;

	public MapReader(String file) {
		this.fileName = file;
		this.intialised = false;
		this.intersections = new HashMap<String, Intersection>();
	}

	public Road[] processRoads(){
		if(!this.intialised) { initialise(); }
		return this.roads.toArray(new Road[this.roads.size()]);
	}

	public WorldObject[] processWorldObjects(){
		if(!this.intialised) { initialise(); }
		return this.objects.toArray(new WorldObject[this.objects.size()]);
	}

	public Intersection[] processIntersections(){
		if(!this.intialised) { initialise(); }
		return this.intersections.values().toArray(new Intersection[this.intersections.size()]);
	}

	private void initialise(){
		try {
			// Build the doc factory
			FileHandle file = Gdx.files.internal(fileName);			
			XmlReader reader = new XmlReader();
			Element root = reader.parse(file);

			// Setup data structures
			this.objects = new ArrayList<WorldObject>();
			this.roads = new ArrayList<Road>();

			// Process buildings
			Element stations = root.getChildByName("buildings");
			Array<Element> buidlingList = stations.getChildrenByName("building");
			for(Element e : buidlingList){
				this.objects.add(processBuilding(e));
			}

			// Process Infrastructure
			Element infrastructure = root.getChildByName("infrastructure");
			processInfrastructure(infrastructure);

			// Process Intersections
			Element intersections = root.getChildByName("intersections");
			Array<Element> intersectionList = intersections.getChildrenByName("intersection");
			for(Element e : intersectionList){
				processIntersection(e);
			}

			// Process Roads
			Element roads = root.getChildByName("roads");
			Array<Element> roadList = roads.getChildrenByName("road");
			for(Element e : roadList){
				this.roads.add(processRoad(e));
			}

			this.intialised = true;
		} catch (Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void processInfrastructure(Element infrastructure){
		// Process signs
		Element signs = infrastructure.getChildByName("signs");
		Array<Element> signList = signs.getChildrenByName("sign");
		for(Element e : signList){
			this.objects.add(processSign(e));
		}

		// Process Traffic Lights
		Element trafficLights = infrastructure.getChildByName("traffic_lights");
		Array<Element> trafficLightLists = trafficLights.getChildrenByName("light");
		for(Element e : trafficLightLists){
			this.objects.add(processTrafficLight(e));
		}

		// Process Street Lamps
		Element lights = infrastructure.getChildByName("street_lights");
		Array<Element> lightList = lights.getChildrenByName("light");
		for(Element e : lightList){
			this.objects.add(processLight(e));
		}

	}

	private Sign processSign(Element sign){
		float xLoc = sign.getFloat("x_loc");
		float yLoc = sign.getFloat("y_loc");
		float width = sign.getFloat("width");
		float height = sign.getFloat("height");
		String text = sign.get("text");

		Element c = sign.getChildByName("colour");
		// Extract colour and add alpha
		Color col = extractColour(c);

		Point2D.Double point = new Point2D.Double(xLoc, yLoc);
		return new Sign(point, width, height, col, text);
	}

	private TrafficLight processTrafficLight(Element lightElement){
		float xLoc = lightElement.getFloat("x_loc");
		float yLoc = lightElement.getFloat("y_loc");
		TrafficLight.State state = lightElement.get("initial_state").equals("red") ? TrafficLight.State.Red : TrafficLight.State.Green;
		float cycleLength = lightElement.getFloat("cycle_length");
		return new TrafficLight(new Point2D.Double(xLoc, yLoc), state, cycleLength);
	}

	private Light processLight(Element lightElement){
		float xLoc = lightElement.getFloat("x_loc");
		float yLoc = lightElement.getFloat("y_loc");
		float radius = lightElement.getFloat("radius");
		float brightness = lightElement.getFloat("brightness");
		Element c = lightElement.getChildByName("colour");

		// Extract colour and add alpha
		Color col = extractColour(c);
		col = new Color(col.r, col.g, col.b, 0.7f);
		return new Light(new Point2D.Double(xLoc, yLoc), col, radius, brightness);
	}

	private Road processRoad(Element roadElement){
		// Retrieve data
		float startX = roadElement.getFloat("start_x");
		float startY = roadElement.getFloat("start_y");
		float endX = roadElement.getFloat("end_x");
		float endY = roadElement.getFloat("end_y");
		float width = roadElement.getFloat("width");
		int numLanes = roadElement.getInt("num_lanes");

		// Create data types
		Point2D.Double startPos = new Point2D.Double(startX, startY);
		Point2D.Double endPos = new Point2D.Double(endX, endY);

		// Create the road
		Road r = new Road(startPos, endPos, width, numLanes, new int[]{0,0});

		// Register the intersections
		Element intersection = roadElement.getChildByName("intersections");
		Element startIntersection = intersection.getChildByName("start");
		Element endIntersection = intersection.getChildByName("end");
		if(startIntersection != null){
			assignIntersection(startIntersection, r);
		}
		if(endIntersection != null){
			assignIntersection(endIntersection, r);
		}

		// Return road
		return r;
	}

	private void assignIntersection(Element intersectionElement, Road r){
		String intersectionID = intersectionElement.get("id");
		String direction = intersectionElement.get("direction");
		Intersection intersection = this.intersections.get(intersectionID);
		if(intersection!=null){
			intersection.registerRoad(getDirection(direction), r);
		} else {
			System.err.println("Incorrectly formatted XML Document");
			System.exit(0);
		}
	}

	private Building processBuilding(Element buildingElement){
		float xLoc = buildingElement.getFloat("x_loc");
		float yLoc = buildingElement.getFloat("y_loc");
		float width = buildingElement.getFloat("width");
		float height = buildingElement.getFloat("height");	
		Color lerp = Math.random() > 0.5 ? Color.DARK_GRAY.cpy() : Color.LIGHT_GRAY.cpy();
		Color col = Color.FIREBRICK.cpy().lerp(lerp, (float) Math.random());
		return new Building(new Point2D.Double(xLoc, yLoc), width, height, col);
	}

	private Intersection.Direction getDirection(String s){
		if(s.equals("north")){ 
			return Intersection.Direction.North;
		} else if(s.equals("south")){
			return Intersection.Direction.South;
		} else if(s.equals("west")){
			return Intersection.Direction.West;
		} else if(s.equals("east")){
			return Intersection.Direction.East;
		}
		return null;
	}

	private void processIntersection(Element intersectionElement){
		// Retrieve all the data
		String roadID = intersectionElement.get("intersection_id");
		float x_pos = intersectionElement.getFloat("start_x");
		float y_pos = intersectionElement.getFloat("start_y");
		float width = intersectionElement.getFloat("width");
		float height = intersectionElement.getFloat("height");

		// Create the intersection
		Intersection i = new Intersection(new Point2D.Double(x_pos, y_pos), width, height);
		this.intersections.put(roadID, i);		
	}

	private Color extractColour(Element e){
		float red = e.getFloat("red")/255f;
		float green = e.getFloat("green")/255f;
		float blue = e.getFloat("blue")/255f;
		return new Color(red, green, blue, 1f);
	}
}
