package com.vincent.libgdx.game.world.utils.rparts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.vincent.libgdx.game.collisionutils.primitives.Ray;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;

public class RayPart extends RPart {
	private Ray ray;
	private double l;
	private Color color;

	public RayPart(Ray ray, double l, Color color) {
		super(ray.getOrigin());
		this.ray = ray;
		this.l = l;
		this.color = color;
	}

	@Override
	public Node createNode() {
		ModelBuilder modelBuilder = getModelBuilder();
		Node node = modelBuilder.node();
		{
			MeshPartBuilder builder;
			Vec3 start = ray.getOrigin();
			Vec3 end = ray.evalAt(l);

			builder = modelBuilder.part("ray", GL20.GL_LINES, Usage.Position,
					new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)));
			builder.line((float) start.x(), (float) start.y(), (float) start.z(), (float) end.x(), (float) end.y(),
					(float) end.z());

			builder = modelBuilder.part("points", GL20.GL_TRIANGLES, Usage.Position | Usage.ColorUnpacked,
					new Material());
			builder.setColor(color);
			BoxShapeBuilder.build(builder, (float) start.x(), (float) start.y(), (float) start.z(), 0.25f, 0.25f,
					0.25f);
			builder.setColor(color);
			BoxShapeBuilder.build(builder, (float) end.x(), (float) end.y(), (float) end.z(), 0.25f, 0.25f, 0.25f);
		}
		return node;
	}

	@Override
	protected void refreshNode() {
	}
}
