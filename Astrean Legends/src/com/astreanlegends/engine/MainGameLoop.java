package com.astreanlegends.engine;

import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.astreanlegends.engine.entity.Camera;
import com.astreanlegends.engine.entity.Entity;
import com.astreanlegends.engine.entity.Player;
import com.astreanlegends.engine.graphics.DisplayManager;
import com.astreanlegends.engine.graphics.OBJFileLoader;
import com.astreanlegends.engine.graphics.ResourceLoader;
import com.astreanlegends.engine.graphics.lighting.Light;
import com.astreanlegends.engine.graphics.model.Model;
import com.astreanlegends.engine.graphics.model.ModelData;
import com.astreanlegends.engine.graphics.model.TexturedModel;
import com.astreanlegends.engine.graphics.render.MasterRenderer;
import com.astreanlegends.engine.graphics.texture.GuiTexture;
import com.astreanlegends.engine.graphics.texture.ModelTexture;
import com.astreanlegends.engine.graphics.texture.TerrainTexture;
import com.astreanlegends.engine.graphics.texture.TerrainTexturePack;
import com.astreanlegends.engine.utility.MousePicker;
import com.astreanlegends.engine.utility.WaterFramebuffers;
import com.astreanlegends.engine.world.terrain.Terrain;
import com.astreanlegends.engine.world.water.WaterTile;

public class MainGameLoop {

	public static void main(String[] arguments) {
		DisplayManager.create();
		ResourceLoader loader = new ResourceLoader();
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		List<WaterTile> waters = new ArrayList<WaterTile>();
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		List<Light> lights = new ArrayList<Light>();
		
		//****** TERRAIN TEXTURE PACK ******\\
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendmaps/blendMap"));
		//**********************************\\
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
		terrains.add(terrain);
		
		ModelData rocksModelData = OBJFileLoader.loadOBJ("rocks");
		Model rocksModel = loader.loadModelFromData(rocksModelData);
		TexturedModel rocks = new TexturedModel(rocksModel, new ModelTexture(loader.loadTexture("models/rocks")));
		
		ModelData fernModelData = OBJFileLoader.loadOBJ("fern");
		Model fernModel = loader.loadModelFromData(fernModelData);
		TexturedModel fern = new TexturedModel(fernModel, new ModelTexture(loader.loadTexture("atlases/fern")));
		fern.getTexture().setNumberOfRows(2);
		fern.getTexture().setHasTransparency();
		
		ModelData bobbleModelData = OBJFileLoader.loadOBJ("pine");
		Model bobbleModel = loader.loadModelFromData(bobbleModelData);
		TexturedModel bobble = new TexturedModel(bobbleModel, new ModelTexture(loader.loadTexture("models/pine")));
		
		ModelData lampModelData = OBJFileLoader.loadOBJ("lamp");
		Model lampModel = loader.loadModelFromData(lampModelData);
		TexturedModel lamp = new TexturedModel(lampModel, new ModelTexture(loader.loadTexture("models/lamp")));
		lamp.getTexture().setUseFakeLighting();
		
		// ***** NORMAL MAP MODELS ***** \\
		ModelData barrelModelData = OBJFileLoader.loadOBJ("barrel");
		Model barrelModel = loader.loadModelFromData(barrelModelData);
		TexturedModel barrel = new TexturedModel(barrelModel, new ModelTexture(loader.loadTexture("models/barrel")));
		barrel.getTexture().setNormalMap(loader.loadTexture("normals/barrel"));
		barrel.getTexture().setShineDamper(10);
		barrel.getTexture().setReflectivity(0.5F);
		
		ModelData crateModelData = OBJFileLoader.loadOBJ("crate");
		Model crateModel = loader.loadModelFromData(crateModelData);
		TexturedModel crate = new TexturedModel(crateModel, new ModelTexture(loader.loadTexture("models/crate")));
		crate.getTexture().setNormalMap(loader.loadTexture("normals/crate"));
		crate.getTexture().setShineDamper(10);
		crate.getTexture().setReflectivity(0.5F);
		
		ModelData boulderModelData = OBJFileLoader.loadOBJ("boulder");
		Model boulderModel = loader.loadModelFromData(boulderModelData);
		TexturedModel boulder = new TexturedModel(boulderModel, new ModelTexture(loader.loadTexture("models/boulder")));
		boulder.getTexture().setNormalMap(loader.loadTexture("normals/boulder"));
		boulder.getTexture().setShineDamper(10);
		boulder.getTexture().setReflectivity(0.5F);
		
		normalMapEntities.add(new Entity(barrel, new Vector3f(75, 10, -75), 0, 0, 0, 1));
		normalMapEntities.add(new Entity(crate, new Vector3f(85, 10, -75), 0, 0, 0, 4));
		normalMapEntities.add(new Entity(boulder, new Vector3f(65, 10, -75), 0, 0, 0, 1));
		
		Random random = new Random(5666778);
		for(int i = 0; i < 60; i++) {
			if(i % 3 == 0) {
				float x = random.nextFloat() * 150;
				float z = random.nextFloat() * -150;
				if((x > 50 && x < 100) || (z < -50 && z > -100)) {
				} else {
					float y = terrain.getHeightOfTerrain(x, z);
					entities.add(new Entity(fern, 3, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9F));
				}
			}
			if(i % 2 == 0) {
				float x = random.nextFloat() * 150;
				float z = random.nextFloat() * -150;
				if((x > 50 && x < 100) || (z < -50 && z > -100)) {
				} else {
					float y = terrain.getHeightOfTerrain(x, z);
					entities.add(new Entity(bobble, 1, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.6F + 0.8F));
				}
			}
		}
		entities.add(new Entity(rocks, new Vector3f(75, 4.6F, -75), 0, 0, 0, 75));
		
		Light sun = new Light(new Vector3f(10000, 10000, -10000), new Vector3f(1.3F, 1.3F, 1.3F));
		lights.add(sun);
		
		WaterFramebuffers buffers = new WaterFramebuffers();
		WaterTile water = new WaterTile(75, -75, 0);
		waters.add(water);
		
		MasterRenderer renderer = new MasterRenderer(loader, sun, buffers);
		
		ModelData playerModelData = OBJFileLoader.loadOBJ("player");
		Model playerModel = loader.loadModelFromData(playerModelData);
		TexturedModel playerM = new TexturedModel(playerModel, new ModelTexture(loader.loadTexture("models/playerMale")));
		Player player = new Player(playerM, new Vector3f(75, 5, -75), 0, 100, 0,  1);
		entities.add(player);
		
		Camera camera = new Camera(player);
		
		MousePicker picker = new MousePicker(renderer.getProjectionMatrix(), camera, terrain);
		
		while(!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();
			picker.update();
			
			glEnable(GL_CLIP_DISTANCE0);
			buffers.bindReflectionFramebuffer();
			float distance = 2 * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.render(terrains, entities, normalMapEntities, guis, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1));
			camera.getPosition().y += distance;
			camera.invertPitch();
			
			buffers.bindRefractionFramebuffer();
			renderer.render(terrains, entities, normalMapEntities, guis, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));
			glDisable(GL_CLIP_DISTANCE0);
			buffers.unbindCurrentFramebuffer();
		
			renderer.render(terrains, entities, normalMapEntities, guis, lights, camera, new Vector4f(0, -1, 0, 100000));
			renderer.postRender(waters, camera);
			DisplayManager.update();
		}
		buffers.clean();
		renderer.clean();
		loader.clean();
		DisplayManager.close();
	}
}