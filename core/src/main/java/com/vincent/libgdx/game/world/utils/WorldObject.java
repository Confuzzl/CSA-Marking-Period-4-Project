package com.vincent.libgdx.game.world.utils;

import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.world.World;
import com.vincent.libgdx.game.world.entity.Entity;
import com.vincent.libgdx.game.world.utils.rparts.RPart;
import com.vincent.libgdx.game.world.utils.rparts.RenderPart;

public class WorldObject extends Interactable {
	public static Set<WorldObject> set;
	public static Set<WorldObject> removalSet;

	private static int count;
	private int ID;

	public WorldObject(World world, Vec3 position, CollidablePolyhedron hitbox, Color hitboxColor,
			RenderPart renderPart, RPart... rParts) {
		super(world, position, hitbox, hitboxColor, CollisionType.NORMAL, renderPart, rParts);

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
	public void onRay(Entity caster, double distance, boolean shoot) {
		if (caster != getWorld().getPlayer())
			return;
	}

	@Override
	public void onRayClosest(Entity caster, double distance, boolean shoot) {
		if (caster != getWorld().getPlayer())
			return;
	}

	@Override
	public void onRayOff(Entity caster, double distance, boolean shoot) {
		if (caster != getWorld().getPlayer())
			return;
	}

	@Override
	public int ID() {
		return ID;
	}

	@Override
	public String toString() {
		return String.format("WorldObject #%d", ID);
	}

	@Override
	public void kill() {
		removalSet.add(this);
	}
}
