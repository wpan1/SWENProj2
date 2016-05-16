package com.unimelb.swen30006.partc.iplanning;

import java.awt.geom.Point2D;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

import com.unimelb.swen30006.partc.core.MapReader;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.roads.Intersection;

public class WorldConverter {
	private ArrayList<Intersection> intersections;
	
	public WorldConverter(){
		MapReader reader = new MapReader("test_course.xml");
		intersections = new ArrayList<Intersection>(Arrays.asList(reader.processIntersections()));
	}
	
	public ArrayList<Intersection> getIntersections(){
		return intersections;
	}
	
	/**
	 * Returns the intersection closest to the point
	 * @param point Point in 2D space
	 * @return Closest intersection to point
	 */
	public Intersection getShortestDist(Point2D.Double point){
		Intersection closestInt = null;
		Double closestDist = Double.MAX_VALUE;
		// Iterate through all intersections
		for (Intersection intersection : intersections){
			// If another intersection is closer, change values
			if (intersection.pos.distance(point) < closestDist){
				closestInt = intersection;
				closestDist = intersection.pos.distance(point); 
			}
		}
		// Return closest intersection
		return closestInt;
	}
	
	/**
	 * Returns an array of intersections sorted by distance from point
	 * @param point Point in 2D space
	 * @return List of intersections
	 */
	public ArrayList<Intersection> getDistanceArray(Point2D.Double point){
		// List of intersections sorted by distance
		ArrayList<Intersection> distanceList = new ArrayList<Intersection>();
		// Add each intersection while maintaining sorted order
		for (Intersection intersection : intersections){
			int count = 0;
			while (count < distanceList.size()){
				// Break when index maintaining sorted order is found
				if (distanceList.get(count).pos.distance(point) >= intersection.pos.distance(point)){
					break;
				}
			}
			// Add at specified point
			distanceList.add(count, intersection);
		}
		// Return sorted list
		return distanceList;
	}
	
	/**
	 * Returns the intersections that are closest to intersection
	 * @param intersection Intersection to check for
	 * @return List of intersections that are closest to intersection
	 */
	public ArrayList<Intersection> getTraversableInt(Intersection intersection){
		// Get the minimum distance to the intersection
		Double minDist = getShortestDist(intersection.pos).pos.distance(intersection.pos);
		ArrayList<Intersection> closestIntList = new ArrayList<Intersection>();
		for (Intersection distInt : intersections){
			if (distInt.pos.distance(intersection.pos) == minDist){
				closestIntList.add(distInt);
			}
		}
		return closestIntList;
	}
}
