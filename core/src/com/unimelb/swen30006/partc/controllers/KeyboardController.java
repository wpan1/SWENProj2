package com.unimelb.swen30006.partc.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.unimelb.swen30006.partc.core.objects.Car;

/**
 * This class provides functionality for use within the simulation system. It is NOT intended to be
 * read or understood for SWEN30006 Part C. Comments have been intentionally removed to reinforce
 * this. We take no responsibility if you use all your time trying to understand this code.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */

public class KeyboardController extends Controller {

	private final static float ROTATION_RATE = 150f;
	
	public KeyboardController(Car c) {
		super(c);
	}

	@Override
	public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            this.car.brake();
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
        	this.car.accelerate();
        }

        // Handle turning
        float angle = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
        	angle += ROTATION_RATE*delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        	angle -= ROTATION_RATE*delta;
        }
        if(angle != 0){
            this.car.turn(angle);
        }
        
        // Update the car
        this.car.update(delta);
	}

}
