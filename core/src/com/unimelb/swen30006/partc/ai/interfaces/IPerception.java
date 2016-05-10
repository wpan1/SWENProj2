package com.unimelb.swen30006.partc.ai.interfaces;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * The interface for the perception sub system
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public interface IPerception {
	
	/**
	 * Analyses the perception response and determines the appropriate action to take
	 * @param spaceMap
	 * @param colorMap
	 * @param velMap
	 * @return
	 */
	public PerceptionResponse[] analyseSurroundings(boolean[][] spaceMap, Color[][] colorMap, Vector2[][] velMap);
	
}
