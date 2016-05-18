package com.unimelb.swen30006.partc.iplanning;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.objects.Car;

public class CarNavigator {
	Car c;
	
	public CarNavigator(Car c){
		this.c = c;
	}
	
	public boolean needSteer(Point2D.Double nextPoint){
		Vector2 curPosition = new Vector2((float)(nextPoint.x - this.c.getPosition().x),
				(float)(nextPoint.y - this.c.getPosition().y));
		if(curPosition.angle() == this.c.getVelocity().angle()){
			return false;
		}
		else{
			this.c.turn(curPosition.angle() - this.c.getVelocity().angle());
			return true;
		}
	}
	
	public void navigate(ArrayList<Point2D.Double> points){
		c.accelerate();
		needSteer(points.get(0));
		points.remove(0);
	}
}
