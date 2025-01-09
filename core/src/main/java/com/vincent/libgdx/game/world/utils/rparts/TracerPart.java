package com.vincent.libgdx.game.world.utils.rparts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;

public class TracerPart extends RPart {
	private Vec3 origin;
	private Vec3 direction;
	private double l;
	private Color color;

	public TracerPart(Vec3 origin, Vec3 direction, double l, Color color) {
		super(origin);
		this.origin = origin;
		this.direction = direction;
		this.l = l;
		this.color = color;
	}

	@Override
	protected Node createNode() {
		ModelBuilder modelBuilder = getModelBuilder();
		Node node = modelBuilder.node();
		{
			MeshPartBuilder builder = modelBuilder.part("tracer", GL20.GL_LINES, Usage.Position | Usage.ColorUnpacked,
					new Material());
			builder.setColor(color);
			Vec3 end = origin.add(direction.mult(Math.min(l, 100)));
			builder.line(origin.toGDXVector3(), end.toGDXVector3());
		}
		return node;
	}

	@Override
	protected void refreshNode() {
		// TODO Auto-generated method stub

	}

}
