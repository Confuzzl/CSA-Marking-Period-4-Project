package com.vincent.libgdx.game.collisionutils.shapes;

import java.util.function.Function;

import com.vincent.libgdx.game.collisionutils.features.UVEdge;
import com.vincent.libgdx.game.collisionutils.features.UVFace;
import com.vincent.libgdx.game.collisionutils.features.UVVertex;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;

public class UVPolyhedron extends BasePolyhedron<UVVertex, UVEdge, UVFace> {
	private int[][] faceVertexIndices;
	private float[][][][] faceUVs;

	public UVPolyhedron(double[][] coordinates, Vec3 position, CoordinateCenter center, int[][] edgeVertexIndices,
			int[][] faceVertexIndices, float[][][][] faceUVs, ConvertFunction<UVPolyhedron> UVConvert,
			ConvertFunction<CollidablePolyhedron> collidableConvert, Function<BasePolyhedron, String> generator) {
		super(coordinates, position, center, edgeVertexIndices, UVConvert, collidableConvert, generator);
		this.faceVertexIndices = faceVertexIndices;
		this.faceUVs = faceUVs;

		createFeatures(coordinates, center);
	}

	@Override
	public UVPolyhedron copy() {
		return new UVPolyhedron(getCoordinates(), getLocalPosition(), getCenter(), getEdgeVertexIndices(),
				faceVertexIndices, faceUVs, getUVConvert(), getCollidableConvert(), getGenerator());
	}

	@Override
	protected void createFeatures(double[][] coordinates, CoordinateCenter center) {
		UVVertex[] vertices = new UVVertex[coordinates.length];
		int[][] edgeVertexIndices = getEdgeVertexIndices();
		UVEdge[] edges = new UVEdge[edgeVertexIndices.length];
		UVFace[] faces = new UVFace[faceVertexIndices.length];
		for (int i = 0; i < vertices.length; i++)
			vertices[i] = new UVVertex(this, i, new Vec3(coordinates[i]).sub(getOffset()));
		for (int i = 0; i < edges.length; i++)
			edges[i] = new UVEdge(this, i, vertices[edgeVertexIndices[i][0]], vertices[edgeVertexIndices[i][1]]);
		for (int i = 0; i < faces.length; i++) {
			UVVertex[] faceVertices = new UVVertex[faceVertexIndices[i].length];
			for (int j = 0; j < faceVertices.length; j++)
				faceVertices[j] = vertices[faceVertexIndices[i][j]];
			faces[i] = new UVFace(this, i, faceVertices, faceUVs[i]);
			faces[i].calculateRenderingData();
		}
		initializeVertices(vertices);
		initializeEdges(edges);
		initializeFaces(faces);
	}
}
