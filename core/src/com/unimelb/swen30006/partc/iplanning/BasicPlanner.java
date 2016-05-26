package com.unimelb.swen30006.partc.iplanning;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import com.unimelb.swen30006.partc.ai.interfaces.IPlanning;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.objects.Car;

public class BasicPlanner implements IPlanning{

	PathGenerator pg;
	PerceptionHandler pHandler;
	Car c;
	ArrayList<Point2D.Double> route;
	CarNavigator cn;
	
	public BasicPlanner(Car c){
		this.pg = new Dijkstra();
		this.c = c;
		this.cn = new CarNavigator(this.c);
	}
	
	@Override
	public boolean planRoute(Double destination) {
		// If path generated, return true
		if ((this.route = pg.findPath(c, destination)) != null){
			System.out.println(route);
			this.route = cn.pointsConvert(this.route);
			System.out.println(route);
			return true;
		}
		// Otherwise return false
		return false;
	}

	@Override
	public void update(PerceptionResponse[] results, float delta) {
		// car navigation follow the route
		cn.navigate(route, delta);
	}

	@Override
	public float eta() {
		return pg.calculateETA(c);
	}

}
