package com.test.testgame;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class ArrowProjectile extends GameObject {

	public boolean exists = false;
	
	MoveModifier movement = null;
	
	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	public ArrowProjectile(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		// TODO Auto-generated constructor stub
	}

	public void moveProjectile (MoveModifier mv)
	{
		movement = mv;
		//this.registerEntityModifier(movement);
	}
	
	public void fireProjectile (float sourceX, float sourceY, float targetX, float targetY)
	{
		
	}
	
	
	
	public void destroyProjectile()
	{
		
	}

	public MoveModifier getMovement() {
		return movement;
	}

	public void setMovement(MoveModifier movement) {
		this.movement = movement;
	}

}
