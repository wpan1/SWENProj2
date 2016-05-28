package com.unimelb.swen30006.partc.iplanning;

import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.infrastructure.TrafficLight;
import com.unimelb.swen30006.partc.core.objects.Car;

public class PerceptionHandler {
	private Car car;

	// Enum for data
	public enum Classification {
		Building, TrafficLight, Car, RoadMarking, LaneMarking, Sign, StreetLight
	}

	public PerceptionHandler(Car c){
		this.car = c;
	}
	

	/**
	 * 
	 * @param results: a list of incoming events and need to be ordered.
	 * @return the highest event that is nearest the car
	 */
	public PerceptionResponse prioritiseResponse(PerceptionResponse[] results) {
		PerceptionResponse nextResponse = results[0];
		PerceptionResponse trafficresult = null;
		for(PerceptionResponse pr: results){
			// to be ordered
			if(nextResponse.timeToCollision > pr.timeToCollision){
				nextResponse = pr;
			}
			if(pr.objectType.equals(Classification.TrafficLight) && pr.distance < 10){
				trafficresult = pr;
			}
		}
		// change position
		if(trafficresult != null && nextResponse.distance < trafficresult.distance){
			return nextResponse;
		}
		else{
			return trafficresult;
		}
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
