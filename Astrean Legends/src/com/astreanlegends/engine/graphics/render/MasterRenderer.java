package com.astreanlegends.engine.graphics.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.astreanlegends.engine.entity.Camera;
import com.astreanlegends.engine.entity.Entity;
import com.astreanlegends.engine.graphics.ResourceLoader;
import com.astreanlegends.engine.graphics.font.Font;
import com.astreanlegends.engine.graphics.gui.GuiText;
import com.astreanlegends.engine.graphics.lighting.Light;
import com.astreanlegends.engine.graphics.model.TexturedModel;
import com.astreanlegends.engine.graphics.shader.EntityShader;
import com.astreanlegends.engine.graphics.shader.FontShader;
import com.astreanlegends.engine.graphics.shader.GuiShader;
import com.astreanlegends.engine.graphics.shader.NormalMapShader;
import com.astreanlegends.engine.graphics.shader.SkyboxShader;
import com.astreanlegends.engine.graphics.shader.TerrainShader;
import com.astreanlegends.engine.graphics.shader.WaterShader;
import com.astreanlegends.engine.graphics.texture.GuiTexture;
import com.astreanlegends.engine.utility.WaterFramebuffers;
import com.astreanlegends.engine.world.terrain.Terrain;
import com.astreanlegends.engine.world.water.WaterTile;

public class MasterRenderer {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1F;
	private static final float FAR_PLANE = 1000;
	
	public static final float RED = 0.1F;
	public static final float GREEN = 0.4F;
	public static final float BLUE = 0.2F;
	
	private Matrix4f projectionMatrix;
	
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;
	
	private EntityShader entityShader = new EntityShader();
	private EntityRenderer entityRenderer;
	
	private NormalMapShader normalMapShader = new NormalMapShader();
	private NormalMapRenderer normalMapRenderer;
	
	private WaterShader waterShader = new WaterShader();
	private WaterRenderer waterRenderer;
	
	private SkyboxShader skyboxShader = new SkyboxShader();
	private SkyboxRenderer skyboxRenderer;
	
	private GuiShader guiShader = new GuiShader();
	private GuiRenderer guiRenderer;
	
	private FontShader fontShader = new FontShader();
	private FontRenderer fontRenderer;
	
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private Map<TexturedModel, List<Entity>> normalMapEntities = new HashMap<TexturedModel, List<Entity>>();
	private List<WaterTile> waters = new ArrayList<WaterTile>();
	private List<GuiTexture> guis = new ArrayList<GuiTexture>();
	private Map<Font, List<GuiText>> texts = new HashMap<Font, List<GuiText>>();
	
	public MasterRenderer(ResourceLoader loader, Light sun, WaterFramebuffers buffers) {
		System.out.println(getOpenGLVersion());
		enableCulling();
		createProjectionMatrix();
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
		normalMapRenderer = new NormalMapRenderer(normalMapShader, projectionMatrix);
		waterRenderer = new WaterRenderer(waterShader, loader, projectionMatrix, buffers, sun);
		skyboxRenderer = new SkyboxRenderer(skyboxShader, loader, projectionMatrix);
		guiRenderer = new GuiRenderer(guiShader, loader);
		fontRenderer = new FontRenderer(fontShader, loader);
	}
	
	public void init() {
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(RED, GREEN, BLUE, 1);
	}
	
	public void render(List<Terrain> terrains, List<Entity> entities, List<Entity> normalMapEntities, List<Light> lights, Camera camera, Vector4f clipPlane) {
		for(Terrain terrain : terrains)
			addTerrain(terrain);
		for(Entity entity : entities)
			addEntity(entity);
		for(Entity normalMapEntity : normalMapEntities)
			addNormalMapEntity(normalMapEntity);
		render(lights, camera, clipPlane);
	}
	
	public void postRender(List<GuiTexture> guis, List<GuiText> texts) {
		for(GuiTexture gui : guis)
			addGUI(gui);
		for(GuiText text : texts)
			addText(text);
		postRender();
	}
	
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
		init();
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		entityShader.start();
		entityShader.loadClipPlane(clipPlane);
		entityShader.loadSkyColor(RED, GREEN, BLUE);
		entityShader.loadLights(lights);
		entityShader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		entityShader.stop();
		normalMapShader.start();
		normalMapShader.loadClipPlane(clipPlane);
		normalMapShader.loadSkyColor(RED, GREEN, BLUE);
		normalMapShader.loadLights(lights, camera);
		normalMapShader.loadViewMatrix(camera);
		normalMapRenderer.render(normalMapEntities, clipPlane, lights, camera);
		normalMapShader.stop();
		skyboxShader.start();
		skyboxShader.loadViewMatrix(camera);
		skyboxShader.loadFogColor(RED, GREEN, BLUE);
		skyboxRenderer.render();
		skyboxShader.stop();
		
		terrains.clear();
		entities.clear();
		normalMapEntities.clear();
		waters.clear();
	}
	
	public void postRender(List<WaterTile> waters, Camera camera) {
		for (WaterTile water : waters)
			addWater(water);
		postRenderWater(waters, camera);
	}
	
	public void postRender() {
		glDisable(GL_CLIP_DISTANCE0);
		guiShader.start();
		guiRenderer.render(guis);
		guiShader.stop();
		fontShader.start();
		fontRenderer.render(texts);
		fontShader.stop();
		glEnable(GL_CLIP_DISTANCE0);
		
		guis.clear();
		texts.clear();
	}
	
	public void postRenderWater(List<WaterTile> waters, Camera camera) {
		waterShader.start();
		waterShader.loadViewMatrix(camera);
		waterRenderer.render(waters);
		waterShader.stop();
	}
	
	public void addTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void addEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch != null)
			batch.add(entity);
		else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void addNormalMapEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
	    List<Entity> batch = normalMapEntities.get(entityModel);
	    if (batch != null) {
	    	batch.add(entity);
	    } else {
	    	List<Entity> newBatch = new ArrayList<Entity>();
	    	newBatch.add(entity);
	    	normalMapEntities.put(entityModel, newBatch);
	   }
	}
	
	public void addWater(WaterTile water) {
		waters.add(water);
	}
	
	public void addGUI(GuiTexture gui) {
		guis.add(gui);
	}
	
	public void addText(GuiText text) {
		Font font = text.getFont();
		List<GuiText> batch = texts.get(font);
		if(batch != null)
			batch.add(text);
		else {
			List<GuiText> newBatch = new ArrayList<GuiText>();
			newBatch.add(text);
			texts.put(font, newBatch);
		}
	}
	
	public void clean() {
		terrainShader.clean();
		entityShader.clean();
		normalMapShader.clean();
		waterShader.clean();
		skyboxShader.clean();
		guiShader.clean();
		fontShader.clean();
		fontRenderer.clean();
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float)Display.getWidth() / (float)Display.getHeight();
		float yScale = (float)((1F / Math.tan(Math.toRadians(FOV / 2F))) * aspectRatio);
		float xScale = yScale / aspectRatio;
		float frustumLength = FAR_PLANE - NEAR_PLANE;
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = xScale;
		projectionMatrix.m11 = yScale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
		projectionMatrix.m33 = 0;
	}
	
	public static void enableCulling() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
	}
	
	public static void disableCulling() {
		glDisable(GL_CULL_FACE);
	}
	
	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}