 package com.unimelb.swen30006.partc.ai.sensor;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.partc.core.objects.WorldObject;

public class SpaceSensor {
	
	public boolean[][] generateSpaceMap(WorldObject visibleObjects[],int visibility,Point2D.Double currentPos)
	{
		int i,j;
		boolean[][] map=new boolean[visibility][visibility];
		
		//set the map to empty 
		for(i=0;i<visibility;i++)
			for(j=0;j<visibility;j++)
			{
				map[i][j]=false;
			}
		//for every space the object has occupied,set the map to true
		for(i=0;i<visibleObjects.length;i++)
		{
			if(visibleObjects[i]==null)
			{
				continue;
			}
			ArrayList<Point2D.Double> coordinates=new ArrayList<Point2D.Double>();
			coordinates=ObjectLocator.locateObjectOnMap(currentPos, visibility, visibleObjects[i]);
			for(Point2D.Double tmp:coordinates)
			{
				int x=(int)tmp.x;
				int y=(int)tmp.y;
				map[x][y]=true;
			}
		}
		
		return map;
		
	}

}
