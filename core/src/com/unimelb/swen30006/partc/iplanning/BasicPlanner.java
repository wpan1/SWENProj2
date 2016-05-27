package com.unimelb.swen30006.partc.iplanning;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import com.unimelb.swen30006.partc.ai.interfaces.IPlanning;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.roads.Road;

public class BasicPlanner implements IPlanning{

	PathGenerator pg;
	PerceptionHandler pHandler;
	Car c;
	ArrayList<Point2D.Double> route;
	CarNavigator cn;
	WorldConverter convertedWorld;
	
	public BasicPlanner(Car c){
		// Convert world into points
		this.convertedWorld = new WorldConverter("test_course.xml");
		this.pg = new Dijkstra(convertedWorld);
		this.c = c;
		this.cn = new CarNavigator(this.c);
	}
	
	@Override
	public boolean planRoute(Double destination) {
		Road destRoad;
		// Destination is not within 50 units
		if ((destRoad = convertedWorld.closestRoad(destination)) == null){
			return false;
		}
		destination.x = (destRoad.getEndPos().getX() + destRoad.getStartPos().getX())/2;
		destination.y = (destRoad.getEndPos().getY() + destRoad.getStartPos().getY())/2;
		// If path generated, return true
		if ((route = pg.findPath(c, destination)) != null){
			cn.setRoute(route);
			return true;
		}
		// Otherwise return false
		return false;
	}

	@Override
	public void update(PerceptionResponse[] results, float delta) {
		this.c.accelerate();
		this.c.update(delta);
		//System.out.println(this.cn.getRoute());
	}

	@Override
	public float eta() {
		return pg.calculateETA(c);
	}

}
