package com.test.testgame;

import org.andengine.entity.modifier.CardinalSplineMoveModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.CardinalSplineMoveModifier.CardinalSplineMoveModifierConfig;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
/**
 * 
 * @author Chris
 *
 */
public class Character extends GameObject {
	
	//TODO: Character shouldn't move out of screen.
	PathModifier pathmodifier;
	Path movementPath;
	
	MoveModifier movemodifier;
	
	CardinalSplineMoveModifier cardinalModifier;
	CardinalSplineMoveModifierConfig movementConfig;
	
	boolean selected = false;
	boolean selectable = true;
	// 0 has no target.
	int target = 0;
	boolean isTargeted = false;
	int team = 0;
	
	boolean dead = false;
	
	int hitpoints = 0;
	
	
	public Character(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		// TODO Auto-generated constructor stub
	}
	
	public Character(final float pX, final float pY, 
			final ITiledTextureRegion pTiledTextureRegion, 
			final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		super(pX, pY, pTiledTextureRegion, pTiledSpriteVertexBufferObject);
	}

	public void setPathModifier(PathModifier path)
	{
		pathmodifier = path;
		this.registerEntityModifier(pathmodifier);
	}
	
	public void setCardinalModifier(CardinalSplineMoveModifier cardial)
	{
		cardinalModifier = cardial;
		this.registerEntityModifier(cardial);
	}
	
	public void setPath(Path path)
	{
		movementPath = path;
	}
	
	public void setMoveModifier(MoveModifier move)
	{
		movemodifier = move;
	}
	
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
	{
		if (pSceneTouchEvent.isActionDown() && selectable)
		{
			selected = true;
			return true;
			//movementPath = null;
		}
		else if (pSceneTouchEvent.isActionUp() && !selectable)
		{
			isTargeted = true;
			return true;
		}
		
		return false;
		
	}
	
	// targetTag - tagid of the target Entity.
	public void setTarget(int targetTag)
	{
		target = targetTag;
	}
	
	public int getTarget()
	{
		return target;
	}
	
	public int getHitpoints() {
		return hitpoints;
	}

	public void setHitpoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	

}
