package com.test.testgame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.runnable.RunnableHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.CardinalSplineMoveModifier;
import org.andengine.entity.modifier.CardinalSplineMoveModifier.CardinalSplineMoveModifierConfig;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.array.ArrayUtils;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

public class TestGameActivity extends BaseGameActivity implements IOnSceneTouchListener, IUpdateHandler {
    /** Called when the activity is first created. */
	
	Scene mScene;
	static final float mCameraWidth = 800;
	static final float mCameraHeight = 480;
	
	
	 private BitmapTextureAtlas mBitmapTextureAtlas;
	 private TiledTextureRegion mPlayerTiledTextureRegion;
	 private BitmapTextureAtlas mBitmapTextureAtlas1;
	 private TiledTextureRegion mPlayerTiledTextureRegion1;
	 private TiledTextureRegion mPlayerTiledTextureRegion2;
	 private BitmapTextureAtlas mBitmapTextureAtlas2;
	 private BitmapTextureAtlas mPausedTextureAtlas;
	 
	 private BitmapTextureAtlas arrowBitmapTextureAtlas;
	 private TiledTextureRegion arrowTiledTextureRegion;
	 
	 MovementManager manager = new MovementManager();
	 Character character;
	 Character character1;
	 Character character2;
	 
	 ArrayList<ArrowProjectile> projectiles = new ArrayList<ArrowProjectile>();
	 
	 ArrayList<Character> characters = new ArrayList<Character>();
	 
	 ArrayList<Float> xList = new ArrayList<Float>();
	 ArrayList<Float> yList = new ArrayList<Float>();
	 ArrayList<Line> lineList = new ArrayList<Line>();
	 
	 ArrayList<Character> allies = new ArrayList<Character>();
	 ArrayList<Character> enemies = new ArrayList<Character>();
	 
	 //COunt seconds to control updates/
	 float lastSecondsElapsed = 0;
	 float rateOfFire = 0;
	 
	 private Handler mHandler;
	 private RunnableHandler rHandler;
	 
	 private TMXTiledMap mTileMap1;
	 private TMXLayer tmxLayer;
	 private TMXTile tmxTile;
	 
	 private TextureRegion mPausedTextureRegion;
	 private Scene mPauseScene;

	 
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		mHandler = new Handler();
		rHandler = new RunnableHandler();
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
			//mBitmapTextureAtlas1 = new BitmapTextureAtlas(this.getTextureManager(), 512, 256);
			mBitmapTextureAtlas1 = new BitmapTextureAtlas(this.getTextureManager(), 72, 128);
			//mBitmapTextureAtlas1 = new BitmapTextureAtlas(this.getTextureManager(), 2008, 94);
			this.mPlayerTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0, 1, 1);
			mBitmapTextureAtlas.load();
			//this.mPlayerTiledTextureRegion1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas1, this, "face_box_animation.png", 0, 0, 2, 2);
			//this.mPlayerTiledTextureRegion1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas1, this, "trot1_done.png", 0, 0, 5, 4);
			this.mPlayerTiledTextureRegion1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas1, this, "player.png", 0, 0, 3, 4);
			//this.mPlayerTiledTextureRegion1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas1, this, "TexturePacker1.png", 0, 0, 15, 1);
			mBitmapTextureAtlas1.load();
			
			//mBitmapTextureAtlas2 = new BitmapTextureAtlas(this.getTextureManager(), 32, 32);
			mBitmapTextureAtlas2 = new BitmapTextureAtlas(this.getTextureManager(), 72, 128);
			//this.mPlayerTiledTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas2, this, "face_box.png", 0, 0, 1, 1);
			this.mPlayerTiledTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas2, this, "enemy.png", 0, 0, 3, 4);
			mBitmapTextureAtlas2.load();
			//mBitmapTextureAtlas1.load();
			
			this.arrowBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 8, 32); 
			this.arrowTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.arrowBitmapTextureAtlas, this, "arrow.png", 0, 0, 1, 1);
			arrowBitmapTextureAtlas.load();
			
			mPausedTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 200,50);
			mPausedTextureRegion = BitmapTextureAtlasTextureRegionFactory
					    .createFromAsset(mPausedTextureAtlas, this, "paused.png", 0, 0);
			mPausedTextureAtlas.load();

			
		 pOnCreateResourcesCallback.onCreateResourcesFinished();
		
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		// TODO Auto-generated method stub
		 mScene = new Scene();
		 mScene.setBackground(new Background(0,0,255));
		 
		 try
		 {
			 TMXLoader tmxLoader = new TMXLoader(getAssets(), this.getEngine().getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, getVertexBufferObjectManager());
			 this.mTileMap1 = tmxLoader.loadFromAsset("desert.tmx");
		 }
		 catch (final TMXLoadException tmxle) {
	         Debug.e(tmxle);
		 }
		 
		 tmxLayer = this.mTileMap1.getTMXLayers().get(0);
		 mScene.attachChild(tmxLayer);
		 
		 //this.get
		 character = new Character(mCameraWidth / 2, mCameraHeight / 2, mPlayerTiledTextureRegion1, getVertexBufferObjectManager());
		  //character = new Character(mCameraWidth / 2, mCameraHeight / 2, mPlayerTiledTextureRegion1, this.);
		 character.setTag(1);
		 character.setScale(2.0f);
		 //character.animate(10000);
