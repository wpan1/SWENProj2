package com.unimelb.swen30006.ai.planning;

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
			Point2D.Double nextpoint;
			if(points.size() == 1){
				nextpoint = points.get(0);
			}
			else{
				nextpoint = this.intersectionEnterPoint(points.get(0));
			}
			needSteer(nextpoint, delta);
			this.c.update(delta);
			// get next way point
			if(c.getPosition().distance(nextpoint) < 5){
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
	
	public ArrayList<Point2D.Double> pointsConvert(ArrayList<Point2D.Double> points){
		int size = points.size();
		for(int i = 0; i < size - 1; i++){
			points.get(i).x = points.get(i).x + 15;
			points.get(i).y = points.get(i).y + 15;
		}
		return points;
	}
	
	public Point2D.Double intersectionEnterPoint(Point2D.Double point){
		ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();
		Point2D.Double point1 = new Point2D.Double(point.x - 7.5,point.y - 15);
		points.add(point1);
		Point2D.Double point2 = new Point2D.Double(point.x + 7.5,point.y + 15);
		points.add(point2);
		Point2D.Double point3 = new Point2D.Double(point.x - 15,point.y + 7.5);
		points.add(point3);
		Point2D.Double point4 = new Point2D.Double(point.x + 15,point.y - 7.5);
		points.add(point4);
		
		Point2D.Double cloestPoint = point1;
		for(Point2D.Double p: points){
			if(this.c.getPosition().distance(cloestPoint) > this.c.getPosition().distance(p)){
				cloestPoint = p;
			}
		}
		return cloestPoint;
		
		
		
	}
}
