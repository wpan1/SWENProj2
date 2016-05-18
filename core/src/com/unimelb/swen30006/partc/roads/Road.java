package com.unimelb.swen30006.partc.roads;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * This class provides functionality for use within the simulation system. It is NOT intended to be
 * read or understood for SWEN30006 Part C. Comments have been intentionally removed to reinforce
 * this. We take no responsibility if you use all your time trying to understand this code.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public class Road {

	// Private static constants for roads
	private static final Color ROAD_COLOUR = Color.DARK_GRAY;
	private static final int MARKER_SPACING = 5;

	// Private variables
	private final float length;
	private final float width;
	private final Point2D.Double startPos;
	private final Point2D.Double endPos;
	private final int numLanes;
	private final int[] lanes;
	private final RoadMarking[] markers;
	private Rectangle2D shape;

	public Road(Point2D.Double startPos, Point2D.Double endPos, float width, int numLanes, int[] laneMask) {
		this.startPos = startPos;
		this.endPos = endPos;
		this.numLanes = numLanes;
		this.length = (float) endPos.distance(startPos);
		this.width = width;
		this.lanes = laneMask;
		this.shape = createShape();
		// Generate the road markers
		ArrayList<RoadMarking> markings = generateRoadMarkers();
		this.markers = markings.toArray(new RoadMarking[markings.size()]);
	}

	public int getNumLanes(){
		return this.numLanes;
	}

	public int laneDirection(int index){
		return this.lanes[index];
	}

	public float laneWdith(){
		return this.width / (float)this.numLanes;
	}

	public void render(ShapeRenderer r){
		// Draw the road
		Color old = r.getColor();
		r.setColor(ROAD_COLOUR);
		Vector2 start = new Vector2((float)startPos.x, (float)startPos.y);
		Vector2 end = new Vector2((float)endPos.x, (float)endPos.y);
		r.rectLine(start, end, width);
		r.setColor(old);

		// Draw all road markers
		for(RoadMarking m: markers){
			m.render(r);
		}
	}	

	private ArrayList<RoadMarking> generateRoadMarkers(){
		ArrayList<RoadMarking> markings = new ArrayList<RoadMarking>();

		// Calcualte the total number of markings per lane
		int markingsPerLane = (int) this.length / MARKER_SPACING;
		float laneSpacing = this.width / this.numLanes;

		// Calculate determine whether the line is horizontal or vertical
		boolean horizontal = this.startPos.y == this.endPos.y;

		// Find the shifted start point (the bottom left position of the road)
		Point2D.Double shiftedStart = shiftedPoint(this.startPos, !horizontal, -width/2);		

		// For each pair of lanes, build the markings
		for(int i=0; i<(numLanes-1); i++){
			// Shift to the next lane marking, in the operate direction
			shiftedStart = shiftedPoint(shiftedStart, !horizontal, laneSpacing);
			Point2D.Double markerPos = shiftedStart;
			// Iterate through the line to create marking
			for(int j=0; j<markingsPerLane; j++){
				markerPos = shiftedPoint(markerPos, horizontal, MARKER_SPACING);
				markings.add(new RoadMarking(null, markerPos, horizontal, RoadMarking.Type.LaneMarking));
			}
		}
		return markings;
	}


	@Override
	public String toString() {
		return "Road [length=" + length + ", width=" + width + ", startPos=" + startPos + ", endPos=" + endPos
				+ ", numLanes=" + numLanes + "]";
	}

	public float minDistanceTo(Point2D.Double pos){		
		// If we contain the point, the distance is 0
		if(this.shape.contains(pos)){
			return 0;
		}

		// Find the closest point by creating lines and returning the minum distance between the point and any of those lines
		Line2D.Double bottom = new Line2D.Double(shape.getMinX(), shape.getMinY(), shape.getMaxX(), shape.getMinY());
		Line2D.Double right = new Line2D.Double(shape.getMaxX(), shape.getMinY(), shape.getMaxX(), shape.getMaxY());
		Line2D.Double top = new Line2D.Double(shape.getMinX(), shape.getMaxY(), shape.getMaxX(), shape.getMaxY());
		Line2D.Double left = new Line2D.Double(shape.getMinX(), shape.getMinY(), shape.getMinX(), shape.getMaxY());

		// Calcaulte the minDist
		float min1 = (float) Math.min(bottom.ptSegDist(pos), right.ptSegDist(pos));
		float min2 = (float) Math.min(top.ptSegDist(pos), left.ptSegDist(pos));
		return Math.min(min1, min2);
	}

	public boolean containsPoint(Point2D.Double pos){
		return this.shape.contains(pos);
	}

	private Point2D.Double shiftedPoint(Point2D.Double point, boolean horizontal, double amount){
		double x = horizontal ? point.x + amount : point.x; 
		double y = horizontal ? point.y : point.y + amount;
		return new Point2D.Double(x,y);
	}

	private Rectangle2D.Double createShape(){
		// Create the initial shape
		Rectangle2D.Double shape = new Rectangle2D.Double();

		// Create two corners, starting with the perpendicular vector
		Vector2 line = new Vector2((float)(this.endPos.x - this.startPos.x),(float)(this.endPos.y - this.startPos.y));
		Vector2 perp = new Vector2(line.y, -line.x);
		perp = perp.nor();
		perp = perp.scl(this.width/2);

		// Create the corners
		Vector2 bottomLeft = new Vector2((float) this.startPos.x, (float) this.startPos.y);
		Vector2 topRight = new Vector2((float) this.endPos.x, (float) this.endPos.y);
		bottomLeft = bottomLeft.sub(perp);
		topRight = topRight.add(perp);

		// Upload the shape
		shape.setFrameFromDiagonal(new Point2D.Double(bottomLeft.x, bottomLeft.y), new Point2D.Double(topRight.x, topRight.y));
		return shape;
	}

	
	public float getLength() {
		return length;
	}

	public float getWidth() {
		return width;
	}

	public Point2D.Double getStartPos() {
		return startPos;
	}

	public Point2D.Double getEndPos() {
		return endPos;
	}

	public RoadMarking[] getMarkers() {
		return markers;
	}

	public boolean overlaps(Rectangle2D.Double rect) {
		return this.shape.intersects(rect);
	}
}
