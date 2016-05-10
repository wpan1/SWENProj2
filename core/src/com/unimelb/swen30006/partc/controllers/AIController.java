package com.unimelb.swen30006.partc.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.ai.interfaces.*;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.Car;

/**
 * The AI controller for use in integrating your systems with the simulation.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public class AIController extends Controller {
	
	// The interfaces used to update the world
	private ISensing sensor;
	private IPlanning planner;
	private IPerception classifier;
	
	public AIController(Car car, ISensing sensor, IPlanning planner, IPerception classifier) {
		super(car);
		// Modify this as you like to instantiate your interface
	}

	@Override
	public void update(float delta) {
		// First update sensing
		sensor.update(this.car.getPosition(), delta, World.VISIBILITY_RADIUS);
		Vector2[][] velMap = sensor.getVelocityMap();
		boolean[][] spaceMap = sensor.getSpaceMap();
		Color[][] colMap = sensor.getColourMap();
		
		// Then updating classifier
		PerceptionResponse[] responses = classifier.analyseSurroundings(spaceMap, colMap, velMap);
		
		// Finally update planner
		planner.update(responses, delta);		
	}

}
