package com.vincent.libgdx.game.collisionutils.features;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.primitives.VectorLike;
import com.vincent.libgdx.game.collisionutils.shapes.BasePolyhedron;

public class BaseEdge<T extends BasePolyhedron<V, ?, ?>, V extends BaseVertex<T>> extends Feature<T>
		implements VectorLike {
	private V tail, head;

	protected BaseEdge(T parent, int ID, V tail, V head) {
		super(parent, ID);
		this.tail = tail;
		this.head = head;
	}

	@Override
	public Vec3 asVec3() {
		return head.sub(tail);
	}

	public V getTail() {
		return tail;
	}

	public V getHead() {
		return head;
	}

	public Vec3 evalAt(double l) {
		return tail.add(head.sub(tail).mult(l));
	}

//	private static double clamp(double n) {
//		return Math.max(0, Math.min(n, 1));
//	}
//
//	public static Vec3 evalAt(VectorLike tail, VectorLike head, double l) {
//		return tail.add(head.sub(tail).mult(l));
//	}
//
//	public static double distanceTo(VectorLike tail, VectorLike head, VectorLike p) {
//		Vec3 dir = head.sub(tail);
//		Vec3 closestPoint = evalAt(tail, head, clamp(p.sub(tail).dot(dir) / dir.dot(dir)));
//		return p.sub(closestPoint).getMagnitude();
//	}

	@Override
	public String toString() {
		return String.format("%s #%d of %s: %s", getClass().getSimpleName(), getID(), getOwner().getID(), asVec3());
	}
}
