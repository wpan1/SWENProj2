package com.unimelb.swen30006.partc.ai.sensor;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;
import com.unimelb.swen30006.partc.roads.RoadMarking;

public class ObjectLocator {
	
	public static ArrayList<Point2D.Double> locateObjectOnMap(Point2D.Double carPos,int visibility,WorldObject object)
	{
		ArrayList<Point2D.Double> coordinatesOnMap=new ArrayList<Point2D.Double>();

		//find the offset value for x and y
		int offsetX;
		int offsetY;
		offsetX=(int)(carPos.x-(visibility/2));
		offsetY=(int)(carPos.y-(visibility/2));
		
		int x_min=(int)object.boundary.getMinX()-offsetX;
		int y_min=(int)object.boundary.getMinY()-offsetY;
		int x_max=(int)object.boundary.getMaxX()-offsetX;
		int y_max=(int)object.boundary.getMaxY()-offsetY;

		int i,j;
		for(i=x_min;i<x_max;i++)
		{
			for(j=y_min;j<y_max;j++)
			{
				if(i<100&&i>0&&j<100&&j>0)
				{
				Point2D.Double tmp=new Point2D.Double(i, j);
				coordinatesOnMap.add(tmp);
				}
			}
		}
		return coordinatesOnMap;
	}
	public static ArrayList<Point2D.Double>  locateObejctOnMap(Point2D.Double carPos,int visibility,Road road)
	{
		ArrayList<Point2D.Double> coordinatesOnMap=new ArrayList<Point2D.Double>();
		int i,j;
		//find the offset value for x and y
		int offsetX;
		int offsetY;
		offsetX=(int)(carPos.x-(visibility/2));
		offsetY=(int)(carPos.y-(visibility/2));
		
		int X_min=(int)(carPos.x-(visibility/2));
		int Y_min=(int)(carPos.y-(visibility/2));
		
		int X_max=(int)(carPos.x+(visibility/2));
		int Y_max=(int)(carPos.y+(visibility/2));
		
		for(i=X_min;i<X_max;i++)
		{
			for(j=Y_min;j<Y_max;j++)
			{
				Point2D.Double tmp=new Point2D.Double(i,j);
				if(road.containsPoint(tmp))
				{
					tmp.x=tmp.x-offsetX;
					tmp.y=tmp.y-offsetY;
					if(tmp.x<0)
					{
						tmp.x=0;
					}
					if(tmp.x>99)
					{
						tmp.x=99;
					}
					if(tmp.y<0)
					{
						tmp.y=0;
					}
					if(tmp.y>99)
					{
						tmp.y=99;
					}
					

					coordinatesOnMap.add(tmp);
				}
			}
		}
		return coordinatesOnMap;
	}
	public static ArrayList<Point2D.Double>  locateObejctOnMap(Point2D.Double carPos,int visibility,RoadMarking[] marks){
		ArrayList<Point2D.Double> coordinatesOnMap=new ArrayList<Point2D.Double>();
		int x,y;
		//find the offset value for x and y
		int offsetX;
		int offsetY;
		offsetX=(int)(carPos.x-(visibility/2));
		offsetY=(int)(carPos.y-(visibility/2));
		
		for(RoadMarking tmp:marks)
		{
			x=(int)tmp.position.x-offsetX;
			y=(int)tmp.position.y-offsetY;
			if(x<100&&x>0&&y<100&&y>0)
			{
			Point2D.Double coordinate=new Point2D.Double(x, y);
			coordinatesOnMap.add(coordinate);
			}

			
		}
	
		return coordinatesOnMap;
	}
	
	public static ArrayList<Point2D.Double> locateObjectOnMap(Point2D.Double carPos,int visibility,Intersection intersection){
		ArrayList<Point2D.Double> coordinatesOnMap=new ArrayList<Point2D.Double>();

		//find the offset value for x and y
		int offsetX;
		int offsetY;
		offsetX=(int)(carPos.x-(visibility/2));
		offsetY=(int)(carPos.y-(visibility/2));
		
		int x_min=(int)(intersection.pos.x-offsetX);
		int y_min=(int)(intersection.pos.y-offsetY);
		int x_max=(int)(intersection.pos.x+(intersection.length)-offsetX);
		int y_max=(int)(intersection.pos.y+(intersection.width)-offsetY);

		int i,j;
		for(i=x_min;i<x_max;i++)
		{
				Point2D.Double tmp=new Point2D.Double(i, y_min);
				coordinatesOnMap.add(tmp);
			
		}
		for(i=y_min;i<y_max;i++)
		{
				Point2D.Double tmp=new Point2D.Double(x_min, i);
				coordinatesOnMap.add(tmp);
			
		}
		for(i=x_min;i<x_max;i++)
		{
				Point2D.Double tmp=new Point2D.Double(i, y_max);
				coordinatesOnMap.add(tmp);
			
		}
		for(i=x_min;i<x_max;i++)
		{
				Point2D.Double tmp=new Point2D.Double(x_max, i);
				coordinatesOnMap.add(tmp);
			
		}
		return coordinatesOnMap;
	}

	
}
