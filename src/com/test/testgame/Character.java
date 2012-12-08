package com.test.testgame;

import java.util.ArrayList;
import java.util.Arrays;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.CardinalSplineMoveModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.CardinalSplineMoveModifier.CardinalSplineMoveModifierConfig;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;
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
	
	float centerX;
	float centerY;
	
	float range;
	
	float rateOfFire;
	
	ArrayList<Integer> aggressors = new ArrayList<Integer>();
	
	//0 = idle;
	//1 = moving (non-aggressive)
	//2 = moving (to attacking)
	//3 = stopped (attacking)
	int state = 0;
	
	
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
	
	public PathModifier getPathmodifier() {
		return pathmodifier;
	}

	public void setPathAndDuration(Path myPath, float duration)
	{
		    
			final PathModifier pModifier = new PathModifier(duration, myPath);
			final float[] xCoordinate = pModifier.getPath().getCoordinatesX();
			final float[] yCoordinate = pModifier.getPath().getCoordinatesY();
			pModifier.setPathModifierListener(new IPathModifierListener() {

				@Override
				public void onPathStarted(PathModifier pPathModifier,
						IEntity pEntity) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onPathWaypointStarted(PathModifier pPathModifier,
						IEntity pEntity, int pWaypointIndex) {
					// TODO Auto-generated method stub
					
					//calcuate arctangent between enemy pos and target pos in order to determine which way the enemy should be rotated.
					//float arctangent = MathUtils.atan2(enemies.get(i).getY() - mScene.getChildByTag(enemies.get(i).getTarget()).getY(), enemies.get(i).getX() - mScene.getChildByTag(enemies.get(i).getTarget()).getX());
					
					if (pWaypointIndex + 1 < pModifier.getPath().getSize())
					{
						float arctangent = MathUtils.atan2(yCoordinate[pWaypointIndex] - yCoordinate[pWaypointIndex + 1], xCoordinate[pWaypointIndex] - xCoordinate[pWaypointIndex + 1]);
						arctangent = Math.abs(arctangent);
						
						if (arctangent > 0.7853981633974483 && arctangent < 2.356194490192345)
						{
							stopAnimation();
							final long[] pFrameDurations = new long[3];
					        Arrays.fill(pFrameDurations, 200);
							animate(pFrameDurations, 0, 2, true);
						}
						else if (arctangent > 2.356194490192345 && arctangent < 3.9269908169872414)
						{
							stopAnimation();
							final long[] pFrameDurations = new long[3];
					        Arrays.fill(pFrameDurations, 200);
							animate(pFrameDurations, 3, 5, true);
						}
						else if (arctangent > 3.9269908169872414 && arctangent < 5.497787143782138)
						{
							stopAnimation();
							final long[] pFrameDurations = new long[3];
					        Arrays.fill(pFrameDurations, 200);
							animate(pFrameDurations, 6, 8, true);
						}
						else
						{
							stopAnimation();
							final long[] pFrameDurations = new long[3];
					        Arrays.fill(pFrameDurations, 200);
							animate(pFrameDurations, 9, 11, true);
						}
					}
				}

				@Override
				public void onPathWaypointFinished(PathModifier pPathModifier,
						IEntity pEntity, int pWaypointIndex) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onPathFinished(PathModifier pPathModifier,
						IEntity pEntity) {
					// TODO Auto-generated method stub
					
				}
        });
	    
		pathmodifier = pModifier;
			
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
		//this.registerEntityModifier(move);
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
	
	//AI should know who's attacking it.
	public void addAggressors(int attackerTag)
	{
		aggressors.add(attackerTag);
	}
	
	public void removeAggressor(int attackerTag)
	{
		//aggressors.
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
	
	public float getCenterX()
	{
		centerX = this.getX() + this.getWidth() / 2;
		return centerX;
	}
	
	public float getCenterY()
	{
		centerY = this.getY() + this.getHeight() / 2;
		return centerY;
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public float getRateOfFire() {
		return rateOfFire;
	}

	public void setRateOfFire(float rateOfFire) {
		this.rateOfFire = rateOfFire;
	}
	
	

	

}
