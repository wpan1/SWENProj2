package com.unimelb.swen30006.partc.ai.perception;

import java.util.ArrayList;
import java.util.Stack;

/**
 * A simple detecting strategy implements DetectStrategy
 * 
 * @author group 9
 *
 */
public class SimpleDetectStrategy implements DetectStrategy {

	private CombinedPoint[][] map;
	boolean marked[][];

	Stack<CombinedPoint> s = new Stack<CombinedPoint>();

	@Override
	public ArrayList<ObjectBlock> detectObjects(CombinedPoint[][] m) {
		map = m;
		marked = new boolean[map.length][map.length];
		ArrayList<ObjectBlock> blocks = new ArrayList<ObjectBlock>();
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				if (marked[i][j] == false) {
					ObjectBlock block = new ObjectBlock();
					formOneObject(block, map[i][j]);
					blocks.add(block);
				}
			}
		}
		return blocks;
	}

	/**
	 * using stack to perform a recursion. join all the similiar point together to a
	 * possible object
	 * @param block
	 * @param x
	 * @param y
	 * @return
	 */
	private void formOneObject(ObjectBlock block, CombinedPoint point) {
		s.add(point);
		while (!s.isEmpty()) {
			CombinedPoint p = s.pop();
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					try {
						if (marked[p.x + i][p.y + j] == false && map[p.x + i][p.y + j].isSimiliar(point)) {
							marked[p.x + i][p.y + j] = true;
							block.addPoint(map[p.x + i][p.y + j]);
							s.add(map[p.x + i][p.y + j]);
						}
					} catch (Exception e) {
						/* ignore index out of range exception */
					}

				}
			}
		}
		
	}

}
