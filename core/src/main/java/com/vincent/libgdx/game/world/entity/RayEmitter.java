package com.vincent.libgdx.game.world.entity;

import java.util.Objects;
import java.util.Set;

import com.badlogic.gdx.utils.ObjectSet;
import com.vincent.libgdx.game.collisionutils.Movable;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.world.utils.Interactable;
import com.vincent.libgdx.game.world.utils.WorldObject;

public class RayEmitter implements Movable {
	private Entity parent;
	private Vec3 localPosition = new Vec3();

	private Vec3 direction = new Vec3(1, 0, 0);
	private Vec3 vertical = new Vec3(0, 1, 0);
	private Vec3 horizontal = new Vec3(0, 0, 1);
	private double rotX, rotY;

	private ObjectSet<Interactable> interceptors = new ObjectSet<Interactable>();

	private RayQueryInfo<WorldObject> closestWorldObject = new RayQueryInfo<WorldObject>();
	private RayQueryInfo<Entity> closestEntity = new RayQueryInfo<Entity>();
	private RayQueryInfo<Interactable> closestInterceptor = new RayQueryInfo<Interactable>();

	public static class RayQueryInfo<T extends Interactable> {
		private T interceptor = null;
		private double l = Double.POSITIVE_INFINITY;

		private void set(T interceptor, double l) {
			this.interceptor = interceptor;
			this.l = l;
		}

		private void reset() {
			set(null, Double.POSITIVE_INFINITY);
		}

		public T getInterceptor() {
			return interceptor;
		}

		public double distance() {
			return l;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof RayQueryInfo<?> other)
				return interceptor == other.interceptor;
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(interceptor);
		}
	}

	public RayEmitter(Vec3 position) {
//		this.parent = parent;
		localPosition.set(position);
	}

	public void setParent(Entity parent) {
		this.parent = parent;
	}

	public void setRotation(double x, double y) {
		rotX = x % 360;
		rotY = Math.max(-89, Math.min(y, 89));

		direction.set(Vec3.X_PLUS);
		vertical.set(Vec3.Y_PLUS);
		horizontal.set(Vec3.Z_PLUS);

		direction.rotateAroundAxis(Vec3.Y_PLUS, x);
		vertical.rotateAroundAxis(Vec3.Y_PLUS, x);
		horizontal.rotateAroundAxis(Vec3.Y_PLUS, x);

		direction.rotateAroundAxis(horizontal, y);
		vertical.rotateAroundAxis(horizontal, y);
		horizontal.rotateAroundAxis(horizontal, y);
	}

	public static Vec3 directionAt(double x, double y) {
		Vec3 direction = Vec3.X_PLUS.copy();
		Vec3 horizontal = Vec3.Z_PLUS.copy();
		direction.rotateAroundAxis(Vec3.Y_PLUS, x);
		horizontal.rotateAroundAxis(Vec3.Y_PLUS, x);
		direction.rotateAroundAxis(horizontal, y);
		return direction;
	}

	public void rotate(double x, double y) {
		setRotation(rotX + x, rotY + y);
	}

	public void update() {
		query(false);
	}

	public double query(boolean shoot) {
		return query(direction, shoot);
	}

	public double query(Vec3 dir, boolean shoot) {
		interceptors.clear();
		closestWorldObject.reset();
		closestEntity.reset();

		querySub(parent.getWorld().getWorldObjects(), dir, shoot, closestWorldObject);
		querySub(parent.getWorld().getEntities(), dir, shoot, closestEntity);

		if (interceptors.size == 0)
			return Double.POSITIVE_INFINITY;

		if (closestEntity.l < closestWorldObject.l) {
			closestInterceptor.interceptor = closestEntity.interceptor;
			closestInterceptor.l = closestEntity.l;
		} else {
			closestInterceptor.interceptor = closestWorldObject.interceptor;
			closestInterceptor.l = closestWorldObject.l;
		}
		closestInterceptor.interceptor.onRayClosest(parent, closestInterceptor.l, shoot);
		return closestInterceptor.l;
	}

	private <T extends Interactable> void querySub(Set<T> set, Vec3 dir, boolean shoot, RayQueryInfo<T> out) {
		for (T e : set) {
			if (e == parent)
				continue;
			if (!e.getCollisionType().canIntersect())
				continue;

			double l = e.rayIntersectClosest(getGlobalPosition(), dir);

			if (l != Double.POSITIVE_INFINITY) {
				interceptors.add(e);
				if (l < out.l)
					out.set(e, l);
				e.onRay(parent, l, shoot);
				continue;
			}
			e.onRayOff(parent, l, shoot);
		}
	}

//	public void snap() {
//		setLocalPosition(parent.getGlobalPosition().add(0, parent.getRayOffset(), 0));
//	}

	public double getRotX() {
		return rotX;
	}

	public double getRotY() {
		return rotY;
	}

	public Vec3 getDirection() {
		return direction;
	}

	public Vec3 getVertical() {
		return vertical;
	}

	public Vec3 getHorizontal() {
		return horizontal;
	}

	public RayQueryInfo getClosestInterceptor() {
		return closestInterceptor;
	}

	public RayQueryInfo<WorldObject> getClosestWorldObject() {
		return closestWorldObject;
	}

	public RayQueryInfo<Entity> getClosestEntity() {
		return closestEntity;
	}

	public ObjectSet<Interactable> getInterceptors() {
		return interceptors;
	}

	@Override
	public Vec3 getGlobalPosition() {
		return localPosition.add(parent.getGlobalPosition());
	}

	@Override
	public Vec3 getLocalPosition() {
		return localPosition;
	}
}
