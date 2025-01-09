package com.vincent.libgdx.game.collisionutils.shapes;

import java.util.Arrays;
import java.util.function.Function;

import com.vincent.libgdx.game.collisionutils.Generatable;
import com.vincent.libgdx.game.collisionutils.Movable;
import com.vincent.libgdx.game.collisionutils.features.BaseEdge;
import com.vincent.libgdx.game.collisionutils.features.BaseFace;
import com.vincent.libgdx.game.collisionutils.features.BaseVertex;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.world.utils.Nestable;

public abstract class BasePolyhedron<V extends BaseVertex<?>, E extends BaseEdge<?, ?>, F extends BaseFace<?>>
		implements Movable, Generatable {
	private static int count;
	private int ID;

	private Nestable parent;

	private Vec3 localPosition = new Vec3();
	private CoordinateCenter center;

	private double[][] coordinates;
	private V[] vertices;
	private E[] edges;
	private int[][] edgeVertexIndices;
	private F[] faces;

	@FunctionalInterface
	public static interface ConvertFunction<T extends BasePolyhedron<?, ?, ?>> {
		public T apply(double[][] coordinates, Vec3 localPosition, CoordinateCenter center);
	}

	private ConvertFunction<UVPolyhedron> UVConvert;

	protected ConvertFunction<UVPolyhedron> getUVConvert() {
		return UVConvert;
	}

	public UVPolyhedron toUV() {
		return UVConvert.apply(coordinates, localPosition, center);
	}

	private ConvertFunction<CollidablePolyhedron> collidableConvert;

	protected ConvertFunction<CollidablePolyhedron> getCollidableConvert() {
		return collidableConvert;
	}

	public CollidablePolyhedron toCollidable() {
		return collidableConvert.apply(coordinates, localPosition, center);
	}

	private Function<BasePolyhedron, String> generator;

	protected Function<BasePolyhedron, String> getGenerator() {
		return generator;
	}

	public static class CoordinateCenter implements Generatable {
		public static enum Mode {
			AT_CENTROID, AT_FOOT, AT_DEFINED;
		}

		private Mode mode;
		private Vec3 position;

		public CoordinateCenter(Mode mode, Vec3 position) {
			this.mode = mode;
			this.position = position;
		}

		public static CoordinateCenter atCentroid() {
			return new CoordinateCenter(Mode.AT_CENTROID, null);
		}

		public static CoordinateCenter atFoot() {
			return new CoordinateCenter(Mode.AT_FOOT, null);
		}

		public static CoordinateCenter atDefined(Vec3 position) {
			return new CoordinateCenter(Mode.AT_DEFINED, position);
		}

		@Override
		public String generate() {
			return String.format("new CoordinateCenter(CoordinateCenter.Mode.%s, %s)", mode,
					position == null ? null : position.generate());
		}
	}

	protected BasePolyhedron(double[][] coordinates, Vec3 position, CoordinateCenter center, int[][] edgeVertexIndices,
			ConvertFunction<UVPolyhedron> UVConvert, ConvertFunction<CollidablePolyhedron> collidableConvert,
			Function<BasePolyhedron, String> generator) {
		this.coordinates = coordinates;
		this.localPosition.set(position);
		this.center = center;
		this.edgeVertexIndices = edgeVertexIndices;
		this.UVConvert = UVConvert;
		this.collidableConvert = collidableConvert;
		this.generator = generator;

		ID = count++;
	}

	public abstract BasePolyhedron<V, E, F> copy();

	protected void initializeVertices(V[] vertices) {
		this.vertices = vertices;
	}

	protected void initializeEdges(E[] edges) {
		this.edges = edges;
	}

	protected void initializeFaces(F[] faces) {
		this.faces = faces;
	}

	protected abstract void createFeatures(double[][] coordinates, CoordinateCenter center);

	protected Vec3 getOffset() {
		Vec3 centroid = new Vec3(), sum = new Vec3();
		for (double[] v : coordinates)
			sum.addEquals(v[0], v[1], v[2]);
		centroid.set(sum.div(coordinates.length));

		return switch (center.mode) {
		case AT_CENTROID -> centroid;
		case AT_FOOT -> new Vec3(centroid.x(), 0, centroid.z());
		case AT_DEFINED -> center.position;
		};
	}

	public V sampleVertex() {
		return vertices[0];
	}

	protected double[][] getCoordinates() {
		return coordinates;
	}

	public V[] getVertices() {
		return vertices;
	}

	public E[] getEdges() {
		return edges;
	}

	public F[] getFaces() {
		return faces;
	}

	protected int[][] getEdgeVertexIndices() {
		return edgeVertexIndices;
	}

	public int getID() {
		return ID;
	}

	public void setParent(Nestable n) {
		parent = n;
	}

	public Nestable getParent() {
		return parent;
	}

	public CoordinateCenter getCenter() {
		return center;
	}

	@Override
	public Vec3 getGlobalPosition() {
		return parent == null ? localPosition : localPosition.add(parent.getGlobalPosition());
	}

	@Override
	public Vec3 getLocalPosition() {
		return localPosition;
	}

	protected String generateCoordinates() {
		String output = "{";
		for (double[] v : coordinates) {
			output += String.format("{%f,%f,%f},", v[0], v[1], v[2]);
		}
		output += "}";
		return output;
	}

	public void debugCoordinates() {
		System.out.println(Arrays.deepToString(coordinates));
	}

	@Override
	public final String generate() {
		return generator.apply(this);
	}
}
