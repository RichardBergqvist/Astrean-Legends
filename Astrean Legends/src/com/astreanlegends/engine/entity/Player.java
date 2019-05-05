package com.astreanlegends.engine.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import com.astreanlegends.engine.graphics.DisplayManager;
import com.astreanlegends.engine.graphics.model.TexturedModel;
import com.astreanlegends.engine.world.terrain.Terrain;

public class Player extends Entity {

	private static final float WALKING_SPEED = 20;
	private static final float RUNNING_SPEED = 40;
	private static final float TURNING_SPEED = 160;
	private static final float GRAVITY = -50;
	private static final float JUMPING_POWER = 18;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	
	private boolean isCrouching = false;
	private boolean isAirborne = false;
	private boolean isFirstPerson = false;
	
	public Player(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale) {
		super(model, position, rotationX, rotationY, rotationZ, scale);
	}

	public void move(Terrain terrain) {
		checkInputs();
		rotate(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float)(distance * Math.sin(Math.toRadians(getRotationY())));
		float dz = (float)(distance * Math.cos(Math.toRadians(getRotationY())));
		move(dx, 0, dz);
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		move(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
		if(getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			isAirborne = false;
			getPosition().y = terrainHeight;
		}
	}
	
	private void jump() {
		if(!isAirborne) {
			upwardsSpeed = JUMPING_POWER;
			isAirborne = true;
		}
	}
	
	private void crouch() {
		isCrouching = true;
		// TODO: Implement player crouching.
	}
	
	private void switchPerspective(boolean switchPerspective) {
		if(switchPerspective)
			if(!isFirstPerson)
				isFirstPerson = true;
		if(!switchPerspective)
			if(isFirstPerson)
				isFirstPerson = false;
			
	}
	
	private void checkInputs() {
		float currentSpeedW = WALKING_SPEED;
		// RUNNING
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			currentSpeedW = RUNNING_SPEED;
		}
		
		// WALKING
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
			currentSpeed = currentSpeedW;
		else if(Keyboard.isKeyDown(Keyboard.KEY_S))
			currentSpeed = -currentSpeedW;
		else
			currentSpeed = 0;
		
		// TURNING
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
			currentTurnSpeed = -TURNING_SPEED;
		else if(Keyboard.isKeyDown(Keyboard.KEY_A))
			currentTurnSpeed = TURNING_SPEED;
		else
			currentTurnSpeed = 0;
		
		// JUMPING
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			jump();
		
		// CROUCHING
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
			crouch();
		
		// PERSPECTIVE SWITCH
		if(Keyboard.isKeyDown(Keyboard.KEY_F3))
			switchPerspective(true);
		if(Keyboard.isKeyDown(Keyboard.KEY_F2))
			switchPerspective(false);
	}

	public boolean isCrouching() {
		return isCrouching;
	}

	public boolean isAirborne() {
		return isAirborne;
	}

	public boolean isFirstPerson() {
		return isFirstPerson;
	}
}