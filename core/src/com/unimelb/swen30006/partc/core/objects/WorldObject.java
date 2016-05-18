package com.unimelb.swen30006.partc.core.objects;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.partc.render_engine.IRenderable;

/**
 * This class provides functionality for use within the simulation system. It is NOT intended to be
 * read or understood for SWEN30006 Part C. Comments have been intentionally removed to reinforce
 * this. We take no responsibility if you use all your time trying to understand this code.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public class WorldObject {

	// Private instance variables common to all objects
	private Point2D.Double position;
	private float width;
	private float length;
	private Color colour;
	private IRenderable renderObject;
	public Rectangle2D.Double boundary;
	
	public WorldObject(Point2D.Double pos, float width, float length, Color col, IRenderable renderer) {
		this.position = pos;
		this.width = width;
		this.length = length;
		this.colour = col;
		this.boundary = new Rectangle2D.Double((float)pos.x - width/2, (float)pos.y - length/2, width, length);
		this.renderObject = renderer;
	}
	
	public boolean collidesWith(WorldObject o){
		return (this.boundary.intersects(o.boundary) || o.boundary.intersects(this.boundary));
	}
	
	public void render(ShapeRenderer r){
		this.renderObject.render(this.position, r);
	}
	
	public Point2D.Double getPosition() {
		return position;
	}

	public float getWidth() {
		return width;
	}

	public float getLength() {
		return length;
	}

	public Color getColour() {
		return colour;
	}

	protected void setPosition(Point2D.Double position) {
		this.position = position;
	}
	
	protected IRenderable getRenderObject(){
		return this.renderObject;
	}

	protected void setWidth(float width) {
		this.width = width;
	}

	protected void setLength(float length) {
		this.length = length;
	}

	protected void setColour(Color colour) {
		this.colour = colour;
	}
	
	
}
