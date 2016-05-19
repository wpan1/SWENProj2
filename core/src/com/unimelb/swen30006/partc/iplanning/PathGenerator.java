package com.unimelb.swen30006.partc.iplanning;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.unimelb.swen30006.partc.core.objects.Car;

public interface PathGenerator {
	
	/**
	 * Calculates the estimated time of arrival
	 * To be called after findPath
	 * @param c Current car position
	 * @return float of estimated time
	 */
	public float calculateETA(Car c);
	
	/**
	 * Finds the path from car to the destination
	 * @param c Car for starting position check
	 * @param destination End point
	 * @return Boolean value whether path is possible
	 */
	public ArrayList<Point2D.Double> findPath(Car c, Point2D.Double destination);
}
