package com.unimelb.swen30006.partc.ai.interfaces;

import java.awt.geom.Point2D;

/**
 * The interface for the planning sub system
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public interface IPlanning {

	/**
	 * Plans a route to the given destination, or the closest possible point to
	 * that destination, in the world the car is operating. Returns true if a route can be found
	 * and false if there is no route that gets the user within 50 units of the position
	 * @param destination The point we want to reach
	 * @return true if a route can be found
	 */
	public boolean planRoute(Point2D.Double destination);
	
	/**
	 * Takes the perception results and processes these perception results to identify any
	 * neccesary changes to the current course of action. This must consider all road rules when
	 * determining actions, and can only provide one set of inputs to the car at a time
	 * @param results the responses from the perception subsystem
	 * @param delta the difference in time between update steps
	 */
	public void update(PerceptionResponse[] results, float delta);
	
	/**
	 * Returns the estimate of the time remaining until the car reaches its destination,
	 * given in seconds. If there is no destination, it returns 0.
	 * @return
	 */
	public float eta();
}
