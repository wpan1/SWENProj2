package com.unimelb.swen30006.partc.ai.sensor;

import java.awt.geom.Point2D;

import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.objects.Car;

public class CarInspector {
	
	private Car car;
	public CarInspector(Car c)
	{
		this.car=c;
	}
	public Vector2 getVelocity(float delta)
	{
		
		return car.getVelocity();
	}

}
