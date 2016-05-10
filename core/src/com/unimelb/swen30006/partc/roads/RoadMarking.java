package com.unimelb.swen30006.partc.roads;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * This class provides functionality for use within the simulation system. It is NOT intended to be
 * read or understood for SWEN30006 Part C. Comments have been intentionally removed to reinforce
 * this. We take no responsibility if you use all your time trying to understand this code.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public final class RoadMarking {

	// Private constants
	private static final Color MARKING_COLOUR = Color.LIGHT_GRAY;
	private static final float MARKING_WIDTH = 1f;

	// Added Marking Types
	public enum Type {
		Signage, LaneMarking
	}
	
	// Immutable public values
	public final String text;
	public final Point2D.Double position;
	public final Type type;
	private final boolean horizontal;
	
	public RoadMarking(String text, Point2D.Double pos, boolean horizontal, Type type) {
		this.text = text;
		this.position = pos;
		this.type = type;
		this.horizontal = horizontal;
	}

	public void render(ShapeRenderer r){
		Color old = r.getColor();
		r.setColor(MARKING_COLOUR);
		float horizontalWidth = horizontal ? 2*MARKING_WIDTH : MARKING_WIDTH;
		float verticalWidth = horizontal ? MARKING_WIDTH : 2*MARKING_WIDTH;
		r.rect((float)(this.position.x - MARKING_WIDTH), (float)(this.position.y - MARKING_WIDTH), horizontalWidth, verticalWidth);
		r.setColor(old);
	}
}
