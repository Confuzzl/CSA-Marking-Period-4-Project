package com.vincent.libgdx.game.world.entity;

import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.vincent.libgdx.game.Assets;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.collisionutils.vclip.VClip;
import com.vincent.libgdx.game.collisionutils.vclip.VClip.VClipInfo;
import com.vincent.libgdx.game.world.World;
import com.vincent.libgdx.game.world.utils.Interactable;
import com.vincent.libgdx.game.world.utils.rparts.RPart;
import com.vincent.libgdx.game.world.utils.rparts.RenderPart;

public class Entity extends Interactable {
	public static Set<Entity> set;
	public static Set<Entity> removalSet;

	private static int count;
	private int ID;

	private final Vec3 startPosition = new Vec3();
	private static final double VOID_DEPTH = -200;
	private Vec3 movement = new Vec3();
	private static final double stepHeight = 10;
	private static final Vec3 stepCheck = new Vec3(0, stepHeight, 0, true);

	private static final double gAccel = 100;
	private Vec3 down = new Vec3();
	private boolean ignoreGravity = true;
	private static final double gTerminalVelocity = 5;

	private double jumpStrength = 40;
	private Vec3 up = new Vec3();
	private boolean inJump = false;

	private boolean onFloor = false;
	private static final double floorDistanceCheck = 0.1;
	private static final Vec3 floorCheck = new Vec3(0, -floorDistanceCheck, 0, true);

	private boolean underCeiling = false;
	private static final double ceilingDistanceCheck = 0.1;
	private static final Vec3 ceilingCheck = new Vec3(0, ceilingDistanceCheck, 0, true);

	private RayEmitter rayEmitter;
	private Vec3 rayEmitterDefaultOffset = new Vec3();

	private ObjectMap<Interactable, VClipInfo> cachedPairs = new ObjectMap<Interactable, VClipInfo>();
//	private ObjectSet<Interactable> colliders = new ObjectSet<Interactable>();
	private ObjectSet<Interactable> downColliders = new ObjectSet<Interactable>();
	private ObjectSet<Interactable> upColliders = new ObjectSet<Interactable>();

	private Decal decal;
	private double decalOffset;

	public Entity(World world, Vec3 position, CollidablePolyhedron hitbox, Color hitboxColor,
			CollisionType collisionType, RenderPart renderPart, boolean ignoreGravity, String decalName,
			double decalWidth, double decalHeight, double decalOffset, RPart... rParts) {
		super(world, position, hitbox, hitboxColor, collisionType, renderPart, rParts);
		this.ignoreGravity = ignoreGravity;
		this.decal = decalName == null ? null
				: Decal.newDecal((float) decalWidth, (float) decalHeight,
						new TextureRegion(Assets.getTexture(decalName)), true);
		this.decalOffset = decalOffset;
		startPosition.setAndLock(position);

		set.add(this);
		ID = count++;

		finishConstructing();
	}

	@Override
	public void resetRender() {
		toggleHitboxPart(false);
		toggleSelectedPart(false);
		refreshInstance();
	}

	public int getID() {
		return ID;
	}

	@Override
	public void update() {
		floorCheck();
		ceilingCheck();

		if (!ignoreGravity)
			handleGravity();

		if (getCollisionType().canCollide()) {
			if (collisionCheckFor(down, getWorld().getWorldObjects()))
				if (inJump)
					up.reset();
			collisionCheckFor(up, getWorld().getWorldObjects());
			if (collisionCheckFor(movement, getWorld().getWorldObjects()))
				collisionCheckFor(movement.add(stepCheck), getWorld().getWorldObjects());
		}

		Vec3 net = movement.add(down).add(up);
		addLocalPosition(net);

		if (getGlobalPosition().y() < VOID_DEPTH) {
			setLocalPosition(startPosition);
			down.reset();
			up.reset();
		}

		if (rayEmitter != null)
			rayEmitter.update();
	}

