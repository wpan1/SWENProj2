package com.unimelb.swen30006.partc.ai.perception;


import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse.Classification;
import com.unimelb.swen30006.partc.core.World;

/**
 * This class is a response generator
 * 
 * @author Group 9
 *
 */
public class Generator {
	
	/**
	 * calculate the distance between the object and the car
	 * @param object
	 * @return
	 */
	private float calculateDistance(ObjectBlock object){
		float distance = World.VISIBILITY_RADIUS;
		for(int i = 0; i<object.getBlock().size(); i++){
			float squareX =  (float) Math.pow(object.getBlock().get(i).x-World.VISIBILITY_RADIUS/2,2);
			float squareY = (float) Math.pow(object.getBlock().get(i).y-World.VISIBILITY_RADIUS/2,2);
			float tmpDistance = (float) Math.sqrt(squareX+squareY);
			if(tmpDistance<distance){
				distance = tmpDistance;
			}
		}
		return distance;
	}
	
	/**
	 * calculate the absolute velocity between the object and the car
	 * @param object
	 * @return
	 */
	private Vector2 calculateVelocity(ObjectBlock object, Vector2 self_velocity){
		int center = World.VISIBILITY_RADIUS/2;
		if(!object.getType().equals(Classification.Car)){
			return new Vector2(0,0);
		} else {
			if(object.getBlock().get(0).x==center && object.getBlock().get(0).y==center){
				return self_velocity;
			}
			else{
				return self_velocity.sub(object.getBlock().get(0).velocity);
			}
		}
	}
	
	/**
	 * calculate direction between the object and the car
	 * @param object
	 * @return
	 */
	private Vector2 calculateDirection(ObjectBlock object){
		return object.getBlock().get(0).velocity;
	}
	
	/**
	 * calculate the time that object and the car collide
	 * @param object
	 * @return
	 */
	private float calculateTimeToCollide(ObjectBlock object){
		int center = World.VISIBILITY_RADIUS/2;
		float min_time = Float.MAX_VALUE;
		
		for(CombinedPoint p : object.getBlock()){
			
			float tx = (p.x-center)/p.velocity.x;
			float ty = (p.y-center)/p.velocity.y;

			if(tx<=ty+0.5 && tx>= ty-0.5){
				if(min_time<tx){
					min_time = tx;
				}
			}	
		}
		return min_time;
	}
	
	/**
	 * generate the perception 
	 * a little different from the design model, it passes self_absolute_velocity as argument
	 * 
	 * @param object
	 * @param self_absolute_velocity
	 * @return
	 */
	public PerceptionResponse generatePerceptionResponse(ObjectBlock object,Vector2 self_absolute_velocity){
		
		float distance = calculateDistance(object);
		Vector2 velocity = calculateVelocity(object,self_absolute_velocity);;
		Vector2 direction = calculateDirection(object);
		float timeToCollision = calculateTimeToCollide(object) ;
		return new PerceptionResponse(distance, velocity, direction, timeToCollision, object.getType(),object.getInformation());
	}

}
