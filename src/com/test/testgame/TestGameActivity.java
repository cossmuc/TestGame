package com.test.testgame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.CardinalSplineMoveModifier;
import org.andengine.entity.modifier.CardinalSplineMoveModifier.CardinalSplineMoveModifierConfig;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.array.ArrayUtils;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

public class TestGameActivity extends BaseGameActivity implements IOnSceneTouchListener, IUpdateHandler {
    /** Called when the activity is first created. */
	
	Scene mScene;
	static final float mCameraWidth = 800;
	static final float mCameraHeight = 480;
	
	private Handler mHandler;
	
	 private BitmapTextureAtlas mBitmapTextureAtlas;
	 private TiledTextureRegion mPlayerTiledTextureRegion;
	 private BitmapTextureAtlas mBitmapTextureAtlas1;
	 private TiledTextureRegion mPlayerTiledTextureRegion1;
	 private TiledTextureRegion mPlayerTiledTextureRegion2;
	 private BitmapTextureAtlas mBitmapTextureAtlas2;
	 
	 MovementManager manager = new MovementManager();
	 Character character;
	 Character character1;
	 Character character2;
	 
	 ArrayList<Character> characters = new ArrayList<Character>();
	 
	 ArrayList<Float> xList = new ArrayList<Float>();
	 ArrayList<Float> yList = new ArrayList<Float>();
	 ArrayList<Line> lineList = new ArrayList<Line>();
	 
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		mHandler = new Handler();
		Camera mCamera = new Camera(0, 0, mCameraWidth, mCameraHeight);
        final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(mCameraWidth, mCameraHeight), mCamera);
        //Supposed to fix the texture disappear issue ,but missing in GLES 2.
        //engineOptions.getRenderOptions().disableExtensionVertexBufferObjects();
		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		// TODO Auto-generated method stub
			mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32);
			//mBitmapTextureAtlas1 = new BitmapTextureAtlas(this.getTextureManager(), 64, 64);
			mBitmapTextureAtlas1 = new BitmapTextureAtlas(this.getTextureManager(), 512, 256);
			this.mPlayerTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0, 1, 1);
			mBitmapTextureAtlas.load();
			//this.mPlayerTiledTextureRegion1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas1, this, "face_box_animation.png", 0, 0, 2, 2);
			this.mPlayerTiledTextureRegion1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas1, this, "trot1_done.png", 0, 0, 5, 4);
			mBitmapTextureAtlas1.load();
			
			mBitmapTextureAtlas2 = new BitmapTextureAtlas(this.getTextureManager(), 32, 32);
			this.mPlayerTiledTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas2, this, "face_box.png", 0, 0, 1, 1);
			mBitmapTextureAtlas2.load();
			//mBitmapTextureAtlas1.load();
		 pOnCreateResourcesCallback.onCreateResourcesFinished();
		
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		// TODO Auto-generated method stub
		 mScene = new Scene();
		 mScene.setBackground(new Background(0,0,0));
		 //this.get
		  character = new Character(mCameraWidth / 2, mCameraHeight / 2, mPlayerTiledTextureRegion1, getVertexBufferObjectManager());
		  //character = new Character(mCameraWidth / 2, mCameraHeight / 2, mPlayerTiledTextureRegion1, this.);
		 character.setTag(1);
		 //character.setScale(1.5f);
		 //character.animate(10000);
