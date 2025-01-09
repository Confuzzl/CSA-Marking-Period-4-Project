package com.vincent.libgdx.game.world.utils.rparts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.ArrowShapeBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.vincent.libgdx.game.collisionutils.features.BaseEdge;
import com.vincent.libgdx.game.collisionutils.features.BaseFace;
import com.vincent.libgdx.game.collisionutils.features.BaseVertex;
import com.vincent.libgdx.game.collisionutils.features.Feature;
import com.vincent.libgdx.game.collisionutils.features.UVEdge;
import com.vincent.libgdx.game.collisionutils.features.UVFace;
import com.vincent.libgdx.game.collisionutils.features.UVVertex;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.primitives.VectorLike;
import com.vincent.libgdx.game.collisionutils.shapes.UVPolyhedron;

public class FeaturePart extends PolyRPart {
//	private Array<NodePart> vertices = new Array<NodePart>();
//	private Array<NodePart> edges = new Array<NodePart>();
//	private Array<NodePart> faces = new Array<NodePart>();
	private Node vertices;
	private Node edges;
	private Node faces;

	public Feature n;

	public FeaturePart(Vec3 position, UVPolyhedron polyhedron) {
		super(position, polyhedron);
	}

	@Override
	public void init() {
		super.init();
//		highlight(null);
		refreshNode();
	}

	public void highlight(Feature n) {
		this.n = n;
		refreshNode();
		getParent().refreshInstance();
	}

	public void highlightVertex(int i) {
		highlight(getPolyhedron().getVertices()[i]);
	}

	public void highlightEdge(int i) {
		highlight(getPolyhedron().getEdges()[i]);
	}

	public void highlightFace(int i) {
		highlight(getPolyhedron().getFaces()[i]);
	}

	@Override
	protected Node createNode() {
		ModelBuilder modelBuilder = getModelBuilder();
		Node parent = modelBuilder.node();
		{
			vertices = modelBuilder.node();
			vertices.id = "vertices";
			createVertices(Color.RED);
			vertices.attachTo(parent);
		}
		{
			edges = modelBuilder.node();
			edges.id = "edges";
			createEdges(Color.BLUE);
			edges.attachTo(parent);
		}
		{
			faces = modelBuilder.node();
			faces.id = "faces";
			createFaces(Color.GREEN);
			faces.attachTo(parent);
		}
		return parent;
	}

	private void createVertices(Color color) {
		UVVertex[] vertices = getPolyhedron().getVertices();
		for (int i = 0; i < vertices.length; i++) {
			UVVertex v = vertices[i];
			MeshPartBuilder builder = getModelBuilder().part("vertex " + i, GL20.GL_TRIANGLES,
					Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(color)));
			createPoint(builder, v, color);
		}
	}

	private void createPoint(MeshPartBuilder builder, VectorLike v, Color color) {
		BoxShapeBuilder.build(builder, (float) (v.x()), (float) (v.y()), (float) (v.z()), 0.25f, 0.25f, 0.25f);
	}

	private void createEdges(Color color) {
		UVEdge[] edges = getPolyhedron().getEdges();
		for (int i = 0; i < edges.length; i++) {
			UVEdge e = edges[i];
			UVVertex v0 = e.getTail();
			UVVertex v1 = e.getHead();
			MeshPartBuilder builder = getModelBuilder().part("edge " + i, GL20.GL_TRIANGLES,
					Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(color)));
			createArrow(builder, v0, v1, color);
		}
	}

	private void createArrow(MeshPartBuilder builder, VectorLike v0, VectorLike v1, Color color) {
		ArrowShapeBuilder.build(builder, (float) v0.x(), (float) v0.y(), (float) v0.z(), (float) v1.x(), (float) v1.y(),
				(float) v1.z(), 0.1f, 0.1f, 3);
	}

	private void createFaces(Color color) {
		UVFace[] faces = getPolyhedron().getFaces();
		for (int i = 0; i < faces.length; i++) {
			UVFace f = faces[i];
			Vec3 centroid = f.getCentroid();
			Vec3 endNormal = centroid.add(f.getNormal().mult(5));
			MeshPartBuilder builder = getModelBuilder().part("face " + i, GL20.GL_TRIANGLES,
					Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(color)));
			createPoint(builder, centroid, color);
			createArrow(builder, centroid, endNormal, color);
		}
	}

	@Override
	protected void refreshNode() {
		nodeToggle(getNode(), false, true);
		if (n == null)
			return;
		Node node = null;
		if (n instanceof BaseVertex)
			node = vertices;
		else if (n instanceof BaseEdge)
			node = edges;
		else if (n instanceof BaseFace)
			node = faces;
		for (int i = 0; i < node.parts.size; i++)
			node.parts.get(i).enabled = i == n.getID();
//		getParent().refreshInstance();
	}
}
