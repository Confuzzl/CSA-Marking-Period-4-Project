package com.vincent.libgdx.game.collisionutils.features;

import com.vincent.libgdx.game.collisionutils.shapes.BasePolyhedron;

public abstract class Feature<T extends BasePolyhedron> {
	private T parent;
	private int ID;

//	public abstract VoronoiRegion getRegion();

	protected Feature(T parent, int ID) {
		this.parent = parent;
		this.ID = ID;
	}

	public T getOwner() {
		return parent;
	}

	public int getID() {
		return ID;
	}

	@Override
	public abstract String toString();

//	public abstract void generateRegion();

//	public abstract double distanceTo(VectorLike p);
}
