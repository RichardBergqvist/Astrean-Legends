package com.astreanlegends.engine.entity;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private final float MINIMUM_DISTANCE_FROM_PLAYER = 30;
	private final float MAXIMUM_DISTANCE_FROM_PLAYER = 100;
	private final float MINIMUM_PITCH_3RD = 10;
	private final float MAXIMUM_PITCH_3RD = 90;
	private final float MINIMUM_PITCH_1ST = -90;
	private final float MAXIMUM_PITCH_1ST = 90;
	private final float MAXIMUM_ANGLE_AROUND_PLAYER = 140;
	
	private float distanceFromPlayer = 40;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch = 20;
	private float yaw = 0;
	
	private Player player;
	
	public Camera(Player player) {
		this.player = player;
	}

	public void move() {
		calculatePitch();
		calculateAngleAroundPlayer();
		if(!player.isFirstPerson()) {
			calculateZoom();
			float horizontalDistance = calculateHorizontalDistance();
			float verticalDistance = calculateVerticalDistance();
			calculateCameraPosition(horizontalDistance, verticalDistance);
			yaw = 180 - (player.getRotationY() + angleAroundPlayer);
		} else {
			calculateCameraPositionFirstPerson();
			yaw = 180 - player.getRotationY() + angleAroundPlayer;
		}
		yaw %= 360;
	}
	
	private void calculateZoom() {
		if(!player.isFirstPerson()) {
			float zoomLevel = Mouse.getDWheel() * 0.1F;
			distanceFromPlayer -= zoomLevel;
			if(distanceFromPlayer <= MINIMUM_DISTANCE_FROM_PLAYER)
				distanceFromPlayer = (MINIMUM_DISTANCE_FROM_PLAYER - 1);
			if(distanceFromPlayer >= MAXIMUM_DISTANCE_FROM_PLAYER)
				distanceFromPlayer = (MAXIMUM_DISTANCE_FROM_PLAYER - 1);
		}
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1F;
			pitch -= pitchChange;
			if(!player.isFirstPerson()) {
				if(pitch <= MINIMUM_PITCH_3RD)
					pitch = MINIMUM_PITCH_3RD;
				if(pitch >= MAXIMUM_PITCH_3RD)
					pitch = MAXIMUM_PITCH_3RD;
			} else {
				if(pitch <= MINIMUM_PITCH_1ST)
					pitch = MINIMUM_PITCH_1ST;
				if(pitch >= MAXIMUM_PITCH_1ST)
					pitch = MAXIMUM_PITCH_1ST;
			}
		}
	}
	
	private void calculateAngleAroundPlayer() {
		if(Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.1F;
			angleAroundPlayer -= angleChange;
			if(player.isFirstPerson()) {
				if(angleAroundPlayer >= MAXIMUM_ANGLE_AROUND_PLAYER)
					angleAroundPlayer = MAXIMUM_ANGLE_AROUND_PLAYER;
				if(angleAroundPlayer <= -MAXIMUM_ANGLE_AROUND_PLAYER)
					angleAroundPlayer = -MAXIMUM_ANGLE_AROUND_PLAYER;
			}
		}
	}
	
	private float calculateHorizontalDistance() {
		return (float)(distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float)(distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		float theta = player.getRotationY() + angleAroundPlayer;
		float xOffset = (float)(horizontalDistance * Math.sin(Math.toRadians(theta)));
		float zOffset = (float)(horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - xOffset;
		position.y = player.getPosition().y + verticalDistance;
		position.z = player.getPosition().z - zOffset;
	}
	
	private void calculateCameraPositionFirstPerson() {
		position.x = player.getPosition().x;
		position.y = player.getPosition().y + 10;
		position.z = player.getPosition().z;
	}
	
	public void invertPitch() {
		this.pitch = -pitch;
	}
	
	public void setPosition(float x, float y, float z) {
		this.position = new Vector3f(x, y, z);
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}
}