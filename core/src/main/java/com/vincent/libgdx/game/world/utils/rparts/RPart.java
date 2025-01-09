package com.vincent.libgdx.game.world.utils.rparts;

import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.vincent.libgdx.game.collisionutils.Movable;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.world.utils.Nestable;

public abstract class RPart implements Movable/* , Rotatable */ {
	private Nestable parent;
//	private Quat rotation = new Quat();

	private Vec3 localPosition = new Vec3();
	private Node node;
	private boolean render = true;

	public RPart(Vec3 position) {
	}

	public void init() {
		node = createNode();
	}

	protected abstract Node createNode();

	protected abstract void refreshNode();

	protected void setNode(Node node) {
		this.node = node;
	}

	public Node getNode() {
		return node;
	}

	protected ModelBuilder getModelBuilder() {
		return parent.getModelBuilder();
	}

	public void toggle(boolean b) {
		nodeToggle(node, b, true);
	}

	public static void nodeToggle(Node node, boolean enable, boolean recursive) {
		for (NodePart nodePart : node.parts)
			nodePart.enabled = enable;
		if (recursive)
			for (Node n : node.getChildren())
				nodeToggle(n, enable, recursive);
	}

	public Nestable getParent() {
		return parent;
	}

	public void setParent(Nestable parent) {
		this.parent = parent;
	}

	public void setRender(boolean b) {
		render = b;
	}

	public boolean getRender() {
		return render;
	}

	@Override
	public Vec3 getLocalPosition() {
		return localPosition;
	}

	@Override
	public Vec3 getGlobalPosition() {
		return localPosition.add(parent.getGlobalPosition());
	}

	@Override
	public final void setLocalPosition(Vec3 v) {
		Movable.super.setLocalPosition(v);
		node.translation.set((float) v.x(), (float) v.y(), (float) v.z());
		node.calculateTransforms(true);
	}

//	@Override
//	public Quat getQuat() {
//		return rotation;
//	}
//
//	@Override
//	public void setRotation(Vec3 r) {
//		getQuat().setRotation(r);
//		Vector3 trans = modelInstance.transform.getTranslation(new Vector3());
//		modelInstance.transform.set(getQuat().toGDXQuaternion());
//		modelInstance.transform.setTranslation(trans);
//	}
}
