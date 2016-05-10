package com.unimelb.swen30006.partc.core.objects;

import java.awt.geom.Point2D.Double;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.partc.render_engine.BuildingRender;

/**
 * This class provides functionality for use within the simulation system. It is NOT intended to be
 * read or understood for SWEN30006 Part C. Comments have been intentionally removed to reinforce
 * this. We take no responsibility if you use all your time trying to understand this code.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public final class Building extends WorldObject {
	
	public Building(Double pos, float width, float length, Color c){
		super(pos, width, length, c, new BuildingRender(width, length, c));
	}

}