	private boolean collisionCheckForSet(Vec3 v, Set<? extends Interactable> set, ObjectSet<Interactable> colliderSet) {
		if (v.equals(Vec3.ZERO))
			return false;
		CollidablePolyhedron hitbox = getCollider();
		Vec3 originalPosition = hitbox.getLocalPosition().copy();
		hitbox.addLocalPosition(v);

		colliderSet.clear();

		for (Interactable i : set) {
			if (i == this || !i.getCollisionType().canCollide())
				continue;

			VClipInfo info = cachedCheck(i);
//			if (showVClip(i)) {
//				getFeaturePart().highlight(info.get(this));
//				i.getFeaturePart().highlight(info.get(i));
//			}
			if (!info.getState().allowed())
				colliderSet.add(i);
		}

		hitbox.setLocalPosition(originalPosition);
		if (colliderSet.size == 0)
			return false;
		return true;
	}

	private boolean collisionCheckFor(Vec3 v, Set<? extends Interactable> set) {
		if (v.equals(Vec3.ZERO))
			return false;

		CollidablePolyhedron hitbox = getCollider();
		Vec3 originalPosition = hitbox.getLocalPosition().copy();
		hitbox.addLocalPosition(v);

		for (Interactable i : set) {
			if (i == this || !i.getCollisionType().canCollide())
				continue;

			VClipInfo info = cachedCheck(i);
//			if (showVClip(i)) {
//				getFeaturePart().highlight(info.get(this));
//				i.getFeaturePart().highlight(info.get(i));
//			}
			if (!info.getState().allowed()) {
				v.reset();
				hitbox.setLocalPosition(originalPosition);
				return true;
			}
		}

		hitbox.setLocalPosition(originalPosition);
		return false;
	}

	public VClipInfo cachedCheck(Interactable object) {
		VClipInfo existing;
		VClipInfo put = ((existing = cachedPairs.get(object)) != null)
				? VClip.closestFeaturePair(this, object, existing.getN1(), existing.getN2())
				: VClip.closestFeaturePair(this, object);
		if (!put.getState().isError())
			cachedPairs.put(object, put);
		return put;
	}

	private void floorCheck() {
		onFloor = collisionCheckForSet(floorCheck, getWorld().getWorldObjects(), downColliders);
	}

	private void ceilingCheck() {
		underCeiling = collisionCheckForSet(ceilingCheck, getWorld().getWorldObjects(), upColliders);
	}

	private void handleGravity() {
		if (onFloor) {
			down.reset();
			return;
		}
		double dt = Gdx.graphics.getDeltaTime();
		down.subEquals(0, gAccel * dt * dt, 0);
	}

	public Decal getDecal() {
		return decal;
	}

	public void initiateJump() {
		if (onFloor) {
			up.set(0, jumpStrength * Gdx.graphics.getDeltaTime(), 0);
			inJump = true;
		}
	}

	protected boolean showVClip(Interactable i) {
		return false;
	}

	public boolean ignoreGravity() {
		return ignoreGravity;
	}

	public boolean onFloor() {
		return onFloor;
	}

	public boolean underCeiling() {
		return underCeiling;
	}

	public boolean inJump() {
		return inJump;
	}

	protected void setRayEmitter(RayEmitter rayEmitter) {
		this.rayEmitter = rayEmitter;
		rayEmitter.setParent(this);
		rayEmitterDefaultOffset.setAndLock(rayEmitter.getLocalPosition());
	}

	@Override
	public void setLocalPosition(Vec3 v) {
		super.setLocalPosition(v);
		Vec3 pos = getGlobalPosition();
		if (decal != null)
			decal.setPosition((float) pos.x(), (float) (pos.y() + decalOffset), (float) pos.z());
	}

//	public ObjectSet<Interactable> getColliders() {
//		return colliders;
//	}

	public ObjectSet<Interactable> getFloorColliders() {
		return downColliders;
	}

	public ObjectSet<Interactable> getCeilingColliders() {
		return upColliders;
	}

	public Vec3 getMovement() {
		return movement;
	}

	public Vec3 getDown() {
		return down;
	}

	public Vec3 getUp() {
		return up;
	}

	@Override
	public void onRay(Entity caster, double distance, boolean shoot) {
	}

	@Override
	public void onRayClosest(Entity caster, double distance, boolean shoot) {
	}

	@Override
	public void onRayOff(Entity caster, double distance, boolean shoot) {
	}

	@Override
	public int ID() {
		return ID;
	}

	@Override
	public String toString() {
		return String.format("Entity #%d", ID);
	}

	@Override
	public void kill() {
		removalSet.add(this);
	}
}
