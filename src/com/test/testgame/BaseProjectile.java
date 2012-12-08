package com.test.testgame;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public interface BaseProjectile {
	
	public void moveProjectile (MoveModifier mv);
	
	public void fireProjectile (float sourceX, float sourceY, float targetX, float targetY);
	
	public void destroyProjectile();

}
