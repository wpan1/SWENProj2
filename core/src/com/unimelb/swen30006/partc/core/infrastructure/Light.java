package com.unimelb.swen30006.partc.core.infrastructure;

import java.awt.geom.Point2D.Double;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.core.objects.Infrastructure;
import com.unimelb.swen30006.partc.render_engine.LightRenderer;

/**
 * This class provides functionality for use within the simulation system. It is NOT intended to be
 * read or understood for SWEN30006 Part C. Comments have been intentionally removed to reinforce
 * this. We take no responsibility if you use all your time trying to understand this code.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public class Light extends Infrastructure {

	private static final float WIDTH = 1f;
	private static final float LENGHT = 1f;
	
	
	public Light(Double pos, Color col, float radius, float brightness) {
		super(pos, WIDTH, LENGHT, col, new LightRenderer(col, radius), Infrastructure.InfrastructureType.Light);
		LightRenderer renderer = (LightRenderer) getRenderObject();
		renderer.setBrightness(brightness);
	}

	@Override
	public boolean canCollide(Car car) {
		return false;
	}
	
	public void turnOn(){
		LightRenderer renderer = (LightRenderer) getRenderObject();
		renderer.turnOn();
	}
	
	public void turnOff(){
		LightRenderer renderer = (LightRenderer) getRenderObject();
		renderer.turnOff();
	}

}
