package com.vincent.libgdx.game.collisionutils.features;

import com.vincent.libgdx.game.collisionutils.vclip.VClip.DPrimeState;

public interface DifferentiableFeature {
	public DPrimeState signDPrime(CollidableEdge e, double l);
}
