package com.vincent.libgdx.game.collisionutils.shapes;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.BasePolyhedron.CoordinateCenter;

public class BoxFactory {
	/*
	 * @formatter:off
	 *         4---a--→5
	 *         ↑       |
	 *         7   4   6
	 *         |       ↓
	 * 4←--7---3←--2---2←--6---5←--a---4
	 * ↑       |       ↑       |       ↑
	 * 9   3   3   0   1   1   b   2   9
	 * |       ↓       |       ↓       |
	 * 7---4--→0---0--→1---5--→6---8--→7
	 *         ↑       |
	 *         4   5   5
	 *         |       ↓
	 *         7←--8---6
	 * 
	 * @formatter:on
	 */

	private static int[][] edgeVertexIndices = { { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 0 }, { 7, 0 }, { 1, 6 }, { 5, 2 },
			{ 3, 4 }, { 6, 7 }, { 7, 4 }, { 4, 5 }, { 5, 6 } };

	private static int[][] faceVertexIndices = { { 0, 1, 2, 3 }, { 1, 6, 5, 2 }, { 6, 7, 4, 5 }, { 7, 0, 3, 4 },
			{ 2, 5, 4, 3 }, { 6, 1, 0, 7 } };
	private static float[][] triUV0 = { { 0, 0 }, { 1, 0 }, { 1, 1 } };
	private static float[][] triUV1 = { { 0, 0 }, { 1, 1 }, { 0, 1 } };
	private static float[][][] faceUV = { triUV0, triUV1 };
	private static float[][][][] faceUVs = { faceUV, faceUV, faceUV, faceUV, faceUV, faceUV };

	public static UVPolyhedron sampleUVBox(double size) {
		return UVBox(0, 0, 0, size, size, size, Vec3.ZERO, CoordinateCenter.atCentroid());
	}

	public static UVPolyhedron UVBox(double x, double y, double z, double depth, double height, double width,
			Vec3 localPosition, CoordinateCenter center) {
		return UVBox(new double[][] { { x, y, z }, { x, y, z + width }, { x, y + height, z + width },
				{ x, y + height, z }, { x + depth, y + height, z }, { x + depth, y + height, z + width },
				{ x + depth, y, z + width }, { x + depth, y, z } }, localPosition, center);
	}

	public static UVPolyhedron UVBox(double[][] coordinates, Vec3 localPosition, CoordinateCenter center) {
		return new UVPolyhedron(coordinates, localPosition, center, edgeVertexIndices, faceVertexIndices, faceUVs,
				BoxFactory::UVBox, BoxFactory::collidableBox, BoxFactory::UVBoxGenerator);
	}

	private static String UVBoxGenerator(BasePolyhedron self) {
		return String.format("BoxFactory.UVBox(new double[][] %s, %s, %s)", self.generateCoordinates(),
				self.getLocalPosition().generate(), self.getCenter().generate());
	}

	private static int[][] vertexNeighbors = { { 0, 3, 4 }, { 0, 1, 5 }, { 1, 2, 6 }, { 2, 3, 7 }, { 7, 9, 10 },
			{ 6, 10, 11 }, { 5, 8, 11 }, { 4, 8, 9 } };
	private static int[][] edgeNeighborIndices = { { 0, 5 }, { 0, 1 }, { 0, 4 }, { 0, 3 }, { 3, 5 }, { 1, 5 }, { 1, 4 },
			{ 3, 4 }, { 2, 5 }, { 2, 3 }, { 2, 4 }, { 2, 1 } };
	private static int[][] faceEdgeIndices = { { 0, 1, 2, 3 }, { 5, 11, 6, 1 }, { 8, 9, 10, 11 }, { 4, 3, 7, 9 },
			{ 6, 10, 7, 2 }, { 5, 0, 4, 8 } };

	public static CollidablePolyhedron sampleCollidableBox(double size) {
		return collidableBox(0, 0, 0, size, size, size, Vec3.ZERO, CoordinateCenter.atCentroid());
	}

	public static CollidablePolyhedron collidableBox(double x, double y, double z, double depth, double height,
			double width, Vec3 localPosition, CoordinateCenter center) {
		return collidableBox(new double[][] { { x, y, z }, { x, y, z + width }, { x, y + height, z + width },
				{ x, y + height, z }, { x + depth, y + height, z }, { x + depth, y + height, z + width },
				{ x + depth, y, z + width }, { x + depth, y, z } }, localPosition, center);
	}

	public static CollidablePolyhedron collidableBox(double[][] coordinates, Vec3 localPosition,
			CoordinateCenter center) {
		return new CollidablePolyhedron(coordinates, localPosition, center, vertexNeighbors, edgeVertexIndices,
				edgeNeighborIndices, faceEdgeIndices, BoxFactory::UVBox, BoxFactory::collidableBox,
				BoxFactory::collidableBoxGenerator);
	}

	private static String collidableBoxGenerator(BasePolyhedron self) {
		return String.format("BoxFactory.collidableBox(new double[][] %s, %s, %s)", self.generateCoordinates(),
				self.getLocalPosition().generate(), self.getCenter().generate());
	}
}
