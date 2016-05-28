package com.unimelb.swen30006.partc.ai.sensor;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.ai.interfaces.ISensing;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;

public class CentralSensor implements ISensing {

	private WorldInspector wi;
	private CarInspector ci;
	private SpaceSensor spaceSensor;
	private VelocitySensor velocitySensor;
	private ColourSensor colourSensor;
	private WorldObject[] visibleObjects;
	private Road[] roads;
	private Intersection intersection;
	private int timePeriod;
	private Point2D.Double currentPos;
	private int visibility;
	private Vector2 currentVelocity;

	public CentralSensor(World w, Car c) {
		wi = new WorldInspector(w);
		ci = new CarInspector(c);
		spaceSensor = new SpaceSensor();
		velocitySensor = new VelocitySensor();
		colourSensor = new ColourSensor();

	}

	public boolean update(Point2D.Double pos, float delta, int visibility) {
		currentPos = pos;
		this.visibility = visibility;
		wi.updateVisibility(visibility);
		visibleObjects = wi.getVisibleObject(currentPos);
		roads = wi.getVisibleRoads(currentPos);
		intersection = wi.getIntersection(currentPos, visibility);
		timePeriod = wi.getTimePeriod();
		currentVelocity = ci.getVelocity(delta);

		return true;

	}

	public Vector2[][] getVelocityMap() {
		Vector2[][] vMap;
		vMap = velocitySensor.generateVelocityMap(currentPos, visibleObjects, visibility, currentVelocity);
		return vMap;
	}

	public boolean[][] getSpaceMap() {
		boolean[][] sMap;
		sMap = spaceSensor.generateSpaceMap(visibleObjects, visibility, currentPos);
		return sMap;
	}

	public Color[][] getColourMap() {
		Color colourMap[][];
		colourMap = colourSensor.generateColourMap(visibleObjects, roads, intersection, timePeriod, visibility,
				currentPos);

		return colourMap;
	}

}
