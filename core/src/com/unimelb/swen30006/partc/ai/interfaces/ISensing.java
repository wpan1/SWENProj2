package com.unimelb.swen30006.partc.ai.interfaces;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * The interface for the planning sub system
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */

public interface ISensing {

	/**
	 * Update is responsible for updating the current state of the world, it should be
	 * called with a position and scan the world at that point in time. This then
	 * allows the users to retrieve the various maps from the other interface methods 
	 * @param pos the position of the object that runs the sensing process
	 * @param delta the time difference since the previous update
	 * @param visibility the maximum visibility within the world, at the current point in time
	 * @return true if the scan finished successfully
	 */
	public boolean update(Point2D.Double pos, float delta, int visibility);
	
	/**
     * Returns a two dimensional array containing the current relative velocity of any objects
     * within the space. If there are no WorldObjects in the space, the velocity should be 0.
     * If there is a world object in the space, the velocity should be the relative velocity
     * of that object compared to the object we are tracking. As an example, an object that
     * has the same velocity and is moving along side a car would have a relative velocity of 0, 
     * and the velocity of an object moving toward us will have a relative velocity of our 
     * velocity plus their velocity towards us.
     * @return the calculated velocity map
	 */
	public Vector2[][] getVelocityMap();
	
	/**
	 * Returns a two dimensional array representing whether the space is
	 *  or isn't occupied by another collidable World Object.
	 * @return the calculated space map
	 */
	public boolean[][] getSpaceMap();

	/**
	 * Returns a two dimensional array of the additive colour of everything in a given position 
	 * around the current user.
	 * @return the calculated colour map
	 */
	public Color[][] getColourMap();
}