//		 final long[] pFrameDurations = new long[17];
//         Arrays.fill(pFrameDurations, 250);
//		 character.animate(pFrameDurations, 0, 16, true);
		 mScene.attachChild(character);
		 mScene.registerTouchArea(character);
		 character.selectable = true;
		 character.setHitpoints(10);
		 character.setTeam(1);
		 character.setRange(200);
		 character.setRateOfFire(0);
		 characters.add(character);
		 allies.add(character);
		 
		 Character testPlayer2 = new Character(mCameraWidth / 2, mCameraHeight / 2 + 90, mPlayerTiledTextureRegion1, getVertexBufferObjectManager());
		 testPlayer2.setTag(5);
		 testPlayer2.setScale(2.0f);
		 mScene.attachChild(testPlayer2);
		 mScene.registerTouchArea(testPlayer2);
		 testPlayer2.selectable = true;
		 testPlayer2.setHitpoints(10);
		 testPlayer2.setTeam(1);
		 testPlayer2.setRange(200);
		 testPlayer2.setRateOfFire(0);
		 allies.add(testPlayer2);
		 
		 
		 character1 = new Character(mCameraWidth - 80, mCameraHeight - 40, mPlayerTiledTextureRegion2, getVertexBufferObjectManager());
		 character1.setTag(2);
		 //character1.setColor(Color.RED);
		 character1.setScale(2.0f);
		 mScene.attachChild(character1);
		 mScene.registerTouchArea(character1);
		 character1.selectable = false;
		 character1.setHitpoints(5);
		 character1.setTeam(2);
		 characters.add(character1);
		 enemies.add(character1);
		 
		 character2 = new Character(mCameraWidth / 8, mCameraHeight / 8, mPlayerTiledTextureRegion2, getVertexBufferObjectManager());
		 character2.setTag(3);
		 //character2.setColor(Color.GREEN);
		 character2.setScale(2.0f);
		 mScene.attachChild(character2);
		 mScene.registerTouchArea(character2);
		 character2.selectable = false;
		 character2.setHitpoints(5);
		 character2.setTeam(2);
		 characters.add(character2);
		 enemies.add(character2);
		 
		 mScene.registerUpdateHandler(this);
		 mScene.setOnSceneTouchListener(this);
		 
		// mHandler.postDelayed(mDeclareDead, 100);
		 mPauseScene = new Scene();
		 final Sprite pausedSprite = new Sprite(mCameraWidth / 2 - 100, mCameraHeight / 2 - 25, mPausedTextureRegion, getVertexBufferObjectManager());
		 mPauseScene.attachChild(pausedSprite);
		 mPauseScene.setBackgroundEnabled(false);
		 pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

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
		// TODO Peformance issues already encountered possible blocking TouchEvent with all the calculations. A good reason to use historical touch info and updateHandle
		
		for (int i = 0; i < allies.size(); i++)
		{
			
			if (allies.get(i).selected)
			{
				
				//PathModifiers must have at least two coordinates. This adds current position as the first.
				if (xList.size() == 0 && yList.size() == 0)
				{
					xList.add(allies.get(i).getX());
					yList.add(allies.get(i).getY());
					
					/*xList.add(allies.get(i).getCenterX());
					yList.add(allies.get(i).getCenterY());*/
					
					
				}
				
				//If the player has lifted thier finger add the coordinate arrays to the Path object and begin the movement.
				if (pSceneTouchEvent.isActionUp())
				{
					//Cancel current path
					allies.get(i).unregisterEntityModifier(allies.get(i).pathmodifier);
					
					//We cannot convert directly from ArrayList to Float[] so we have to hold the value in a list.
					List<Float> xcoordinates = xList;
					List<Float> ycoordinates = yList;
					
					//FInd if player selected an enemy.
					for (int targets = 0; targets < enemies.size(); targets++)
					{
						if (enemies.get(targets).isTargeted)
						{
							Log.d("EnemyTest", "Targeted Enemy: " + enemies.get(targets).getTag());
							allies.get(i).setTarget(enemies.get(targets).getTag());
							enemies.get(targets).isTargeted = false;
						}
					}
					
					//ArrayUtils is a utility provided by the Appache Commons license.
					//We can convert from a List to a float[].
					Path myPath = new Path(ArrayUtils.toFloatArray(xcoordinates), ArrayUtils.toFloatArray(ycoordinates));
					
					//TODO: Speed is hardcoded at 50.
					float duration = myPath.getLength() / 50;

					/*PathModifier path = new PathModifier(duration, myPath);
					
					//Character handles PathModifier and registers it. registerEntityModifer begins the movement.
					allies.get(i).setPathModifier(path);		
					final long[] pFrameDurations = new long[3];
			        Arrays.fill(pFrameDurations, 200);
			        //each frame for 200 milliseconds.
			        //Frames 0, 1, and 2 in the spritesheet contain the animation.
			        //true - loop the animations
					allies.get(i).animate(pFrameDurations, 0, 2, true);*/
					
					
					allies.get(i).setPathAndDuration(myPath, duration);
					
					//Clear the ArrayLists for next command.
					xList.clear();
					yList.clear();
					
					//Because player has lifted finger character deselected.
					allies.get(i).selected = false;
				}
				else if (pSceneTouchEvent.isActionMove())
				{
					// In the case of anything other than pSceneTouchEvent.isActionUp() in other words while finger is down or moving. 
					// Add points to the arrays.
					xList.add(pSceneTouchEvent.getX());
					yList.add(pSceneTouchEvent.getY());
					
					/*xList.add(pSceneTouchEvent.getX() + allies.get(i).getWidth() / 2);
					yList.add(pSceneTouchEvent.getY() + allies.get(i).getHeight() / 2);*/
					
					
					
					//Connect this point to prevoius point.
					
					//Draw a line to show where the intended destination is:
					
					// List - 2 we are getting position of the previous point "Size of an Array is always + 1 to the index."
					//TODO: Character follows line from top left corner should follow from center.
					//TODO: Not sure if this is the best place to render a line. Lines should be rendered in "onCreateScene" method. Should add it to the arrayList here
					//and then have something like a "renderAllLines" method.
					
					/*Line myLine = new Line(xList.get(xList.size() - 2), yList.get(xList.size() - 2), pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), 5, getVertexBufferObjectManager());
					
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
					}*/
					
					
				}
			
			}
			
		
		}
		
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
		
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		
		// update player targets every 0.3 seconds
		//TODO: consider moving all movement sprite updates to OnUpdate event for sprite. Should be able to eliminate most of the for loops.
		if (lastSecondsElapsed > 0.3)
		{
			for (int i = 0; i < allies.size(); i++)
			{
				float distanceTocurrentTarget = 9999.0f;
				
				if (allies.get(i).getTarget() != 0)
				{
					distanceTocurrentTarget = MathUtils.distance(allies.get(i).getX(), 
						allies.get(i).getY(), 
						mScene.getChildByTag(allies.get(i).getTarget()).getX(), 
						mScene.getChildByTag(allies.get(i).getTarget()).getY());
				}
				
				//TODO: Switch Targets if enemy is dead.
				//Out of range find a new target.
				if (distanceTocurrentTarget > (allies.get(i).getRange()))// || allies.get(i).getTarget() == 0)
				{
					allies.get(i).setTarget(0);
				}
				
					for (int j = 0; j < enemies.size(); j++)
					{
						float distanceBetweenStuff = MathUtils.distance(allies.get(i).getX(), 
								allies.get(i).getY(), 
								enemies.get(j).getX(), 
								enemies.get(j).getY());
						 
						if (distanceBetweenStuff < allies.get(i).getRange())
						{
							//set the target.
							allies.get(i).setTarget(enemies.get(j).getTag());
							
							Log.d("RateOfFire", "is: " + allies.get(i).getRateOfFire());
							if (allies.get(i).getRateOfFire() > 3.0f)
							{
								ArrowProjectile arrow = new ArrowProjectile(allies.get(i).getCenterX(), allies.get(i).getCenterY(), arrowTiledTextureRegion, getVertexBufferObjectManager());
								//projectiles.add(arrow);
								mScene.attachChild(arrow);
								float arctangent = MathUtils.atan2(arrow.getY() - mScene.getChildByTag(allies.get(i).getTarget()).getY(), arrow.getX() - mScene.getChildByTag(allies.get(i).getTarget()).getX());
								//arctangent - 90 because screen coordinates are different from standard coordinate plane.
								arrow.setRotation(MathUtils.radToDeg(arctangent) - 90.0f);
								arrow.moveProjectile(new MoveModifier(0.5f, 
										allies.get(i).getCenterX(), 
										mScene.getChildByTag(allies.get(i).getTarget()).getX(), 
										allies.get(i).getCenterY(),  
										mScene.getChildByTag(allies.get(i).getTarget()).getY()));
								arrow.registerEntityModifier(arrow.getMovement());
								//TODO: better calculate RoF
								//rateOfFire = 0;
								projectiles.add(arrow);
								allies.get(i).setRateOfFire(0);
								enemies.get(j).setHitpoints(enemies.get(j).getHitpoints() - 1);
								Log.d("HitPpints", "HItpoints: " + enemies.get(j).getHitpoints());
								if (enemies.get(j).getHitpoints() <= 0)
								{
									allies.get(i).setTarget(0);
									enemies.get(j).setDead(true);
								}
							}
							
						}
						
						
					}
					
					
					
					//Because we only have animations for four directions. We set the direction based on if the
					//arctanget falls between certain values.
					//TODO: Keep current arctangent as a property of Character. If new arctangent does not require for new direction. Do not bother changing animation.
					
					
				allies.get(i).setRateOfFire(allies.get(i).getRateOfFire() + pSecondsElapsed);
			}
			Log.d("Seconds Elapsed: ", pSecondsElapsed + "");
			//lastSecondsElapsed += 1;
			//TODO: replace with UpdateHandler ro TimerHandler.
			//Keeps track of time between updates.
			lastSecondsElapsed = 0;
			//rateOfFire += pSecondsElapsed;
			
			///Have enemies acquire targets.
			
			
			for (int i = 0; i < enemies.size(); i++)
			{
				/*float distanceTocurrentTarget = 9999.0f;
				
				if (enemies.get(i).getTarget() != 0)
				{
					distanceTocurrentTarget = MathUtils.distance(enemies.get(i).getX(), 
							enemies.get(i).getY(), 
						mScene.getChildByTag(enemies.get(i).getTarget()).getX(), 
						mScene.getChildByTag(enemies.get(i).getTarget()).getY());
				}*/
				
				for (int j = 0; j < allies.size(); j++)
				{
					if (enemies.get(i).collidesWith(allies.get(j)))
					{
						allies.get(j).setDead(true);
						enemies.get(i).setTarget(0);
					}
					//TODO: Find closest player target. Enemy will just attack bottom of the list.
					if (!allies.get(j).isDead())
					{
						float distanceBetweenStuff = MathUtils.distance(enemies.get(i).getX(), 
								enemies.get(i).getY(), 
								allies.get(j).getX(), 
								allies.get(j).getY());
						Log.d("Distance Between Stuff:", "Distance Between Stuff: " + distanceBetweenStuff);
						
						
						//Determine if target j is closer than the enemies current target, if so change targets.
						//TODO: More complex AI.
						//If an enemies target is 0 it has no target. Set to the current target.
						if (enemies.get(i).getTarget() == 0)
						{
							enemies.get(i).setTarget(allies.get(j).getTag());
						}
						else
						{
							float distanceTocurrentTarget = MathUtils.distance(enemies.get(i).getX(), 
									enemies.get(i).getY(), 
								mScene.getChildByTag(enemies.get(i).getTarget()).getX(), 
								mScene.getChildByTag(enemies.get(i).getTarget()).getY());
							
							if (distanceTocurrentTarget > distanceBetweenStuff)
							{
								enemies.get(i).setTarget(allies.get(j).getTag());
							}
						}
						
						//
					//}
				//}
						//TODO: Bug: If the enemies target is 0 it will search for the 0th child of scene. Currently character obj.
						//enemies.get(i).setTarget(allies.get(j).getTag());
						enemies.get(i).unregisterEntityModifier(enemies.get(i).movemodifier);
						float duration = (MathUtils.distance(enemies.get(i).getX(), 
								enemies.get(i).getY(), 
							mScene.getChildByTag(enemies.get(i).getTarget()).getX(), 
							mScene.getChildByTag(enemies.get(i).getTarget()).getY())) / 10;
						//Move the enemy to the players current position. 
						MoveModifier mv = new MoveModifier(duration, 
								enemies.get(i).getX(), 
								mScene.getChildByTag(enemies.get(i).getTarget()).getX(), 
								enemies.get(i).getY(),  
								mScene.getChildByTag(enemies.get(i).getTarget()).getY());
						enemies.get(i).setMoveModifier(mv);
						enemies.get(i).registerEntityModifier(mv);
						Log.d("AtanTest", i + ": " +  MathUtils.atan2(mScene.getChildByTag(enemies.get(i).getTarget()).getY(), mScene.getChildByTag(enemies.get(i).getTarget()).getX()));
						//calcuate arctangent between enemy pos and target pos in order to determine which way the enemy should be rotated.
						float arctangent = MathUtils.atan2(enemies.get(i).getY() - mScene.getChildByTag(enemies.get(i).getTarget()).getY(), enemies.get(i).getX() - mScene.getChildByTag(enemies.get(i).getTarget()).getX());
						
						arctangent = Math.abs(arctangent);
						
						//Because we only have animations for four directions. We set the direction based on if the
						//arctanget falls between certain values.
						//TODO: Keep current arctangent as a property of Character. If new arctangent does not require for new direction. Do not bother changing animation.
						if (arctangent > 0.7853981633974483 && arctangent < 2.356194490192345)
						{
							enemies.get(i).stopAnimation();
							final long[] pFrameDurations = new long[3];
					        Arrays.fill(pFrameDurations, 200);
							enemies.get(i).animate(pFrameDurations, 0, 2, true);
						}
						else if (arctangent > 2.356194490192345 && arctangent < 3.9269908169872414)
						{
							enemies.get(i).stopAnimation();
							final long[] pFrameDurations = new long[3];
					        Arrays.fill(pFrameDurations, 200);
							enemies.get(i).animate(pFrameDurations, 3, 5, true);
						}
						else if (arctangent > 3.9269908169872414 && arctangent < 5.497787143782138)
						{
							enemies.get(i).stopAnimation();
							final long[] pFrameDurations = new long[3];
					        Arrays.fill(pFrameDurations, 200);
							enemies.get(i).animate(pFrameDurations, 6, 8, true);
						}
						else
						{
							enemies.get(i).stopAnimation();
							final long[] pFrameDurations = new long[3];
					        Arrays.fill(pFrameDurations, 200);
							enemies.get(i).animate(pFrameDurations, 9, 11, true);
						}
					
					}
				}
				
				
			}
			
			//Cleanup dead
			//rHandler.postRunnable(mDeclareDead);
			/*runOnUiThread(new Runnable() {
		        public void run() {
		        	for (int i = 0; i < allies.size(); i++)
					{
						if (allies.get(i).isDead())
						{
							//allies.remove(i);
							allies.get(i).setVisible(false);
							allies.get(i).setIgnoreUpdate(true);
							allies.get(i).clearEntityModifiers();
							allies.get(i).clearUpdateHandlers();
							//allies.remove(i);
							mScene.detachChild(allies.get(i));
							allies.remove(i);
						}
					}
					
					for (int i = 0; i < enemies.size(); i++)
					{
						if (enemies.get(i).isDead())
						{
							mScene.detachChild(enemies.get(i));
						}
					}
		   }
			
		     });*/
         
		     
			for (int i = 0; i < allies.size(); i++)
			{
				if (allies.get(i).isDead())
				{
					//allies.remove(i);
					allies.get(i).setVisible(false);
					allies.get(i).setIgnoreUpdate(true);
					allies.get(i).clearEntityModifiers();
					allies.get(i).clearUpdateHandlers();
					allies.remove(i);
				}
			}
			
			for (int i = 0; i < enemies.size(); i++)
			{
				if (enemies.get(i).isDead())
				{
					enemies.get(i).setVisible(false);
					enemies.get(i).setIgnoreUpdate(true);
					enemies.get(i).clearEntityModifiers();
					enemies.get(i).clearUpdateHandlers();
					enemies.remove(i);
				}
			}
			
			for (int i = 0; i < projectiles.size(); i++)
			{
				if (projectiles.get(i).getMovement().isFinished())
				{
					projectiles.get(i).setVisible(false);
					projectiles.get(i).setIgnoreUpdate(true);
					projectiles.get(i).clearEntityModifiers();
					projectiles.get(i).clearUpdateHandlers();
					projectiles.remove(i);
				}
			}
			
		}
		else 
		{
			lastSecondsElapsed += pSecondsElapsed;
			rateOfFire += pSecondsElapsed;
			//lastSecondsElapsed += 1;
			for (int i = 0; i < allies.size(); i++)
			{
				allies.get(i).setRateOfFire(allies.get(i).getRateOfFire() + pSecondsElapsed);
				Log.d("RateOfFire2", "M: " + allies.get(i).getRateOfFire());
			}
		}
	}

		private Runnable mDeclareDead = new Runnable() {
	        public void run() {
	        	for (int i = 0; i < allies.size(); i++)
				{
					if (allies.get(i).isDead())
					{
						//allies.remove(i);
						/*allies.get(i).setVisible(false);
						allies.get(i).setIgnoreUpdate(true);
						allies.get(i).clearEntityModifiers();
						allies.get(i).clearUpdateHandlers();*/
						//allies.remove(i);
						mScene.detachChild(allies.get(i));
						allies.remove(i);
					}
				}
				
				for (int i = 0; i < enemies.size(); i++)
				{
					if (enemies.get(i).isDead())
					{
						mScene.detachChild(enemies.get(i));
					}
				}
	   }
	     };
	

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	

	public void pauseGame() {
	    mScene.setChildScene(mPauseScene, false, true, true);
	    mEngine.stop();
	}
	
	public void unPauseGame() {
		mScene.clearChildScene();
	    mEngine.start();
	}


	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
	    if (pKeyCode == KeyEvent.KEYCODE_MENU
	        && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
	        if (mEngine.isRunning()) {
	            pauseGame();
	            //Toast.makeText(this, "Menu button to resume",
	                //Toast.LENGTH_SHORT).show();
	        } else {
	            unPauseGame();
	        }
	        return true;
	    }
	    return super.onKeyDown(pKeyCode, pEvent);
	}


}