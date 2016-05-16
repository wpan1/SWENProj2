package com.unimelb.swen30006.partc.iplanning;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Intersection.Direction;
import com.unimelb.swen30006.partc.roads.Road;

public class AStar implements PathGenerator{
	
	World world;
	ArrayList<Point2D.Double> allPoints;
	
	public AStar(World world){
		this.world = world;
	}
	
	@Override
	public float calculateETA(Car c, PerceptionHandler pHandler) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float calculateSingleETA(Car c, Double speedLimit) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void findShortestPath(Car c, Point2D.Double destination) {
		// Find closest road to destination
		Road destRoad = world.closestRoad(destination);
		destRoad.laneDirection(0);
		if (destRoad == null) return;
		// Get current road car is on
		Road currRoad = world.roadAtPoint(c.getPosition());
		// Get direction in which car is going
		Direction carDir = getCarDirection(c);
		// Find closest point on road to destination
		Stack<Intersection> intList = new Stack<Intersection>();
		if (world.intersectionAtPoint(currRoad.getEndPos()) != null)
			intList.push(world.intersectionAtPoint(currRoad.getEndPos()));
		else if (world.intersectionAtPoint(currRoad.getStartPos()) != null)
			intList.push(world.intersectionAtPoint(currRoad.getStartPos()));
		while(currRoad != destRoad){
			// Generate all roads possible
			ArrayList<Road> roads = new ArrayList<Road>();
			for (Direction dir : intList.pop().roads.keySet()){
				if (!dir.equals(carDir))
					roads.add(intList.pop().roads.get(dir));
			}
		}
	}
	
	/** Returns the Direction of the car given its velocity
	 * @param c Car object
	 * @return returns emun of Direction
	 */
	private Direction getCarDirection(Car c){
		float carAngle = c.getVelocity().angle();
		if (carAngle <= 45 && carAngle > 315)
			return Direction.East;
		else if (carAngle <= 135 && carAngle > 45)
			return Direction.North;
		else if (carAngle <= 225 && carAngle > 135){
			return Direction.West;
		}
		return Direction.South;
	}
	
	private Direction getRoadDirection(Car c, Road r){
		// Horizontal road
		if (r.getStartPos().y == r.getEndPos().y){
			// Car is eastbound if ontop of the road
			if (c.getPosition().y > r.getEndPos().y){
				return Direction.East;
			}
			// Car is southbound if onbottom of the road
			return Direction.South;
		}
		// Vertical road
		if (r.getStartPos().x == r.getEndPos().x){
			// Car is southbound if onleft of the road
			if (c.getPosition().x > r.getEndPos().x){
				return Direction.South;
			}
			// Car is northbound is onright of the road
			return Direction.North;
		}
		return null;
	}
	
	/** Returns intersection at end of road, given car direction
	 * 
	 * @param distanceList
	 */
	private Point2D.Double getRoadEnd(Car c, Road r){
		Direction roadDir = getRoadDirection(c, r);
		// If car is northbound
		if (roadDir == Direction.North){
			// If start point is at the top
			if (r.getStartPos().y > r.getEndPos().y){
				return r.getStartPos();
			}
			// If start point is at the bottom
			return r.getEndPos();
		}
		// If car is southbound
		if (roadDir == Direction.South){
			// If start point is at the bottom
			if (r.getStartPos().y < r.getEndPos().y){
				return r.getStartPos();
			}
			// If start point is at the top
			return r.getEndPos();
		}
		// If car is eastbound
		if (roadDir == Direction.East){
			// If start point is at the right
			if (r.getStartPos().x > r.getEndPos().x){
				return r.getStartPos();
			}
			// If start point is at the left
			return r.getEndPos();
		}
		// If car is westbound
		if (roadDir == Direction.West){
			// If start point is at the left
			if (r.getStartPos().x < r.getEndPos().x){
				return r.getStartPos();
			}
			// If start point is at the right
			return r.getEndPos();
		}
		return null;
	}
	
	private void evalHeuristic(ArrayList<Intersection> distanceList){
		
	}
	
}
