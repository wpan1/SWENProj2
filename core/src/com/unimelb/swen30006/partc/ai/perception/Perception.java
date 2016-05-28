package com.unimelb.swen30006.partc.ai.perception;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.ai.interfaces.IPerception;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;

/**
 * 
 * @author Group 9
 * 
 *         The perception class implements the IPerception interface, which is
 *         the starting point of the whole Perception System.
 *
 */
public class Perception implements IPerception {

	private CombinedMap map;
	private Generator resultGenerator;
	private ArrayList<ObjectBlock> blocks;
	private DetectStrategy detector;
	private ClassifyStrategy classifier;

	@Override
	public PerceptionResponse[] analyseSurroundings(boolean[][] spaceMap, Color[][] colorMap, Vector2[][] velMap) {

		Vector2 self_absolute_velocity = new Vector2(0, 0);
		map = new CombinedMap();
		resultGenerator = new Generator();
		detector = new SimpleDetectStrategy();
		classifier = new SimpleClassifyStrategy();
		map.combineMaps(velMap, spaceMap, colorMap);
		/* detect possible objects */
		blocks = detector.detectObjects(map.getMap());

		/* classify all the objects */
		for (ObjectBlock block : blocks) {
			classifier.classifyObject(block);
			if (block.is_static()) {
				self_absolute_velocity = block.getBlock().get(0).velocity.rotate(180);
			}
		}

		/* classify and generate response */
		PerceptionResponse p[] = new PerceptionResponse[blocks.size()];
		int i = 0;
		for (ObjectBlock block : blocks) {
			if (block.getType() != null) {
				p[i++] = resultGenerator.generatePerceptionResponse(block, self_absolute_velocity);
			}
		}
		return p;
	}

}
