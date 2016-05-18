package com.unimelb.swen30006.partc.core.infrastructure;

import java.awt.geom.Point2D.Double;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.partc.core.ISteppable;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.core.objects.Infrastructure;
import com.unimelb.swen30006.partc.render_engine.TrafficLightRenderer;

/**
 * This class provides functionality for use within the simulation system. It is NOT intended to be
 * read or understood for SWEN30006 Part C. Comments have been intentionally removed to reinforce
 * this. We take no responsibility if you use all your time trying to understand this code.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public class TrafficLight extends Infrastructure implements ISteppable {

	// The state of the traffic lights
	public enum State {
		Red, Green, Amber, Off
	}
	
	// Private widths and lengths
	private static final float WIDTH = 2f;
	private static final float LENGHT = 2f;
	
	// Timers for amber, and values for colour
	private static final float AMBER_TIMER = 5f;
	private static final Color AMBER_COL = Color.YELLOW;
	private static final Color GREEN_COL = Color.GREEN;
	private static final Color RED_COL = Color.RED;
	
	// Internal data for state transitions and managing the traffic light
	private State state;
	private float timer;
	public final float cycleLength;
	
	public TrafficLight(Double pos, State initialState, float cycleDuration) {
		super(pos, WIDTH, LENGHT, null, new TrafficLightRenderer(), Infrastructure.InfrastructureType.TrafficLight);
		this.cycleLength = cycleDuration;
		this.state = initialState;
		setOffsetTimer();
	}
	
	@Override
	public Color getColour() {
		switch(this.state){
		case Red:
			return RED_COL;
		case Green:
			return GREEN_COL;
		case Amber:
			return AMBER_COL;
		default:
			return Color.BLACK;
		}
	}

	@Override
	public void update(float delta) {
		// First update timer	
		this.timer -= delta;
		
		// Then handle our state transistions
		if(this.timer <= 0){
			switch(this.state){
			case Red:
				this.state = State.Green;
				break;
			case Green:
				this.state = State.Amber;
				break;
			case Amber:
				this.state = State.Red;
				break;
			default:
				// Do nothing if off.
				break;
			}
			setOffsetTimer();
		}
		
		// Update our renderer
		TrafficLightRenderer r = (TrafficLightRenderer) getRenderObject();
		r.updateColor(getColour());
	}

	@Override
	public boolean canCollide(Car car) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	// Set a timer based on the current state.
	private void setOffsetTimer(){
		switch(this.state){
		case Green:
			this.timer = this.cycleLength - AMBER_TIMER;
			break;
		case Amber:
			this.timer = AMBER_TIMER;
			break;
		case Red:
			this.timer = this.cycleLength;
			break;
		default:
			this.timer = Float.MAX_VALUE;
		}
	}

}
