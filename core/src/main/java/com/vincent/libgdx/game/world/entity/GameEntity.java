package com.vincent.libgdx.game.world.entity;

import com.badlogic.gdx.graphics.Color;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.world.World;

public abstract class GameEntity extends Entity {
	private final int maxHealth;
	private int health;

	public GameEntity(World world, Vec3 position, CollidablePolyhedron hitbox, CollisionType collisionType,
			boolean ignoreGravity, String decalName, double decalWidth, double decalHeight, double decalOffset,
			int health) {
		super(world, position, hitbox, Color.RED, collisionType, null, ignoreGravity, decalName, decalWidth,
				decalHeight, decalOffset);
		this.maxHealth = health;
		this.health = health;
	}

	@Override
	public void update() {
		super.update();
		if (health <= 0)
			onDeath();
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getHealth() {
		return health;
	}

	public double getPercentHealth() {
		return (double) health / maxHealth;
	}

	public abstract void onDeath();

	public void resetHealth() {
		health = maxHealth;
	}

	public void changeHealth(int h) {
		health += h;
	}
}
