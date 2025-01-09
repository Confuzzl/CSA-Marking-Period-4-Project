package com.vincent.libgdx.game.world.utils;

import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.vincent.libgdx.game.collisionutils.primitives.Ray;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.world.utils.rparts.RayPart;

public class RRay implements Nestable {
	public static Set<Renderable> set;
	public static Set<Renderable> removalSet;

	private Vec3 localPosition = new Vec3();

	private ModelBuilder modelBuilder;
	private Model model;
	private ModelInstance modelInstance;

	public RRay(Ray ray, double l, Color color) {
		modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		addRPart(new RayPart(ray, l, color));
		model = modelBuilder.end();
		modelInstance = new ModelInstance(model);

		set.add(this);
	}

	@Override
	public void refreshInstance() {
		modelInstance = new ModelInstance(model, (float) localPosition.x(), (float) localPosition.y(),
				(float) localPosition.z());
	}

	@Override
	public ModelBuilder getModelBuilder() {
		return modelBuilder;
	}

	@Override
	public Model getModel() {
		return model;
	}

	@Override
	public ModelInstance getModelInstance() {
		return modelInstance;
	}

	@Override
	public Vec3 getGlobalPosition() {
		return localPosition;
	}

	@Override
	public Vec3 getLocalPosition() {
		return localPosition;
	}

	@Override
	public void kill() {
		removalSet.add(this);
	}
}
