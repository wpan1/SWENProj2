package com.unimelb.swen30006.partc.core.objects;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.ISteppable;
import com.unimelb.swen30006.partc.render_engine.CarRenderer;

/**
 * This class provides functionality for use within the simulation system. It is NOT intended to be
 * read or understood for SWEN30006 Part C. Comments have been intentionally removed to reinforce
 * this. We take no responsibility if you use all your time trying to understand this code.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public class Car extends WorldObject implements ISteppable {

	// Private car behaviour
	private static final float STEERING_ADJUST_FACTOR = 0.25f;
	private static final float FRICTION_FORCE = 3.5f;
	private static final float EPSILON = 0.05f;
	private static final float TURN_EPSILON = 0.0002f;
	private static final float MAX_VELOCITY = 50f;
	private static final float MAX_DEGREES = 360;
	
	// Private instance variables for configuration
	private float acceleration;
	private float brakingPower;

	// Private instance variables for state
	private Vector2 velocity;
	private float rotation;

	// Private instance variables simply used to track updating
	private boolean braking = false;
	private boolean reversing = false;
	private boolean accelerating = false;
	private float turnAngle;

	public Car(Double pos, float width, float length, Color col, float acceleration, float brakingPower, float turningRadius) {
		super(pos, width, length, col, new CarRenderer(col, width, length));
		this.velocity = new Vector2(0,0);
		this.rotation = 0;
		this.acceleration = acceleration;
		this.brakingPower = brakingPower;
		this.rotation = this.turnAngle = 0;
	}

	public void brake() {
		this.braking = true;
	}

	public void accelerate() {
		this.accelerating = true;
		this.reversing = false;
	}

	public void turn(float angle){
		this.turnAngle = angle;
	}

	public Vector2 getVelocity(){
		return this.velocity;
	}

	private void resetInput(){
		this.braking = false;
		this.accelerating = false;
		this.reversing = false;
	}

	@Override
	public void update(float delta) {

		// Calculate the net force on the car
		// First calculate the force created by the engine, that is either accelerating or reversing
		float drivingForce = 0;
		if(this.accelerating || this.reversing){
			drivingForce = this.acceleration;
		}

		// Calculate the braking force, if not braking apply a small amount of frictino so we slow down over
		// time, given this is negligible compared to braking we do one or, not both
		float brakingForce = 0;
		if(this.braking && (this.velocity.len() > EPSILON)){
			brakingForce = this.brakingPower;
		} else if (this.velocity.len() > 0){
			brakingForce = FRICTION_FORCE;
		}
		
		// Apply steering
		applySteering();
		
		// Calculate acceleration
		Vector2 netAcceleration = calculateAcceleration(drivingForce, brakingForce);
		
		// Apply the acceleration to velocity
		applyAcceleration(netAcceleration, delta);
		
		// Update the position
		updatePosition(delta);
		
		// Update the render 
		CarRenderer r = (CarRenderer) this.getRenderObject();
		r.setRotation(this.rotation);
		updateBoundary();
		
		// Reset our inputs
		resetInput();
	}
	
	private void updateBoundary(){
		this.boundary = new Rectangle2D.Double((float)this.getPosition().x - this.getWidth()/2, (float)this.getPosition().y - this.getLength()/2, this.getWidth(), this.getLength());
	}
	
	private void applySteering(){
		// We can turn if we are moving.
		if(this.velocity.len() > EPSILON) { 
			// Update our rotation
			this.rotation += this.turnAngle;
			// Slowly return our rotation to 0 if not turning
			if(this.turnAngle > TURN_EPSILON || this.turnAngle < -TURN_EPSILON ){
				// Slowly update turnings
				int sign = this.turnAngle > 0 ? 1 : -1;
				float magnitude = Math.abs(this.turnAngle) - Math.abs(this.turnAngle)*STEERING_ADJUST_FACTOR;
				this.turnAngle = magnitude * sign;
			} else {
				this.turnAngle = 0;
			}
		}

	}
	
	private Vector2 calculateAcceleration(float drivingForce, float brakingForce){
		// Create an acceleration vector by rotating a unit vector
		// and scaling with the appropriate force
		Vector2 acceleration = new Vector2(1,0);
		acceleration.rotate(this.rotation);
		if(this.reversing){
			acceleration.rotate(180);
		}
		acceleration.scl(drivingForce);
		
		// Create a braking vector
		Vector2 braking = new Vector2(1,0);
		if(acceleration.len() > 0){
			// Rotate to face the other direction
			braking.rotate(acceleration.angle() - MAX_DEGREES/2);
		} else {
			// Apply braking in the opposite direction that we are facing
			braking.rotate((this.rotation - MAX_DEGREES/2) % MAX_DEGREES);
		}
		braking.scl(brakingForce);

		// Calculate net change
		Vector2 netAcceleration = acceleration.add(braking);
		return netAcceleration;
	}
	
	private void applyAcceleration(Vector2 acceleration, float delta){
		// Rotate our velocity (highly simplified effect of rotating the car) and update with acceleration
		this.velocity.setAngle(this.rotation);
		this.velocity.x += acceleration.x * delta;
		this.velocity.y += acceleration.y * delta;

		// If we get greater than max velocity then limit us to that, if we're smaller than epsilon stop
		if(this.velocity.len() > MAX_VELOCITY) {
			float scalar = this.velocity.len() / MAX_VELOCITY;
			this.velocity.scl(1/scalar);
		} else if (this.velocity.isZero(EPSILON)){
			this.velocity.x = 0;
			this.velocity.y = 0;
		}
	}
	
	private void updatePosition(float delta){
		// Update position
		Point2D.Double position = this.getPosition();
		position.x = position.x + this.velocity.x * delta;
		position.y = position.y + this.velocity.y * delta;
		this.setPosition(position);

	}

}
