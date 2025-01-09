package com.vincent.libgdx.game.world.utils.rparts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.vincent.libgdx.game.collisionutils.features.UVEdge;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.UVPolyhedron;

public class EdgePart extends PolyRPart {
	private Color color;
	private String name;

	public EdgePart(Vec3 position, UVPolyhedron polyhedron, Color color, String name) {
		super(position, polyhedron);
		this.color = color;
		this.name = name;
	}

//	@Override
//	protected Model createModel() {
//		ModelBuilder modelBuilder = getModelBuilder();
//		modelBuilder.begin();
//		{
//			Node node = modelBuilder.node();
//			node.id = "wireframe";
//
//			Edge[] edges = getPolyhedron().getEdges();
//			for (int i = 0; i < edges.length; i++) {
//				Edge edge = edges[i];
//				MeshPartBuilder builder = modelBuilder.part("edge " + i, GL20.GL_LINES,
//						Usage.Position | Usage.ColorUnpacked, new Material());
//				builder.setColor(color);
//				Vec3 centroid = edge.getOwner().getGlobalPosition();
//				builder.line(edge.getTail().sub(centroid).toGDXVector3(), edge.getHead().sub(centroid).toGDXVector3());
//			}
//		}
//		return modelBuilder.end();
//	}

	@Override
	public Node createNode() {
		ModelBuilder modelBuilder = getModelBuilder();
		Node node = modelBuilder.node();
		node.id = name;
		UVEdge[] edges = getPolyhedron().getEdges();
		for (int i = 0; i < edges.length; i++) {
			UVEdge edge = edges[i];
			MeshPartBuilder builder = modelBuilder.part("edge " + i, GL20.GL_LINES,
					Usage.Position | Usage.ColorUnpacked, new Material());
			builder.setColor(color);
//			Vec3 centroid = edge.getOwner().getLocalPosition();
			builder.line(edge.getTail().toGDXVector3(), edge.getHead().toGDXVector3());
		}
		return node;
	}

	@Override
	protected void refreshNode() {
	}
}
