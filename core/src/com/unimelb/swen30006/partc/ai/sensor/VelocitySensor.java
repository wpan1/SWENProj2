package com.unimelb.swen30006.partc.ai.sensor;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.core.objects.WorldObject;

public class VelocitySensor {
	public Vector2[][] generateVelocityMap(Point2D.Double currentPos,WorldObject[] visibleObjects,int visibility,Vector2 currentVelocity)
	{
		int i,j;
		Vector2[][] map=new Vector2[visibility][visibility];
		
		//set the map to empty 
		for(i=0;i<visibility;i++)
			for(j=0;j<visibility;j++)
			{
			map[i][j]=new Vector2(0,0);
			}
		//for every space the object has occupied,set the map to true
		for(i=0;i<visibleObjects.length;i++)
		{
			if(visibleObjects[i]==null)
			{
				break;
			}
			ArrayList<Point2D.Double> coordinates=new ArrayList<Point2D.Double>();
			coordinates=ObjectLocator.locateObjectOnMap(currentPos, visibility, visibleObjects[i]);
			Vector2 velocity=calcVelocity(visibleObjects[i],currentPos,currentVelocity);
			for(Point2D.Double tmp:coordinates)
			{
				int x=(int)tmp.x;
				int y=(int)tmp.y;
				map[x][y]=velocity;
			}
		}
		
		return map;
	}
	private Vector2 calcVelocity(WorldObject o,Point2D.Double carPos,Vector2 currentVelocity){
		Vector2 objectVelocity=new Vector2(0,0);
		Vector2 relativeVelocity=new Vector2(0,0);
		
		if(o instanceof Car )
		{
			objectVelocity=((Car) o).getVelocity();
		}
		else
		{
			objectVelocity.x=0;
			objectVelocity.y=0;
		}
		relativeVelocity.x=objectVelocity.x-currentVelocity.x;
		relativeVelocity.y=objectVelocity.y-currentVelocity.y;
		return relativeVelocity;
	}
}