//		 final long[] pFrameDurations = new long[17];
//         Arrays.fill(pFrameDurations, 250);
//		 character.animate(pFrameDurations, 0, 16, true);
		 mScene.attachChild(character);
		 mScene.registerTouchArea(character);
		 character.selectable = true;
		 character.setHitpoints(10);
		 character.setTeam(1);
		 characters.add(character);
		 
		 
		 
		 character1 = new Character(mCameraWidth / 4, mCameraHeight / 4, mPlayerTiledTextureRegion, getVertexBufferObjectManager());
		 character1.setTag(2);
		 character1.setColor(Color.RED);
		 character1.setScale(1.5f);
		 mScene.attachChild(character1);
		 mScene.registerTouchArea(character1);
		 character1.selectable = false;
		 character1.setHitpoints(100);
		 character1.setTeam(2);
		 characters.add(character1);
		 
		 character2 = new Character(mCameraWidth / 8, mCameraHeight / 8, mPlayerTiledTextureRegion2, getVertexBufferObjectManager());
		 character2.setTag(3);
		 character2.setColor(Color.GREEN);
		 character2.setScale(1.5f);
		 mScene.attachChild(character2);
		 mScene.registerTouchArea(character2);
		 character2.selectable = false;
		 character2.setHitpoints(100);
		 character2.setTeam(2);
		 characters.add(character2);
		 
		 
		 mScene.registerUpdateHandler(this);
		 mScene.setOnSceneTouchListener(this);
		 
		 pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}
	
	
	
	/*public void onLoadScene()
	{
		
	}*/

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub
		pOnPopulateSceneCallback.onPopulateSceneFinished();
		
		
		
	}
	
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene,
			TouchEvent pSceneTouchEvent) {
		
		// TODO The path should only be calculated for the sprite that is touched. 
		// TODO Path is currently not interruptable.
		// TODO If finger is dragged out of touchscreen path is messed up. Another condition may be needed for pSceneTouchEvent.isOutOfScreen
		// TODO Peformance issues already encountered possible blocking TouchEvent with all the calculations. A good reason to use historical touch info and updateHandler
		
	/*	for (int i = 0; i < characters.size(); i++)
		{
			
			if (characters.get(i).selected)
			{
				
				//PathModifiers must have at least two coordinates. This adds current position as the first.
				if (xList.size() == 0 && yList.size() == 0)
				{
					xList.add(characters.get(i).getX());
					yList.add(characters.get(i).getY());
					
					
				}
				
				//If the player has lifted thier finger add the coordinate arrays to the Path object and begin the movement.
				if (pSceneTouchEvent.isActionUp())
				{
					character.unregisterEntityModifier(character.pathmodifier);
					
					//We cannot convert directly from ArrayList to Float[] so we have to hold the value in a list.
					List<Float> xcoordinates = xList;
					List<Float> ycoordinates = yList;
					
					//ArrayUtils is a utility provided by the Appache Commons license.
					//We can convert from a List to a float[].
					Path myPath = new Path(ArrayUtils.toFloatArray(xcoordinates), ArrayUtils.toFloatArray(ycoordinates));
					
					
					
					//float intDistance = calculateDistance(myPath);
					
					float[] totalDistanceX = ArrayUtils.toFloatArray(xcoordinates);
					float[] totalDistanceY = ArrayUtils.toFloatArray(ycoordinates);
					
					//float distanceX = calculateDistance(totalDistanceX);
					//float distanceY = calculateDistance(totalDistanceY);
					
					float duration = myPath.getLength() / 50;
					
					// TODO 10 second time. Sprite will move faster over longer distances. Need to calculate a time so that speed is constant. 
					PathModifier path = new PathModifier(duration, myPath);
					//Character handles PathModifier and registers it. registerEntityModifer begins the movement.
					characters.get(i).setPathModifier(path);
					characters.get(i).animate(1000, true);
					
					
					//Clear the ArrayLists for next command.
					xList.clear();
					yList.clear();
					
					//TODO: Need to clear lines here
					
					characters.get(i).selected = false;
				}
				else if (pSceneTouchEvent.isActionMove())
				{
					// In the case of anything other than pSceneTouchEvent.isActionUp() in other words while finger is down or moving. 
					// Add points to the arrays.
					xList.add(pSceneTouchEvent.getX());
					yList.add(pSceneTouchEvent.getY());
					
					
					
					//Connect this point to prevoius point.
					
					//Draw a line to show where the intended destination is:
					
					// List - 2 we are getting position of the previous point "Size of an Array is always + 1 to the index."
					//TODO: Character follows line from top left corner should follow from center.
					//TODO: Not sure if this is the best place to render a line. Lines should be rendered in "onCreateScene" method. Should add it to the arrayList here
					//and then have something like a "renderAllLines" method.
					
					Line myLine = new Line(xList.get(xList.size() - 2), yList.get(xList.size() - 2), pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), 5, getVertexBufferObjectManager());
					
					//This will only draw the line outside of the character.
					
					if (!myLine.collidesWith(characters.get(i)))
					{
						mScene.attachChild(myLine);
						//characters.get(i).attachChild(myLine);
						lineList.add(myLine);
					}
					else
					{
						myLine.dispose();
					}
					
					
				}
			
			}
			
		
		}*/
		
		/// This one works best.
		/// COmmented out for test.
		
		for (int i = 0; i < characters.size(); i++)
		{
			
			if (characters.get(i).selected)
			{
				
				//pSceneTouchEvent.getPointerID()
				//PathModifier path;
				
				if (pSceneTouchEvent.isActionUp())
				{
					
					character.unregisterEntityModifier(character.pathmodifier);
					
					float duration = characters.get(i).movementPath.getLength() / 50;
					
					// TODO 10 second time. Sprite will move faster over longer distances. Need to calculate a time so that speed is constant. 
					PathModifier path = new PathModifier(duration, characters.get(i).movementPath);
					//Character handles PathModifier and registers it. registerEntityModifer begins the movement.
					characters.get(i).setPathModifier(path);
					characters.get(i).movementPath = null;
					
					//characters.get(i).animate(1000, true);
					 final long[] pFrameDurations = new long[17];
			         Arrays.fill(pFrameDurations, 250);
					 character.animate(pFrameDurations, 0, 16, true);
					 
			         //hardcoded to 1 for test. Problem is mScene.getTag returns entity not Character.
			         //characters.get(1).isTargeted = false;
			       //If we haven;t targeted anyone it stay zero.
			         characters.get(i).setTarget(0);
			         
			         //character.stopAnimation();
			         
					 //Loop though to find which character we've targeted.
					 /*for (int targeted = 0; targeted < characters.size(); targeted++)
					 {
						 if (characters.get(targeted).isTargeted)
						 //if (characters.get(targeted).onAreaTouched(pSceneTouchEvent, pSceneTouchEvent.getX(), pSceneTouchEvent.getY()) && characters.get(targeted).selectable == false)
						 {
							 
							 //Give the targets tag to the character we are selecting
							 Log.d("Chris","Character: " + characters.get(targeted).getTag());
							 characters.get(i).setTarget(characters.get(targeted).getTag());
							 character.animate(pFrameDurations, 0, 16, true);
						 }
						 
						 
					 }*/
					 
					 characters.get(i).selected = false;
					
					
				}
				
				else if (pSceneTouchEvent.isActionMove())
				{
					if (characters.get(i).movementPath == null)
					{
						
						 Path myPath = new Path(2)
							.to(characters.get(i).getX(), characters.get(i).getY())
							.to(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
						 
						 characters.get(i).setPath(myPath);
					}
					else
					{
						//Path myPath = new Path(characters.get(i).movementPath.getSize() + 1);
						
						//characters.get(i).movementPath.to(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
						//characters.get(i).setPath(characters.get(i).movementPath);
						
						float[] xPathCoordinates = characters.get(i).movementPath.getCoordinatesX();
						float[] yPathCoordinates = characters.get(i).movementPath.getCoordinatesY();	
						
						float[] newxPathCoordinates = java.util.Arrays.copyOf(xPathCoordinates, xPathCoordinates.length + 1);
						newxPathCoordinates[newxPathCoordinates.length - 1] = pSceneTouchEvent.getX();
						
						float[] newyPathCoordinates = java.util.Arrays.copyOf(yPathCoordinates, yPathCoordinates.length + 1);
						newyPathCoordinates[newyPathCoordinates.length - 1] = pSceneTouchEvent.getY();
						
						Path myPath = new Path(newxPathCoordinates, newyPathCoordinates);
						
						characters.get(i).setPath(myPath);
						

					}
				}
			}
			
		}

		////////Cardinal modifier works, but movement is weird/
		
		/*for (int i = 0; i < characters.size(); i++)
		{
			if (characters.get(i).selected)
			{
				if (xList.size() == 0 && yList.size() == 0)
				{
					xList.add(characters.get(i).getX());
					yList.add(characters.get(i).getY());
				}
				
				if (pSceneTouchEvent.isActionUp())
				{
					character.unregisterEntityModifier(character.cardinalModifier);
					
					//float duration = characters.get(i).movementPath.getLength() / 50;
					//float duration = characters.get(i).cardinalModifier.
					
					// TODO 10 second time. Sprite will move faster over longer distances. Need to calculate a time so that speed is constant. 
					
					CardinalSplineMoveModifierConfig movementConfig = new CardinalSplineMoveModifierConfig(xList.size(), 1);
					
					for (int k = 0; k < xList.size(); k++)
					{
						movementConfig.setControlPoint(k, xList.get(k), yList.get(k));
					}
					
					CardinalSplineMoveModifier movement = new CardinalSplineMoveModifier(10, movementConfig);
					
					characters.get(i).setCardinalModifier(movement);
					
					//characters.get(i).animate(1000, true);
					 final long[] pFrameDurations = new long[17];
			         Arrays.fill(pFrameDurations, 250);
					 character.animate(pFrameDurations, 0, 16, true);
					
					characters.get(i).selected = false;
				}
				else if (pSceneTouchEvent.isActionMove())
				{
					// In the case of anything other than pSceneTouchEvent.isActionUp() in other words while finger is down or moving. 
					// Add points to the arrays.
					xList.add(pSceneTouchEvent.getX());
					yList.add(pSceneTouchEvent.getY());
					
					
					
					//Connect this point to prevoius point.
					
					//Draw a line to show where the intended destination is:
					
					// List - 2 we are getting position of the previous point "Size of an Array is always + 1 to the index."
					//TODO: Character follows line from top left corner should follow from center.
					//TODO: Not sure if this is the best place to render a line. Lines should be rendered in "onCreateScene" method. Should add it to the arrayList here
					//and then have something like a "renderAllLines" method.
					
					Line myLine = new Line(xList.get(xList.size() - 2), yList.get(xList.size() - 2), pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), 5, getVertexBufferObjectManager());
					
					//This will only draw the line outside of the character.
					
					if (!myLine.collidesWith(characters.get(i)))
					{
						mScene.attachChild(myLine);
						//characters.get(i).attachChild(myLine);
						lineList.add(myLine);
					}
					else
					{
						myLine.dispose();
					}
					
				}
			}
		}*/
			
		return false;
			
	}
	
	public float calculateDistance(Path myPath)
	{
		float total = 0;
		
		for (int i = 0; i < myPath.getLength(); i++)
		{
			total += myPath.getSegmentLength(i);
		}
		
		return total;
		
		
		
		/*float total = 0;
		
		for (int i = 0; i < movements.length; i++)
		{
			if ((i + 1) != (movements.length + 1))
				total += movements[i] + movements[i + 1];
		}
		return total;*/
		
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		//This works the line is eliminated however, it is not smooth.
		/*for (int i = 0; i < lineList.size(); i++)
		{
			if (lineList.get(i).collidesWith(characters.get(0)))
			{
				lineList.get(i).detachSelf();
				lineList.get(i).dispose();
				lineList.remove(i);
			}
		}*/
		
		//First have to figure out how to deselct targets
		
		/*for (int i = 0; i < characters.size(); i++)
		{
			//Log.d("Chris", "Target" + characters.get(i).target);
			if (characters.get(i).target == 0)
			{

			}
			else
			{
				//Get the distance between entities
				float distanceBetweenStuff = MathUtils.distance(characters.get(i).getX(), 
						characters.get(i).getY(), 
						mScene.getChildByTag(characters.get(i).target).getX(), 
						mScene.getChildByTag(characters.get(i).target).getY());
				//hardcode 50 probably have to determin e other stuff like screen size later. Have attack ranges and such,
				//Stop moving close.
				Log.d("Chris", "Distance Between Stuff." + distanceBetweenStuff);
				if (distanceBetweenStuff < 200.0f)
				{
					//characters.get(i).unregisterEntityModifier(character.pathmodifier);
					characters.get(1).setHitpoints(characters.get(1).getHitpoints() - 1);
				}
				//float distanceX = Math.abs(characters.get(i).getX() - mScene.getChildByTag(characters.get(i).target).getX());
				//float distanceY = Math.abs(characters.get(i).getY() - mScene.getChildByTag(characters.get(i).target).getY());
			}
		}*/
		
			//Test mode with hardcoded stuff.
		
			/*float distanceBetweenStuff = MathUtils.distance(character.getX(), 
					character.getY(), 
					character1.getX(), 
					character1.getY());
			//hardcode 50 probably have to determin e other stuff like screen size later. Have attack ranges and such,
			//Stop moving close.
			Log.d("Chris", "Distance Between Stuff." + distanceBetweenStuff);
			
			if (distanceBetweenStuff < 300.0f)
			{
				//characters.get(i).unregisterEntityModifier(character.pathmodifier);
				character1.setHitpoints(character1.getHitpoints() - 1);
			} 
			
			if (character1.getHitpoints() < 0)
			{
				mScene.detachChild(character1);
			}*/
		
			//Loop to determine which of the players chars are close enought to shoot enemies.
			
			for (int i = 0; i < characters.size(); i++)
			{
				for (int j = 0; j < characters.size(); j++)
				{
					//Don't compare to self.
					
						float distanceBetweenStuff = MathUtils.distance(characters.get(i).getX(), 
								characters.get(i).getY(), 
								characters.get(j).getX(), 
								characters.get(j).getY());
						//hardcode 50 probably have to determin e other stuff like screen size later. Have attack ranges and such,
						//Stop moving close.
						Log.d("Chris", "Distance Between Stuff." + distanceBetweenStuff);
						
						//getSelectable == false is a holdover until we add ranged attacks. 
						if (distanceBetweenStuff < 200.0f 
								&& characters.get(j).selectable == false && 
								characters.get(i).getTeam() != characters.get(j).getTeam())
						{
							//characters.get(i).unregisterEntityModifier(character.pathmodifier);
							if (characters.get(i).getTarget() == 0)
							{
								characters.get(i).setTarget(characters.get(j).getTag());
								characters.get(j).setHitpoints(characters.get(j).getHitpoints() - 1);
								
								if (characters.get(j).getHitpoints() < 0)
								{
									//mScene.detachChild(characters.get(j));
									//characters.remove(characters.get(j).getTag());
									characters.get(j).setDead(true);
									characters.get(i).setTarget(0);
								}
							} 
							else if (characters.get(i).getTarget() != 0 && MathUtils.distance(characters.get(i).getX(), 
									characters.get(i).getY(), 
									mScene.getChildByTag(characters.get(i).getTarget()).getX(), 
									mScene.getChildByTag(characters.get(i).getTarget()).getY()) >= distanceBetweenStuff)
							{
								characters.get(i).setTarget(characters.get(j).getTag());
								characters.get(j).setHitpoints(characters.get(j).getHitpoints() - 1);
								
								if (characters.get(j).getHitpoints() < 0)
								{
									//mScene.detachChild(characters.get(j));
									//characters.remove(characters.get(j).getTag());
									characters.get(j).setDead(true);
									characters.get(i).setTarget(0);
								}
							} 
							
							
							//characters.get(j).setHitpoints(characters.get(j).getHitpoints() - 1);
						} 
					
				}
				
			}
			
			for (int i = 0; i < characters.size(); i++)
			{
				if (characters.get(i).isDead())
				{
					//characters.remove(characters.get(i).getTag());
					//mScene.detachChild(characters.get(i));
				}
			}
			
			//Have enemy's chase player.
			
			for (int i = 0; i < characters.size(); i++)
			{
				if (characters.get(i).getTeam() == 2)
				{
					characters.get(i).unregisterEntityModifier(characters.get(i).movemodifier);
					float duration = MathUtils.distance(characters.get(i).getX(), characters.get(i).getY(), characters.get(0).getX(), characters.get(0).getY()) / 50;
					//hardcoded needs 0 changed. characters.get(target).getX() 
					MoveModifier mv = new MoveModifier(duration, characters.get(i).getX(), characters.get(0).getX(), characters.get(i).getY(), characters.get(0).getY());
					characters.get(i).registerEntityModifier(mv);
				}
				
			}
			
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}