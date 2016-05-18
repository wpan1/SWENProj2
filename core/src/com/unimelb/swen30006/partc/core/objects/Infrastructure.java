package com.unimelb.swen30006.partc.core.objects;

import java.awt.geom.Point2D.Double;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.partc.render_engine.IRenderable;

/**
 * This class provides functionality for use within the simulation system. It is NOT intended to be
 * read or understood for SWEN30006 Part C. Comments have been intentionally removed to reinforce
 * this. We take no responsibility if you use all your time trying to understand this code.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public abstract class Infrastructure extends WorldObject {

	public enum InfrastructureType {
		Light, TrafficLight, Sign;
	}
	
	public final InfrastructureType type;
	
	public Infrastructure(Double pos, float width, float length, Color col, IRenderable renderer, InfrastructureType type) {
		super(pos, width, length, col, renderer);
		this.type = type;
	}
	
	public abstract boolean canCollide(Car car);

}
