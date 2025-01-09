package com.vincent.libgdx.game.world.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.vincent.libgdx.game.Assets;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.world.entity.npcs.NPC;
import com.vincent.libgdx.game.world.utils.Tracer;
import com.vincent.libgdx.game.world.utils.rparts.TracerPart;

public class Weapons {
	public static Player owner;
	public static CameraController ownerCamera;

	public static enum Weapon {
		//@formatter:off
		FISTS(0, 0, 0, 0, 1, 0, null) {
			@Override
			public void fire() {
			}
		}, 
		M4MATTER(30, 30, 0.2, 1, 1, 0, Color.GOLD) , 
		THE_MORE_GUN(20, 12, 1, 1, 7, 5, Color.RED), 
		DBG9000(15, 50, 0.1, 1.5,1, 0, Color.LIME),
		TRI_CATCHER(100, 3, 2, 1.5, 3, 3, Color.PURPLE);
		//@formatter:on

		private Sound sound;
		private final int baseDamage;
		private final int capacity;
		private final double cooldown;
		private final double timeToReload;

		private float timeSinceLastFire;
		private int currentAmmo;
		private boolean reloading;
		private float reloadTimer;

		private int bulletsPerShot;
		private double bulletDegreeSpread;

		private Color tracerColor;

		private Weapon(int baseDamage, int capacity, double cooldown, double timeToReload, int bulletsPerShot,
				double bulletDegreeSpread, Color tracerColor) {
			if (name() != "FISTS")
				sound = Assets.getSound("weapon_" + name().toLowerCase());
			this.baseDamage = baseDamage;
			this.capacity = capacity;
			currentAmmo = capacity;
			this.cooldown = cooldown;
			this.timeToReload = timeToReload;
			this.bulletsPerShot = bulletsPerShot;
			this.bulletDegreeSpread = bulletDegreeSpread;
			this.tracerColor = tracerColor;
		}

		public void reset() {
			timeSinceLastFire = 0;
			currentAmmo = capacity;
			reloading = false;
			reloadTimer = 0;
		}

		public void update() {
			float dt = Gdx.graphics.getDeltaTime();
			timeSinceLastFire += dt;
			if (reloading) {
				reloadTimer += dt;
				if (reloadTimer >= timeToReload) {
					reloadTimer = 0;
					reloading = false;
					currentAmmo = capacity;
				}
			}
		}

		public void initiateReload() {
			if (!reloading)
				reloading = true;
		}

		public boolean canFire() {
			return timeSinceLastFire >= cooldown && currentAmmo > 0 && !reloading && NPC.current == null;
		}

		public void fire() {
			if (!canFire())
				return;
			if (sound != null)
				sound.play(0.25f);
			timeSinceLastFire = 0;
			currentAmmo--;

			Tracer tracer = new Tracer();
			for (int i = -bulletsPerShot / 2; i < bulletsPerShot / 2 + 1; i++)
				query(tracer, ownerCamera.getRotX() + bulletDegreeSpread * i, ownerCamera.getRotY());
			tracer.finishTracer();
		}

		private void query(Tracer tracer, double x, double y) {
			Vec3 origin = ownerCamera.getGlobalPosition();
			Vec3 direction = RayEmitter.directionAt(x, y);
			double l = ownerCamera.query(direction, true);

			tracer.addRPart(
					new TracerPart(origin.add(ownerCamera.getDirection()).sub(0, 0.25, 0), direction, l, tracerColor));
		}

		public int getDamage() {
			return baseDamage;
		}

		public int getCurrentAmmo() {
			return currentAmmo;
		}

		public int getCapacity() {
			return capacity;
		}

		public float getCurrentTime() {
			return timeSinceLastFire;
		}

		public double getCooldown() {
			return cooldown;
		}

		public float getReloadTimer() {
			return reloadTimer;
		}

		public double getTimeToReload() {
			return timeToReload;
		}

		public boolean isReloading() {
			return reloading;
		}
	}
}
