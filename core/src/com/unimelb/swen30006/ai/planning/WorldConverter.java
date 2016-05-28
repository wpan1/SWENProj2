package com.unimelb.swen30006.ai.planning;

import java.awt.geom.Point2D;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.unimelb.swen30006.partc.core.MapReader;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;

public class WorldConverter {
	// Private variables for storing state and where to get data from
	private String fileName;
	
	// Private data variable for minimum road distance
	private final float MAX_ROAD_DISTANCE = 50;

	// Private data structures for loading things
	private ArrayList<Road> roads;
	private HashMap<String, Intersection> intersections;
	private ArrayList<Vertex> vertexMap;
	
	public WorldConverter(String file){
		this.fileName = file;
		this.intersections = new HashMap<String, Intersection>();
		this.vertexMap = new ArrayList<Vertex>();
		initialise();
	}
	
	/**
	 * Getter for world map
	 * @return world map
	 */
	public ArrayList<Vertex> getMap(){
		return this.vertexMap;
	}
	
	/**
	 * Initialise the converted world
	 */
	private void initialise(){
		try {
			// Build the doc factory
			FileHandle file = Gdx.files.internal(fileName);			
			XmlReader reader = new XmlReader();
			Element root = reader.parse(file);

			// Setup data structures
			this.roads = new ArrayList<Road>();

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
			
			/* Add intersections that are incorrectly labeled in the xml file
			 * All intersections that have x coordinate 480 do not have
			 * roads specified connecting to intersections with coordinate
			 * x of 630, even though they should be.
			 */
			for (Vertex start : vertexMap){
				if (start.point.x == 480.0){
					for (Vertex end : vertexMap){
						if (end.point.x == 630 && end.point.y == start.point.y){
							start.connections.add(end);
							end.connections.add(start);
						}
					}
				}
			}
			
		} catch (Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Processes intersection
	 * @param intersectionElement Intersection element
	 */
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
	
	/**
	 * Adds intersection to graph structure
	 * @param startElement Starting road element
	 * @param endElement Ending road element
	 */
	private void addIntToGraph(Element startElement, Element endElement){
		// Create start intersection
		String startID = startElement.get("id");
		Intersection startInt = this.intersections.get(startID);
		Vertex startVer = null;
		// Check if vertex is initialised
		for (Vertex vertex : this.vertexMap){
			if (vertex.point.equals(startInt.pos)){
				startVer = vertex;
			}
		}
		// If not inistialised, create new vertex
		if (startVer == null){
			startVer = new Vertex(startInt.pos, startID);
			vertexMap.add(startVer);
		}
		// Create end intersection
		String endID = endElement.get("id");
		Intersection endInt = this.intersections.get(endID);
		Vertex endVer = null;
		// Check if vertex is initialised
		for (Vertex vertex : this.vertexMap){
			if (vertex.point.equals(endInt.pos)){
				endVer = vertex;
			}
		}
		// If not inistialised, create new vertex
		if (endVer == null){
			endVer = new Vertex(endInt.pos, endID);
			vertexMap.add(endVer);
		}

		
		for (Vertex vertex : vertexMap){
			// Add vertex to the start vertex in graph
			if (vertex.point.equals(startInt.pos)){
				if (!vertex.findVertex(endVer)){
					vertex.connections.add(endVer);
				}
			}
			// Add vertex to the end vertex in graph
			if (vertex.point.equals(endInt.pos)){
				if (!vertex.findVertex(startVer)){
					vertex.connections.add(startVer);
				}
			}
		}
	}
	
	/**
	 * Processes road from xml element
	 * @param roadElement xml element of road
	 * @return Returns a road
	 */
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
		if (startIntersection != null && endIntersection != null){
			addIntToGraph(startIntersection, endIntersection);
		}
		// Return road
		return r;
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
}
