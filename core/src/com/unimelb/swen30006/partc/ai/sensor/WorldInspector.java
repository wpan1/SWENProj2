package com.unimelb.swen30006.partc.ai.sensor;

import java.awt.geom.Point2D;

import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;

public class WorldInspector {
	private World world;
	private int visibility;

	public WorldInspector(World world) {
		this.world = world;
	}

	public WorldObject[] getVisibleObject(Point2D.Double pos) {
		
		int i=0;
		WorldObject[] allObjects;
		allObjects = world.objectsAtPoint(pos);
		WorldObject[] visibleObjects=new WorldObject[allObjects.length];
		for(WorldObject tmp:allObjects){
			if(this.inRange(pos, tmp.getPosition())){
				visibleObjects[i]=tmp;
				i++;
			}
		}
		
		return visibleObjects;
	}

	public Road[] getVisibleRoads(Point2D.Double pos) {
		return world.roadsAroundPoint(pos);
	}

	public Intersection getIntersection(Point2D.Double pos, int visibility) {
		return world.intersectionAtPoint(pos);
	}

	public int getTimePeriod() {
		return 1;
	}

	public void updateVisibility(int visibility) {
		this.visibility = visibility;
	}
	private boolean inRange(Point2D.Double carPos,Point2D.Double oPos){
		int x_max=(int)carPos.x+visibility/2;
		int y_max=(int)carPos.y+visibility/2;
		int x_min=(int)carPos.x-visibility/2;
		int y_min=(int)carPos.y-visibility/2;
		
		if(oPos.x>=x_min&&oPos.x<=x_max&&oPos.y>=y_min&&oPos.y<=y_max){
			return true;
		}
		return false;
	}

	

}
