package com.unimelb.swen30006.partc.ai.perception;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * An immutable class of combined point 
 * 
 * @author Group 9
 *
 */
public final class CombinedPoint {
	
	protected Vector2 velocity;
	protected boolean space;
	protected Color colour;
	protected int x;
	protected int y;
	
	public boolean isSimiliar(CombinedPoint p2){
		if(this.colour==null && p2.colour == null)
			return true;
		return p2.space==this.space && p2.colour.equals(this.colour);
	}

}