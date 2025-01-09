package com.vincent.libgdx.game.world.entity.npcs;

import com.badlogic.gdx.graphics.Color;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.world.LegendOfHolmer;
import com.vincent.libgdx.game.world.World;
import com.vincent.libgdx.game.world.entity.Entity;

public class NPC extends Entity {
	public static NPC hover;
	public static NPC current;
	public static int textFlag = -1;

	private double interactDistance;

//	private boolean inSpeech;
	private String[] speeches;

	public NPC(World world, Vec3 position, CollidablePolyhedron hitbox, String decalName, double decalWidth,
			double decalHeight, double decalOffset, double interactDistance, String... speeches) {
		super(world, position, hitbox, Color.RED, CollisionType.RAY_ONLY, null, true, decalName, decalWidth,
				decalHeight, decalOffset);
		this.interactDistance = interactDistance;
		this.speeches = speeches;
	}

	public static String getText() {
		if (current == null)
			return null;
		if (textFlag < 0 || textFlag >= current.speeches.length)
			return null;
		return current.speeches[textFlag];
	}

	public int getTextFlag() {
		return textFlag;
	}

	@Override
	public void resetRender() {
		toggleHitboxPart(false);
		toggleSelectedPart(false);
		refreshInstance();
	}

	@Override
	public void onRayClosest(Entity caster, double distance, boolean shoot) {
		if (distance > interactDistance) {
			resetRender();
			return;
		}
		hover = this;
		toggleSelectedPart(true);
		refreshInstance();
	}

	public static void advanceSpeech() {
		if (current == null)
			if (hover != null)
				current = hover;
			else
				return;

		if (textFlag++ == current.speeches.length - 1) {
			current.kill();
			current = null;
			hover = null;
			textFlag = -1;
			LegendOfHolmer.toNextState();
		}
	}

	@Override
	public void onRayOff(Entity caster, double distance, boolean shoot) {
		hover = null;
		resetRender();
	}
}
