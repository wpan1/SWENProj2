package com.unimelb.swen30006.partc.ai.perception;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse.Classification;
import com.unimelb.swen30006.partc.core.infrastructure.TrafficLight;

/**
 * A classifying algorithm implements the classifying strategy
 * 
 * @author group 9
 *
 */
public class SimpleClassifyStrategy implements ClassifyStrategy{

	@Override
	public void classifyObject(ObjectBlock object) {
		
		// space is true then building, traffic light, sign, street light or car
		if(object.getBlock().get(0).space){
			if(compareTrafficLight(object)){
				object.setType(Classification.TrafficLight);
			}
			else if(compareSign(object)){
				object.setType(Classification.Sign);
			}
			else if(compareStreetLight(object)){
				object.setType(Classification.StreetLight);
			}
			else if(object.getBlock().get(0).velocity.isZero() && object.getBlock().size()==1){
				object.setType(Classification.Car);
			}
			else {
				object.setType(Classification.Building);
			}
			
		}
		//road marking or lane marking
		else if(!object.getBlock().get(0).space){
			if (object.getBlock().get(0).colour == Color.LIGHT_GRAY){
				if(object.getBlock().size()==1){
					object.setType(Classification.LaneMarking);
				} else {
					object.setType(Classification.RoadMarking);
				}
			}
			else{
				object.setType(null);
			}
		}
		
	}
	
	/** Determine whether the object is a traffic light and the state of traffic light*/
	private boolean compareTrafficLight(ObjectBlock object){
		if(object.getBlock().size()!=1){
			return false;
		}	
		if (object.getBlock().get(0).colour==Color.RED){
			object.addInformation("State", TrafficLight.State.Red);
			return true;
		}
		else if(object.getBlock().get(0).colour==Color.GREEN){
			object.addInformation("State", TrafficLight.State.Green);
			return true;
		}
		else if(object.getBlock().get(0).colour==Color.YELLOW){
			object.addInformation("State", TrafficLight.State.Amber);
			return true;
		}
		return false;
	}
	
	/** Determine whether the object is a sign */
	private boolean compareSign(ObjectBlock object){
		float red = 180/255f;
		float green = 209/255f;
		float blue = 18/255f;
		Color sign = new Color(red, green, blue, 1f);
		if (object.getBlock().get(0).colour.equals(sign)){
			return true;
		}
		return false;
	}
	
	/** Determine whether the object is a street light */
	private boolean compareStreetLight(ObjectBlock object){
		float red = 254/255f;
		float green = 209/255f;
		float blue = 18/255f;
		Color streetLight = new Color(red, green, blue, 1f);
		if (object.getBlock().get(0).colour.equals(streetLight)){
			return true;
		}
		return false;
	}

}