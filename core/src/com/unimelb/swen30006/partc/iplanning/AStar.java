package com.unimelb.swen30006.partc.iplanning;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Stack;

import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Intersection.Direction;
import com.unimelb.swen30006.partc.roads.Road;

public class AStar implements PathGenerator{
	
	World world;
	ArrayList<Point2D.Double> allPoints;
	WorldConverter convertedWorld;
	
	public AStar(){
		this.convertedWorld = new WorldConverter("test_course.xml");
	}
	
	@Override
	public float calculateETA(Car c) {
		float totalDist = 0.0f;
		int vertexIndex = 0;
		while (vertexIndex + 1 < convertedWorld.getMap().size() - 1){
			Point2D.Double v1 = convertedWorld.getMap().get(vertexIndex).point;
			Point2D.Double v2 = convertedWorld.getMap().get(vertexIndex + 1).point;
			totalDist += v1.distance(v2);
		}
		return totalDist;
	}
	
	@Override
	public boolean findShortestPath(Car c, Point2D.Double destination) {
		// Get closest intersection to car
		Vertex carVertex = null;
		Double carDis = Double.MIN_VALUE;
		// Get closest intersection to destination
		Vertex endVertex = null;
		Double endDis = Double.MIN_VALUE;
		// Find min distance between car/end and vertices in map
		for (Vertex v : convertedWorld.getMap()){
			Double disVertex = c.getPosition().distance(v.point);
			// Check distance for car
			if (disVertex < carDis){
				carVertex = v;
				carDis = disVertex;
			}
			// Check distance for end
			if (disVertex < endDis){
				endVertex = v;
				endDis = disVertex;
			}
		}
		// Get route to endVertes
		ArrayList<Point2D.Double> allPoints = new ArrayList<Point2D.Double>();
		while (carVertex != endVertex){
			allPoints.add(carVertex.point);
			// Find closest vertex to end vertex
			Vertex interVertex = null;
			Double interDis = Double.MIN_VALUE;
			for (Vertex v : carVertex.connections){
				Double disVertex = destination.distance(v.point);
				// Check distance for intermediate vertex
				if (interDis < carDis){
					interVertex = v;
					interDis = disVertex;
				}
			}
			carVertex = interVertex;
		}
		// Add last intersection
		allPoints.add(carVertex.point);
		// Add point from intersection to destination
		allPoints.add(destination);
		return true;
	}
	
}
