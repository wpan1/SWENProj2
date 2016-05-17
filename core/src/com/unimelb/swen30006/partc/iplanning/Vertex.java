package com.unimelb.swen30006.partc.iplanning;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Vertex{
	// Point in 2D space
	Point2D.Double point;
	// Vertexes that are connected to current vertex
	ArrayList<Vertex> connections;
	
	public Vertex(Point2D.Double point){
		this.point = point;
		connections = new ArrayList<Vertex>();
	}
	
	/**
	 * Check if vertex is connected to current vertex
	 * @param vertex Vertex to check
	 * @return boolean value whether vertices are connected
	 */
	public boolean findVertex(Vertex vertex){
		if (connections.contains(vertex))
			return true;
		return false;
	}

	/**
	 * Hash code for arraylist comparison
	 * Missing connections as this is irrelevant for comparison
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		return result;
	}

	/**
	 * Equals for object comparisons
	 * Missing connections as this is irrelevant for comparison
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (point == null) {
			if (other.point != null)
				return false;
		} else if (!point.equals(other.point))
			return false;
		return true;
	}
} 