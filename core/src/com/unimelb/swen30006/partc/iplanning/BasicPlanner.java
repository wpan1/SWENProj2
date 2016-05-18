package com.unimelb.swen30006.partc.iplanning;

import java.awt.geom.Point2D.Double;

import com.unimelb.swen30006.partc.ai.interfaces.IPlanning;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.objects.Car;

public class BasicPlanner implements IPlanning{

	PathGenerator pg;
	PerceptionHandler pHandler;
	Car c;
	
	public BasicPlanner(Car c){
		this.pg = new Dijkstra();
		this.c = c;
	}
	
	@Override
	public boolean planRoute(Double destination) {
		return pg.findPath(c, destination);
	}

	@Override
	public void update(PerceptionResponse[] results, float delta) {
	}

	@Override
	public float eta() {
		return pg.calculateETA(c);
	}

}
