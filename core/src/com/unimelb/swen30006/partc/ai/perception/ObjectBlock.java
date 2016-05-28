package com.unimelb.swen30006.partc.ai.perception;

import java.util.ArrayList;
import java.util.HashMap;

import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse.Classification;

/**
 *  A detected object 
 *  
 * @author Group 9
 */
public class ObjectBlock {
	
	/** Type of the object */
	private Classification type;
	
	/** Points in this object */
	private ArrayList<CombinedPoint> block = new ArrayList<CombinedPoint>();
	
	/** Additional information about the object */
	private HashMap<String,Object> information = new HashMap<String,Object>();
	
	public void setType(Classification type){
		this.type = type;
	}
	
	public void addPoint(CombinedPoint point){
		this.block.add(point);
	}
	
	public Classification getType(){
		return this.type;
	}
	
	public ArrayList<CombinedPoint> getBlock(){
		return this.block;
	}
	
	public void addInformation(String key, Object o){
		this.information.put(key, o);
	}
	
	public HashMap<String,Object> getInformation(){
		return this.information;
	}
	
	public boolean is_static(){
		if(type == Classification.Building||type == Classification.LaneMarking||
				type == Classification.RoadMarking||type == Classification.Sign||
				type == Classification.StreetLight||type == Classification.TrafficLight
				)
			return true;
		return false;
	}

	public String toString(){
		if(type!=null)
			return type.toString()+"  "+block.size();
		else
			return "Road" + block.size();
	}

}
