package com.vincent.libgdx.game.collisionutils.rtree;

import com.badlogic.gdx.utils.Array;
import com.vincent.libgdx.game.collisionutils.Collidable;

public class RTree {
	private Node root;

	private static class Node {
		private boolean isRoot;
		private boolean isLeaf;
		private Array<Node> children;
		private Array<Collidable> objects;
	}
}
