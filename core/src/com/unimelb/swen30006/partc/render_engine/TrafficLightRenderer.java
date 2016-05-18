package com.unimelb.swen30006.partc.render_engine;

import java.awt.geom.Point2D.Double;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * This class provides functionality for use within the simulation system. It is NOT intended to be
 * read or understood for SWEN30006 Part C. Comments have been intentionally removed to reinforce
 * this. We take no responsibility if you use all your time trying to understand this code.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public class TrafficLightRenderer implements IRenderable{

	private static final float RADIUS = 2f;
	private Color col;
	
	public void updateColor(Color c){
		this.col = c;
	}

	@Override
	public void render(Double pos, ShapeRenderer r) {
		Color old = r.getColor();
		r.setColor(col);
		r.circle((float) pos.getX(), (float) pos.getY(), RADIUS);
		r.setColor(old);
	}

}
