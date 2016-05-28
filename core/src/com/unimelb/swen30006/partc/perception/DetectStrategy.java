package com.unimelb.swen30006.partc.ai.perception;

import java.util.ArrayList;

/**
 * detect interface
 * 
 * @author Group 9
 *
 */
interface DetectStrategy {
	
	/**
	 * this function should take the point as input and applying an algorithm to
	 * parse all the points to some object blocks
	 * 
	 * @param map the map with all combined points
	 * @return a list of object block
	 */
	ArrayList<ObjectBlock> detectObjects(CombinedPoint map[][]);

}
