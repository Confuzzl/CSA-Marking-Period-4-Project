package com.vincent.libgdx.game.world;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.world.entity.Entity;
import com.vincent.libgdx.game.world.entity.Player;
import com.vincent.libgdx.game.world.utils.RRay;
import com.vincent.libgdx.game.world.utils.Renderable;
import com.vincent.libgdx.game.world.utils.Tracer;
import com.vincent.libgdx.game.world.utils.WorldObject;

public class World {
	public static Vec3 light = new Vec3(-3, -4, -5).getNormalize();
	private Player player;

	private Set<WorldObject> worldObjects = new HashSet<WorldObject>();
	private Set<WorldObject> worldObjectsToClear = new HashSet<WorldObject>();
	private Set<Entity> entities = /* new HashSet<Entity>() */ConcurrentHashMap.newKeySet();
	private Set<Entity> entitiesToClear = new HashSet<Entity>();
	private Set<Renderable> renderables = new HashSet<Renderable>();
	private Set<Renderable> renderablesToClear = new HashSet<Renderable>();

	public World() {
		init();
	}

	public void init() {
		WorldObject.set = worldObjects;
		WorldObject.removalSet = worldObjectsToClear;
		Entity.set = entities;
		Entity.removalSet = entitiesToClear;
		RRay.set = renderables;
		RRay.removalSet = renderablesToClear;
		Tracer.set = renderables;
		Tracer.removalSet = renderablesToClear;

		LegendOfHolmer.world = this;
		LegendOfHolmer.start();
	}

	public void advance() {
		clear(worldObjects, worldObjectsToClear);
		clear(entities, entitiesToClear);
		clear(renderables, renderablesToClear);

		entities.forEach(Entity::update);
		renderables.forEach(Renderable::update);

		LegendOfHolmer.update();
	}

	private void clear(Set<? extends Renderable> set, Set<? extends Renderable> removalSet) {
		set.removeAll(removalSet);
		removalSet.forEach(Renderable::dispose);
		removalSet.clear();
	}

	public Player getPlayer() {
		return player;
	}

	protected void setPlayer(Player p) {
		player = p;
	}

	public Set<Renderable> getRenderables() {
		return renderables;
	}

	public Set<WorldObject> getWorldObjects() {
		return worldObjects;
	}

	public Set<Entity> getEntities() {
		return entities;
	}
}
