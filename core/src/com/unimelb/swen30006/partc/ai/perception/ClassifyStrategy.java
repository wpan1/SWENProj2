package com.unimelb.swen30006.partc.ai.perception;

/**
 * classify interface
 * 
 * @author group 9
 *
 */
public interface ClassifyStrategy {
	
	/** Classify the type of an object */
	public void classifyObject(ObjectBlock object);

}