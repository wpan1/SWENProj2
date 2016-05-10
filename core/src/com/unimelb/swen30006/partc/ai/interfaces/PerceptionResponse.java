package com.unimelb.swen30006.partc.ai.interfaces;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

/**
 * The model for the perception responses passing between perception and planning.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public final class PerceptionResponse {

	// Enum for data
	public enum Classification {
		Building, TrafficLight, Car, RoadMarking, LaneMarking, Sign, StreetLight
	}
	
	// Immutable data
	public final float distance;
	public final Vector2 velocity;
	public final Vector2 direction;
	public final float timeToCollision;
	public final Classification objectType;
	public final HashMap<String, Object> information;
	
	/**
	 * Creates a perception response object with the provided data, this will give an immutable class.
	 * @param distance the calculated distance to the object
	 * @param v the velocity of the object
	 * @param dir the direction from the current position to the object
	 * @param ttc the time to collision, -1 if no collision will occur
	 * @param type the type of classification
	 * @param info any additional information that may be calculated by the perception subsystem.
	 */
	public PerceptionResponse(float distance, Vector2 v, Vector2 dir, float ttc, Classification type, HashMap<String, Object> info){
		this.distance = distance;
		this.velocity = v;
		this.timeToCollision = ttc;
		this.objectType = type;
		this.information = info;
		this.direction = dir;
	}
}
