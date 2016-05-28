package com.unimelb.swen30006.partc.iplanning;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.infrastructure.TrafficLight;
import com.unimelb.swen30006.partc.core.infrastructure.TrafficLight.State;
import com.unimelb.swen30006.partc.core.objects.Car;

public class PerceptionHandler {
	private Car car;
	private static final float MAX = 1000;
	private Color color;

	// Enum for data
	public enum Classification {
		Building, TrafficLight, Car, RoadMarking, LaneMarking, Sign, StreetLight
	}

	private State state;

	public PerceptionHandler(Car c){
		this.car = c;
	}
	

	/**
	 * 
	 * @param results: a list of incoming events and need to be ordered.
	 * @return the highest event that is nearest the car
	 */
	public PerceptionResponse prioritiseResponse(PerceptionResponse[] results) {
		int id = 0;
		for(int i = 0; i<results.length;i++){
			// to be ordered
		}
		// change position
		PerceptionResponse temp = results[0];
		results[0] = results[id];
		results[id] = temp;
		return results[0];
	}
	
	/**
	 * 
	 * @param result: the incoming event that the car need deal with
	 */
	public void dealHighestResponse(PerceptionResponse result ) {
		// if the object in the front of the car is another car
		if(result.objectType.equals(Classification.Car)){
			if(car.getVelocity().len() > result.velocity.len()){
				car.brake();
			}
		}
		
		/** or the ojbect is a traffic light
		    but there is a premise that hashmap<string, object>, we
		    need have the same key(string) to map object, otherwise it 
		    just wont work, since the string is different. 
		*/
		else if(result.objectType.equals(Classification.TrafficLight)){
			if(result.information.get("State") == TrafficLight.State.Red){
				car.brake();
			}
		}
	}
	
}
