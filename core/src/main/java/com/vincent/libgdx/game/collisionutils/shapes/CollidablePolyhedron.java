package com.vincent.libgdx.game.collisionutils.shapes;

import java.util.function.Function;

import com.vincent.libgdx.game.collisionutils.Collidable;
import com.vincent.libgdx.game.collisionutils.features.CollidableEdge;
import com.vincent.libgdx.game.collisionutils.features.CollidableFace;
import com.vincent.libgdx.game.collisionutils.features.CollidableVertex;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;

public class CollidablePolyhedron extends BasePolyhedron<CollidableVertex, CollidableEdge, CollidableFace>
		implements Collidable {
	private Vec3 boundingMinOffset = new Vec3(), boundingMaxOffset = new Vec3();

	private int[][] vertexNeighborIndices;

//	private CollidableEdge[] edges;
	private int[][] edgeNeighborIndices;

	private int[][] faceEdgeIndices;

	public CollidablePolyhedron(double[][] coordinates, Vec3 position, CoordinateCenter center,
			int[][] vertexNeighborIndices, int[][] edgeVertexIndices, int[][] edgeNeighborIndices,
			int[][] faceEdgeIndices, ConvertFunction<UVPolyhedron> UVConvert,
			ConvertFunction<CollidablePolyhedron> collidableConvert, Function<BasePolyhedron, String> generator) {
		super(coordinates, position, center, edgeVertexIndices, UVConvert, collidableConvert, generator);
		this.vertexNeighborIndices = vertexNeighborIndices;
		this.edgeNeighborIndices = edgeNeighborIndices;
		this.faceEdgeIndices = faceEdgeIndices;

		createFeatures(coordinates, center);
		setUpNeighbors();
		generateRegions();
//		calculateBoundingInfo();
	}

	@Override
	protected void createFeatures(double[][] coordinates, CoordinateCenter center) {
		CollidableVertex[] vertices = new CollidableVertex[coordinates.length];
		int[][] edgeVertexIndices = getEdgeVertexIndices();
		CollidableEdge[] edges = new CollidableEdge[edgeVertexIndices.length];
		CollidableFace[] faces = new CollidableFace[faceEdgeIndices.length];
		for (int i = 0; i < vertices.length; i++)
			vertices[i] = new CollidableVertex(this, i, new Vec3(coordinates[i]).sub(getOffset()));
		for (int i = 0; i < edges.length; i++)
			edges[i] = new CollidableEdge(this, i, vertices[edgeVertexIndices[i][0]],
					vertices[edgeVertexIndices[i][1]]);
		for (int i = 0; i < faces.length; i++) {
			CollidableEdge[] faceEdges = new CollidableEdge[faceEdgeIndices[i].length];
			for (int j = 0; j < faceEdges.length; j++)
				faceEdges[j] = edges[faceEdgeIndices[i][j]];
			faces[i] = new CollidableFace(this, i, faceEdges);
		}
		initializeVertices(vertices);
		initializeEdges(edges);
		initializeFaces(faces);
	}

	private void setUpNeighbors() {
		CollidableVertex[] vertices = getVertices();
		CollidableEdge[] edges = getEdges();
		CollidableFace[] faces = getFaces();
		for (int i = 0; i < vertexNeighborIndices.length; i++) {
			CollidableEdge[] vertexNeighbors = new CollidableEdge[vertexNeighborIndices[i].length];
			for (int j = 0; j < vertexNeighbors.length; j++)
				vertexNeighbors[j] = edges[vertexNeighborIndices[i][j]];
			vertices[i].setNeighbors(vertexNeighbors);
		}
		for (int i = 0; i < edgeNeighborIndices.length; i++)
			edges[i].setNeighbors(faces[edgeNeighborIndices[i][0]], faces[edgeNeighborIndices[i][1]]);
		for (CollidableFace f : faces)
			f.initializeNormal();
	}

	private void generateRegions() {
		for (CollidableVertex v : getVertices())
			v.generateRegion();
		for (CollidableEdge e : getEdges())
			e.generateRegion();
		for (CollidableFace f : getFaces())
			f.generateRegion();
	}

	private void calculateBoundingInfo() {
		double minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY, minZ = Float.POSITIVE_INFINITY;
		double maxX = Float.NEGATIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;
		for (CollidableVertex v : getVertices()) {
			minX = Math.min(minX, v.x());
			maxX = Math.max(maxX, v.x());
			minY = Math.min(minY, v.y());
			maxY = Math.max(maxY, v.y());
			minZ = Math.min(minZ, v.z());
			maxZ = Math.max(maxZ, v.z());
		}
		boundingMinOffset.set(minX, minY, minZ);
		boundingMaxOffset.set(maxX, maxY, maxZ);
	}

	@Override
	public CollidablePolyhedron getCollider() {
		return this;
	}

	@Override
	public CollidablePolyhedron copy() {
		return new CollidablePolyhedron(getCoordinates(), getLocalPosition(), getCenter(), vertexNeighborIndices,
				getEdgeVertexIndices(), edgeNeighborIndices, faceEdgeIndices, getUVConvert(), getCollidableConvert(),
				getGenerator());
	}
}
