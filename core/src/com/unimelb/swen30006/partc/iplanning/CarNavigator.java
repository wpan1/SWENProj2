package com.unimelb.swen30006.partc.iplanning;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.objects.Car;

public class CarNavigator {
	private final static float ROTATION_RATE = 150f;
	Car c;
	
	public CarNavigator(Car c){
		this.c = c;
	}

	/**
	 * Calculate the steer direction and rate for the car
	 * @param nextPoint for next point the car head for
	 * @return boolean value for if the car steering
	 */
	public boolean needSteer(Point2D.Double nextPoint, float delta){
		// find the vector between car position and the next aimed position
		Vector2 curPosition = new Vector2((float)(nextPoint.x - this.c.getPosition().x),
				(float)(nextPoint.y - this.c.getPosition().y));
		// determine if the car need to turn
		if(curPosition.angleRad() == this.c.getVelocity().angleRad()){
			return false;
		}
		else{
			float angle = 0;
			// find a steering direction
			if(angleconvert(curPosition.angle(), this.c.getVelocity().angle())){
				angle += ROTATION_RATE*delta;
			}
			else{
				angle -= ROTATION_RATE*delta;
			}
			this.c.turn(angle);
			return true;
		}
	}

	/**
	 * AI car driving steps
	 * @param points for all the way points the car need to pass
	 * @param delta rate
	 */
	public void navigate(ArrayList<Point2D.Double> points, float delta){
		// check if car reaches the destination 
		if(points != null && !points.isEmpty()){
			this.c.accelerate();
			// steer the car
			needSteer(points.get(0), delta);
			this.c.update(delta);
			// get next way point
			if(c.getPosition().distance(points.get(0)) < 1){
				points.remove(0);
			}
		}
	}
	
	/**
	 * Determine the turning direction of the car
	 * @param curAngle for the vector angle
	 * @param carAngle for the car angle
	 * @return boolean value for the right turn or left turn
	 */
	public boolean angleconvert(double curAngle, double carAngle){
		double angle1 = curAngle - 360;
		double angle2 = curAngle;
		double angle3 = curAngle + 360;
		double closest = angle1;
		// find the closest turning angle
		if(Math.abs(closest - carAngle) > Math.abs(angle2 - carAngle)){
			closest = angle2;
		}
		if(Math.abs(closest - carAngle) > Math.abs(angle3 - carAngle)){
			closest = angle3;
		}
		// determine the turning direction
		if(closest > carAngle){
			return true;
		}
		else{
			return false;
		}
	}
}
