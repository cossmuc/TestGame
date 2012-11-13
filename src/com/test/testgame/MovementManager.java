package com.test.testgame;

import java.util.HashMap;

import org.andengine.entity.modifier.PathModifier;


/**
 * 
 * @author Chris
 * WIP. MovementManager needs to keep track of all the coordinates for movement of all the units.
 */
public class MovementManager 
{
	
	HashMap<Integer,PathModifier> movements = new HashMap<Integer, PathModifier>();
	
	public MovementManager()
	{
		
	}
	
}
