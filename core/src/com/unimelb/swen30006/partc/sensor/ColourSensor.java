package com.unimelb.swen30006.partc.ai.sensor;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;
import com.unimelb.swen30006.partc.roads.RoadMarking;

public class ColourSensor {
	private static final Color MARKING_COLOUR = Color.LIGHT_GRAY;
	private static final Color ROAD_COLOUR = Color.DARK_GRAY;

	public Color[][] generateColourMap(WorldObject[] wo, Road[] road, Intersection intersection, int timePeriod,
			int visibility, Point2D.Double currentPos) {
		int i, j, k;
		Color[][] map = new Color[visibility][visibility];

		// set the map to empty
		for (i = 0; i < visibility; i++) {
			for (j = 0; j < visibility; j++) {
				map[i][j] = null;
			}
		}
		for (i = 0; i < wo.length; i++) {
			if (wo[i] == null) {
				break;
			}
			ArrayList<Point2D.Double> worldObjectCoordinates = new ArrayList<Point2D.Double>();
			worldObjectCoordinates = ObjectLocator.locateObjectOnMap(currentPos, visibility, wo[i]);
			for (Point2D.Double tmp : worldObjectCoordinates) {
				int x = (int) tmp.x;
				int y = (int) tmp.y;
				map[x][y] = wo[i].getColour().cpy();
			}
		}

		for (i = 0; i < road.length; i++) {
			ArrayList<Point2D.Double> roadCoordinates = new ArrayList<Point2D.Double>();
			roadCoordinates = ObjectLocator.locateObejctOnMap(currentPos, visibility, road[i]);
			RoadMarking[] marks = road[i].getMarkers();
			ArrayList<Point2D.Double> markCoordinates = new ArrayList<Point2D.Double>();
			markCoordinates = ObjectLocator.locateObejctOnMap(currentPos, visibility, marks);
			
			
			for (Point2D.Double tmp : roadCoordinates) {

				int x = (int) tmp.x;
				int y = (int) tmp.y;
				if(map[x][y]==MARKING_COLOUR)
				{
					continue;
				}
				if (map[x][y] == null) {
					map[x][y] = ROAD_COLOUR;
				} else {
					map[x][y].add(ROAD_COLOUR);
				}
			}
			for (Point2D.Double tmp : markCoordinates) {

				int x = (int) tmp.x;
				int y = (int) tmp.y;
				map[x][y]=MARKING_COLOUR;
			}
			
		}
		if (intersection != null) {
			ArrayList<Point2D.Double> intersectionCoordinates = new ArrayList<Point2D.Double>();
			intersectionCoordinates = ObjectLocator.locateObjectOnMap(currentPos, visibility, intersection);
			for (Point2D.Double tmp : intersectionCoordinates) {
				int x = (int) tmp.x;
				int y = (int) tmp.y;
				if(map[x][y]==ROAD_COLOUR)
				map[x][y] = MARKING_COLOUR;
			}

		}

		return map;
	}

	private Color[][] calcColour(HashMap<ArrayList<Object>, Point2D.Double> objectMap) {
		return null;
	}

}
