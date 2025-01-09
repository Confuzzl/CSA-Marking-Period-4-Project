package com.vincent.libgdx.game.world.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.vincent.libgdx.game.collisionutils.Collidable;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.world.World;
import com.vincent.libgdx.game.world.entity.Entity;
import com.vincent.libgdx.game.world.utils.rparts.EdgePart;
import com.vincent.libgdx.game.world.utils.rparts.FeaturePart;
import com.vincent.libgdx.game.world.utils.rparts.RPart;
import com.vincent.libgdx.game.world.utils.rparts.RenderPart;

public abstract class Interactable implements Collidable, Nestable {
	private World world;
	private Vec3 localPosition = new Vec3();
//	private Quat rotation = new Quat();

	private ModelBuilder modelBuilder;
	private Model model;
	private ModelInstance modelInstance;

	private RenderPart renderPart;
	private EdgePart hitboxPart;
	private EdgePart selectedPart;
	private FeaturePart featurePart;

	private CollidablePolyhedron hitbox;

	public static enum CollisionType {
		NORMAL(true, true), COLLISION_ONLY(true, false), RAY_ONLY(false, true), DISABLED(false, false);

		private boolean collision;
		private boolean ray;

		private CollisionType(boolean c, boolean r) {
			this.collision = c;
			this.ray = r;
		}

		public boolean canCollide() {
			return collision;
		}

		public boolean canIntersect() {
			return ray;
		}
	}

	private CollisionType collisionType = CollisionType.DISABLED;

	public Interactable(World world, Vec3 position, CollidablePolyhedron hitbox, Color hitboxColor,
			CollisionType collisionType, RenderPart renderPart, RPart... rParts) {
		this.world = world;
		this.localPosition.set(position);
		this.hitbox = hitbox.copy();
		this.hitbox.setParent(this);
		this.collisionType = collisionType;

		modelBuilder = new ModelBuilder();
		modelBuilder.begin();

		this.renderPart = renderPart;
		if (renderPart != null)
			addRPart(renderPart);
		hitboxPart = new EdgePart(Vec3.ZERO, hitbox.toUV(), hitboxColor, "hitbox");
		addRPart(hitboxPart);
		selectedPart = new EdgePart(Vec3.ZERO, hitbox.toUV(), Color.WHITE, "selected");
		addRPart(selectedPart);
		featurePart = new FeaturePart(Vec3.ZERO, hitbox.toUV());
		addRPart(featurePart);
		for (RPart rPart : rParts)
			addRPart(rPart);

		model = modelBuilder.end();
	}

	protected void finishConstructing() {
		resetRender();
		setLocalPosition(localPosition);
	}

	public abstract void resetRender();

	public RenderPart getRenderPart() {
		return renderPart;
	}

	public EdgePart getHitboxPart() {
		return hitboxPart;
	}

	public void toggleHitboxPart(boolean b) {
		hitboxPart.toggle(b);
	}

	public EdgePart getSelectedPart() {
		return selectedPart;
	}

	public void toggleSelectedPart(boolean b) {
		selectedPart.toggle(b);
	}

	public FeaturePart getFeaturePart() {
		return featurePart;
	}

	@Override
	public ModelInstance getModelInstance() {
		return modelInstance;
	}

	public World getWorld() {
		return world;
	}

	@Override
	public ModelBuilder getModelBuilder() {
		return modelBuilder;
	}

	@Override
	public Model getModel() {
		return model;
	}

	public CollisionType getCollisionType() {
		return collisionType;
	}

	public void setCollisionType(CollisionType type) {
		collisionType = type;
	}

	@Override
	public CollidablePolyhedron getCollider() {
		return hitbox;
	}

	@Override
	public Vec3 getGlobalPosition() {
		return localPosition;
	}

	@Override
	public Vec3 getLocalPosition() {
		return localPosition;
	}

//	@Override
//	public Quat getQuat() {
//		return rotation;
//	}
//
//	@Override
//	public void setRotation(Vec3 r) {
//		getQuat().setRotation(r);
//		for (RPart n : rParts)
//			n.setRotation(r);
//	}
	@Override
	public void refreshInstance() {
		modelInstance = new ModelInstance(model, (float) localPosition.x(), (float) localPosition.y(),
				(float) localPosition.z());
	}

	@Override
	public void dispose() {
		Nestable.super.dispose();
//		System.out.printf("%s, %s\n", localPosition.generate(), hitbox.generate());
	}

	public abstract void onRay(Entity caster, double distance, boolean shoot);

	public abstract void onRayClosest(Entity caster, double distance, boolean shoot);

	public abstract void onRayOff(Entity caster, double distance, boolean shoot);

	public abstract int ID();
}
