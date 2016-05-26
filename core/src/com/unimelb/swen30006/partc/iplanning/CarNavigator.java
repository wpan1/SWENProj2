package com.unimelb.swen30006.partc.iplanning;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.objects.Car;

public class CarNavigator {
	private final static float ROTATION_RATE = 150f;
	Car c;
	ArrayList<Point2D.Double> route;
	
	public CarNavigator(Car c){
		this.c = c;
	}
	
	public void setRoute(ArrayList<Point2D.Double> route) {
		this.route = route;
	}

	public ArrayList<Point2D.Double> getRoute() {
		return route;
	}

	public boolean needSteer(Point2D.Double nextPoint, float delta){
		Vector2 curPosition = new Vector2((float)(nextPoint.x - this.c.getPosition().x),
				(float)(nextPoint.y - this.c.getPosition().y));
		if(curPosition.angle() == this.c.getVelocity().angle()){
			return false;
		}
		else{
			float angle = 0;
			if(curPosition.angle() > this.c.getVelocity().angle()){
				angle += ROTATION_RATE*delta;
			}
			if(curPosition.angle() < this.c.getVelocity().angle()){
				angle -= ROTATION_RATE*delta;
			}
			this.c.turn(angle);
			return true;
		}
	}
	
	public void navigate(ArrayList<Point2D.Double> points, float delta){
		c.accelerate();
		needSteer(points.get(0), delta);
		if(c.getPosition().distance(points.get(0)) < 1){
			points.remove(0);
		}
	}
}
