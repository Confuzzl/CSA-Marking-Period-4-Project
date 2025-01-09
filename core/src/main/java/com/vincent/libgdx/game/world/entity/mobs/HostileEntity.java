package com.vincent.libgdx.game.world.entity.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.world.LegendOfHolmer;
import com.vincent.libgdx.game.world.World;
import com.vincent.libgdx.game.world.entity.Entity;
import com.vincent.libgdx.game.world.entity.GameEntity;
import com.vincent.libgdx.game.world.entity.Player;
import com.vincent.libgdx.game.world.entity.Weapons.Weapon;
import com.vincent.libgdx.game.world.utils.Tracer;
import com.vincent.libgdx.game.world.utils.WorldObject;
import com.vincent.libgdx.game.world.utils.rparts.TracerPart;

public class HostileEntity extends GameEntity {
	public static Player target;
	private Weapon counter;
	private double counterMultiplier;

	private final int baseDamage;
	private final double rayOffset;
	private final double cooldown;
	private float timeSinceLastAttack;
	private final double speed;

	private final boolean active;

	public HostileEntity(World world, Vec3 position, CollidablePolyhedron hitbox, CollisionType collisionType,
			boolean ignoreGravity, String decalName, double decalWidth, double decalHeight, double decalOffset,
			int health, int baseDamage, double rayOffset, double cooldown, double speed, Weapon counter,
			double counterMultiplier, boolean active) {
		super(world, position, hitbox, active ? collisionType : CollisionType.RAY_ONLY, active ? ignoreGravity : true,
				decalName, decalWidth, decalHeight, decalOffset, health);
		this.baseDamage = baseDamage;
		this.rayOffset = rayOffset;
		this.cooldown = cooldown;
		this.speed = speed;
		this.counter = counter;
		this.counterMultiplier = counterMultiplier;
		this.active = active;
		LegendOfHolmer.enemyCount++;
		LegendOfHolmer.enemiesLeft++;
	}

	@Override
	public void update() {
		super.update();
		timeSinceLastAttack += Gdx.graphics.getDeltaTime();
		if (!active)
			return;
		if (timeSinceLastAttack > cooldown) {
			timeSinceLastAttack = 0;
			rayAttack();
		}
		moveTowardsTarget();
	}

	public void moveTowardsTarget() {
		Vec3 a = target.getGlobalPosition();
		Vec3 b = getGlobalPosition();
		getMovement().set(new Vec3(a.x(), 0, a.z()).sub(b.x(), 0, b.z()).getNormalizeTo(speed));
	}

	public void rayAttack() {
		Vec3 origin = getGlobalPosition().add(0, rayOffset, 0);
		Vec3 direction = target.getGlobalPosition().add(0, Player.DEFAULT_RAY_OFFSET - 1, 0).sub(origin);
		double worldL = queryWorld(origin, direction);
		double targetL = target.rayIntersectClosest(origin, direction);

		Tracer tracer = new Tracer();
		tracer.addRPart(new TracerPart(origin, direction, Math.min(worldL, targetL), Color.RED));
		tracer.finishTracer();
		if (target.getCollisionType().canIntersect() && targetL < worldL)
			target.changeHealth(-baseDamage);
	}

	private double queryWorld(Vec3 origin, Vec3 direction) {
		double min = Double.POSITIVE_INFINITY;
		for (WorldObject wo : getWorld().getWorldObjects()) {
			if (!wo.getCollisionType().canIntersect())
				continue;
			min = Math.min(min, wo.rayIntersectClosest(origin, direction));
		}
		return min;
	}

	protected void takeDamage() {
		Weapon weapon = target.getWeapon();
		double multiplier = weapon.equals(counter) ? counterMultiplier : 1;
		changeHealth((int) (-weapon.getDamage() * multiplier));
	}

	@Override
	public void onRayClosest(Entity caster, double l, boolean shoot) {
		if (caster == this || caster != target || !shoot)
			return;
		takeDamage();
	}

	@Override
	public void onDeath() {
		kill();
		LegendOfHolmer.enemiesLeft--;
	}
}
