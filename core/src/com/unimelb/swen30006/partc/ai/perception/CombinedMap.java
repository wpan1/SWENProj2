package com.unimelb.swen30006.partc.ai.perception;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/** 
 * A class contains information about points around the car 
 * 
 * @author Group 9
 */
public class CombinedMap {
	
	private CombinedPoint[][] map;
	
	/** Combine three maps from the sensing to a new combined map
	 * @param velocityMap map of velocity
	 * @param spaceMap map of space
	 * @param colourMap map of colour
	 */
	public void combineMaps(Vector2[][] velocityMap ,boolean[][] spaceMap, Color[][] colourMap){
		
		if(velocityMap.length != spaceMap.length || velocityMap.length != colourMap.length || spaceMap.length != colourMap.length){
			System.out.print("Input Map ERROR!");
		}
		
		map = new CombinedPoint[velocityMap.length][velocityMap.length];
		
		for (int i = 0; i < velocityMap.length; i++){
			for (int j = 0; j < velocityMap.length; j++){
				CombinedPoint point = new CombinedPoint();
				point.velocity = velocityMap[i][j];
				point.space = spaceMap[i][j];
				point.colour = colourMap[i][j];
				point.x=i;
				point.y=j;
				map[i][j] = point;
			}
		}
		
	}
	
	/** Get the combined map */
	public CombinedPoint[][] getMap(){
		return map;
	}

}