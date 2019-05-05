package com.astreanlegends.engine.entity;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private float distanceFromPlayer = 35;
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
		}
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1F;
			pitch -= pitchChange;
		}
	}
	
	private void calculateAngleAroundPlayer() {
		if(Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.1F;
			angleAroundPlayer -= angleChange;
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