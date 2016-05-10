package com.unimelb.swen30006.partc.controllers;

import com.unimelb.swen30006.partc.core.ISteppable;
import com.unimelb.swen30006.partc.core.objects.Car;

/**
 * The abstract controller that implements the stepabble interface and allows the assignment of a car
 * to be controller by this controller.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public abstract class Controller implements ISteppable {

	// The car that this controller is driving
	public final Car car;
	
	/**
	 * Instantiate a controller with a car to control
	 * @param car the car to control
	 */
	public Controller(Car car) {
		this.car = car;
	}

}
