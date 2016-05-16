package com.unimelb.swen30006.partc.iplanning;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.unimelb.swen30006.partc.core.objects.Car;

public interface PathGenerator {
	
	public float calculateETA(Car c, PerceptionHandler pHandler);
	
	public float calculateSingleETA(Car c, Double speedLimit);
	
	public void findShortestPath(Car c, Point2D.Double destination);
}
