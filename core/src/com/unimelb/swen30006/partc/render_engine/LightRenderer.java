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
public class LightRenderer implements IRenderable{

	// Private variables to render
	private final float radius;
	private final float spill_radius;

	private final static Color OFF_COLOUR = Color.LIGHT_GRAY;
	private Color brightColour;
	private Color lightColour;
	private boolean on;

	public LightRenderer(Color c, float radius){
		this.brightColour = c;
		this.radius = radius;
		this.spill_radius = 5*radius;
		this.on = false;
		this.lightColour = new Color(brightColour.r, brightColour.g, brightColour.b, 0.1f);	
	}

	@Override
	public void render(Double pos, ShapeRenderer r) {
		Color old = r.getColor();
		if(this.on){
			r.setColor(brightColour);
			r.circle((float) pos.getX(), (float) pos.getY(), radius, 100);
			r.setColor(lightColour);
			r.circle((float) pos.getX(), (float) pos.getY(), spill_radius, 100);
		} else {
			r.setColor(OFF_COLOUR);
			r.circle((float) pos.getX(), (float) pos.getY(), radius/2, 100);
		}
		r.setColor(old);
	}

	public void turnOn(){
		this.on = true;
	}

	public void turnOff(){
		this.on = false;
	}

	public void setBrightness(float brightness) {
		this.lightColour = new Color(brightColour.r, brightColour.g, brightColour.b, brightness);	
	}

}
