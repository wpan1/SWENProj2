package com.unimelb.swen30006.partc.render_engine;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * This class provides functionality for use within the simulation system. It is NOT intended to be
 * read or understood for SWEN30006 Part C. Comments have been intentionally removed to reinforce
 * this. We take no responsibility if you use all your time trying to understand this code.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public class CarRenderer implements IRenderable {
	
	private final float width;
	private final float length;
	private final Color color;
	private final Color roofColour;
	private float rotation;
	private Rectangle2D.Float shape;
	
	public CarRenderer(Color c, float width, float length) {
		this.color = c;
		this.width = width;
		this.length = length;
		this.rotation = 0;
		this.roofColour = this.color.cpy().lerp(Color.WHITE, (float) 0.5f);
		this.shape = new Rectangle2D.Float(0, 0, length, width);
	}
	
	public void setRotation(float rotation){
		this.rotation = rotation;
	}

	@Override
	public void render(Point2D.Double pos, ShapeRenderer r) {		
		// Save the old colour and set the new colour
		Color old = r.getColor();
		r.setColor(this.color);
				
		// Render the body
		Point2D.Double corner;
		corner = new Point2D.Double((float)pos.x - this.length/2, (float)pos.y - this.width/2);
		shape.setFrameFromCenter(pos, corner);
		r.rect((float)shape.getMinX(), (float)shape.getMinY(), length/2, width/2, length, width, 1, 1, this.rotation);

		// Render the roof and windscreen
		r.setColor(Color.DARK_GRAY);
		r.rect((float)shape.getMinX(), (float)shape.getMinY(), length/2, width/2, (7*length)/8, width, 0.8f, 0.8f, this.rotation);
		r.setColor(this.roofColour);
		r.rect((float)shape.getMinX(), (float)shape.getMinY(), length/2, width/2, (13f*length)/20, width, 0.8f, 0.8f, this.rotation);

		// Set old colour back
		r.setColor(old);
	}

}
