package com.unimelb.swen30006.ai.planning;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import com.unimelb.swen30006.partc.core.objects.Car;

public class Dijkstra implements PathGenerator{
	
	ArrayList<Point2D.Double> allPoints;
	WorldConverter convertedWorld;
	
	public Dijkstra(WorldConverter convertedWorld){
		this.convertedWorld = convertedWorld;
	}
	
	public ArrayList<Point2D.Double> getAllPoints() {
		return allPoints;
	}

	/**
	 * Calculates Estimated time in distance values
	 * @param c Current variables for car
	 * @return float of estimated time
	 */
	@Override
	public float calculateETA(Car c) {
		// Return zero if route not calculated
		if (allPoints == null){
			return 0f;
		}
		// Add distance from car to first intersection
		float totalDist = (float) (allPoints.get(0).distance(c.getPosition()));
		int vertexIndex = 0;
		// Add distance between intersections
		while (vertexIndex + 1 < convertedWorld.getMap().size() - 1){
			Point2D.Double v1 = convertedWorld.getMap().get(vertexIndex).point;
			Point2D.Double v2 = convertedWorld.getMap().get(vertexIndex + 1).point;
			totalDist += v1.distance(v2);
		}
		// Return total distances
		return totalDist;
	}
	
	/**
	 * Finds path using Dijkstra's algorithm
	 * @param c Car for starting position check
	 * @param destination End point
	 * @return Boolean value whether path is possible
	 */
	@Override
	public ArrayList<Point2D.Double> findPath(Car c, Point2D.Double destination) {	
		// Debug print info
		for (Vertex v : convertedWorld.getMap()){
			System.out.print(v.point + "," + v.name + ": ");
			for (Vertex v2 : v.connections){
				System.out.print(v2.point + ";" + v2.name + ", ");
			}
			System.out.println();
		}
		// Get closest intersection to car
		Vertex carVertex = null;
		Double carDis = Double.MAX_VALUE;
		// Get closest intersection to destination
		Vertex endVertex = null;
		Double endDis = Double.MAX_VALUE;
		// Find min distance between car/end and vertices in map
		for (Vertex v : convertedWorld.getMap()){
			// Find distance from car to specified vertex
			Double dis = c.getPosition().distance(v.point);
			// Check distance for car
			if (dis < carDis){
				carVertex = v;
				carDis = dis;
			}
			// Find distance from destination to specified vertex
			Double dis2 = destination.distance(v.point);
			// Check distance for end
			if (dis2 < endDis){
				endVertex = v;
				endDis = dis2;
			}
		}
		
		// Build Initial HashMap
		HashMap<Vertex, Double> dist = new HashMap<Vertex, Double>();
		HashMap<Vertex, Vertex> prev = new HashMap<Vertex, Vertex>();
		for (Vertex v : convertedWorld.getMap()){
			dist.put(v, Double.MAX_VALUE);
			prev.put(v, null);
		}
		// Initialise start to 0
		dist.replace(carVertex, 0.0);
		// Initialise node set
		ArrayList<Vertex> nodeSet = new ArrayList<Vertex>(convertedWorld.getMap());
		while (nodeSet.size() > 0){
			Vertex minVertex = removeMin(dist, nodeSet);
			nodeSet.remove(minVertex);
			// Find neighbours of minimum vertex
			for (Vertex neighbour : minVertex.connections){
				Double tempDist = dist.get(minVertex) + minVertex.point.distance(neighbour.point);
				if (tempDist < dist.get(neighbour)){
					dist.replace(neighbour, tempDist);
					prev.replace(neighbour, minVertex);
				}
			}
		}
		
		// Find path to traverse 
		ArrayList<Point2D.Double> path = new ArrayList<Point2D.Double>();
		path.add(carVertex.point);
		while (endVertex != carVertex){
			path.add(1, endVertex.point);
			endVertex = prev.get(endVertex);
		}
		path.add(destination);
		
		for (Point2D.Double pathvtx : path){
			System.out.println(pathvtx);
		}
		
		return path;
	}
	
	/**
	 * Find the node with the minimum distance
	 * @param dist Distance hashmap
	 * @param nodeSet Set of nodes still available
	 * @return
	 */
	private Vertex removeMin(HashMap<Vertex, Double> dist, ArrayList<Vertex> nodeSet){
		// Initialise maximum values
		Double minDist = Double.MAX_VALUE;
		Vertex minVertex = null;
		// Iterate through vertices
		for (Vertex vertex : nodeSet){
			// Find minimum value
			if (dist.get(vertex) <= minDist){
				minDist = dist.get(vertex);
				minVertex = vertex;
			}
		}
		// Return minimum value
		return minVertex;
	}
	
}
