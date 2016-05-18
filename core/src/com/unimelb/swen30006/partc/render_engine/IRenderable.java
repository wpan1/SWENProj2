package com.unimelb.swen30006.partc.render_engine;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/**
 * This class provides functionality for use within the simulation system. It is NOT intended to be
 * read or understood for SWEN30006 Part C. Comments have been intentionally removed to reinforce
 * this. We take no responsibility if you use all your time trying to understand this code.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public interface IRenderable {
	public void render(Point2D.Double pos, ShapeRenderer r);
}
