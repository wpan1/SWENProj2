package com.unimelb.swen30006.partc.core.infrastructure;

import java.awt.geom.Point2D.Double;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.core.objects.Infrastructure;
import com.unimelb.swen30006.partc.render_engine.SignRenderer;

/**
 * This class provides functionality for use within the simulation system. It is NOT intended to be
 * read or understood for SWEN30006 Part C. Comments have been intentionally removed to reinforce
 * this. We take no responsibility if you use all your time trying to understand this code.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public final class Sign extends Infrastructure {

	public final String text;
	
	public Sign(Double pos, float width, float length, Color col,String text) {
		super(pos, width, length, col, new SignRenderer(col, width, length), Infrastructure.InfrastructureType.Sign);
		this.text = text;
	}

	@Override
	public boolean canCollide(Car car) {
		return false;
	}

}
