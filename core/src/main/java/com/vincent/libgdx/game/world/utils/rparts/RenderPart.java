package com.vincent.libgdx.game.world.utils.rparts;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.vincent.libgdx.game.Assets;
import com.vincent.libgdx.game.collisionutils.features.UVFace;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.UVPolyhedron;
import com.vincent.libgdx.game.world.utils.Tri;

public class RenderPart extends PolyRPart {
	private Material[] materials;
	private static final Material fallback = new Material(TextureAttribute.createDiffuse(Assets.getTexture("default")));;

	public static enum RenderMode {
		NORMAL, WIREFRAME;
	}

	private RenderMode mode;

//	private Node shaded;
//	private Node unshaded;
	private Node normal;
	private Node wireframe;

	public RenderPart(Vec3 position, UVPolyhedron render, RenderMode mode, Material... materials) {
		super(position, render);
		this.materials = materials;
		this.mode = mode;
	}

	@Override
	public void init() {
		super.init();
//		setRenderMode(mode);
		refreshNode();
	}

	public void setRenderMode(RenderMode m) {
		mode = m;
		refreshNode();
		getParent().refreshInstance();
	}

	@Override
	public Node createNode() {
		ModelBuilder modelBuilder = getModelBuilder();
		Node parent = modelBuilder.node();
		parent.id = "render";
		{
			normal = modelBuilder.node();
			normal.id = "shaded";
			createFaceBasedModels(modelBuilder, GL20.GL_TRIANGLES);
			normal.attachTo(parent);
		}
		{
			wireframe = modelBuilder.node();
			wireframe.id = "wireframe";
			createFaceBasedModels(modelBuilder, GL20.GL_LINES);
			wireframe.attachTo(parent);
		}
		return parent;
	}

	private void createFaceBasedModels(ModelBuilder modelBuilder, int primitive) {
		UVFace[] faces = getPolyhedron().getFaces();
		for (int i = 0; i < faces.length; i++) {
			MeshPartBuilder builder = modelBuilder.part("face " + i, primitive,
					Usage.Position | Usage.TextureCoordinates | Usage.Normal, materialAt(i));
			for (Tri tri : faces[i].getTris())
				builder.triangle(tri.getI0(), tri.getI1(), tri.getI2());
		}
	}

	private Material materialAt(int i) {
		if (i < materials.length)
			return materials[i];
		return fallback;
	}

	@Override
	protected void refreshNode() {
		nodeToggle(normal, mode == RenderMode.NORMAL, false);
		nodeToggle(wireframe, mode == RenderMode.WIREFRAME, false);
//		getParent().refreshInstance();
	}
}
