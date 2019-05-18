package com.astreanlegends.engine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.astreanlegends.engine.entity.Camera;
import com.astreanlegends.engine.entity.Entity;
import com.astreanlegends.engine.entity.Player;
import com.astreanlegends.engine.graphics.DisplayManager;
import com.astreanlegends.engine.graphics.OBJFileLoader;
import com.astreanlegends.engine.graphics.ResourceLoader;
import com.astreanlegends.engine.graphics.font.Font;
import com.astreanlegends.engine.graphics.font.FontProperties;
import com.astreanlegends.engine.graphics.gui.GuiText;
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
		List<GuiText> texts = new ArrayList<GuiText>();
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
		
		Font font = new Font(loader.loadFontTexture("candara"), new File("res/fonts/candara.fnt"), new FontProperties(0.6F, 0.1F, 0.7F, 0.2F, new Vector2f(0, 0), new Vector3f(1, 1, 1)));
		GuiText controls = new GuiText("Controls:", font, 2, new Vector2f(0.89F, 0.0F), 1F, false);
//		controls.setFontColor(0.14F, 0.35F, 0.69F);
		texts.add(controls);
		
		GuiText f3 = new GuiText("F3: First-person", font, 1.2F, new Vector2f(0.89F, 0.05F), 1F, false);
		texts.add(f3);
		
		GuiText f2 = new GuiText("F2: Third-person", font, 1.2F, new Vector2f(0.89F, 0.08F), 1F, false);
		texts.add(f2);
		
		Font fontProp = new Font(loader.loadFontTexture("candara"), new File("res/fonts/candara.fnt"), new FontProperties(0.5F, 0.1F, 0.5F, 0.5F, new Vector2f(0, 0), new Vector3f(1, 1, 1)));
		GuiText text = new GuiText(DisplayManager.TITLE, fontProp, 1.5F, new Vector2f(0.01F, 0.02F), 1F, false);
		text.setFontColor(0.57F, 0.19F, 0.64F);
		texts.add(text);
	
		GuiTexture gui = new GuiTexture(loader.loadTexture("gui/health"), new Vector2f(-0.7F, -0.9F), new Vector2f(0.3F, 0.4F));
		guis.add(gui);
		
		while(!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();
			picker.update();
			
			glEnable(GL_CLIP_DISTANCE0);
			buffers.bindReflectionFramebuffer();
			float distance = 2 * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.render(terrains, entities, normalMapEntities, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1));
			camera.getPosition().y += distance;
			camera.invertPitch();
			
			buffers.bindRefractionFramebuffer();
			renderer.render(terrains, entities, normalMapEntities, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));
			glDisable(GL_CLIP_DISTANCE0);
			buffers.unbindCurrentFramebuffer();
		
			renderer.render(terrains, entities, normalMapEntities, lights, camera, new Vector4f(0, -1, 0, 100000));
			renderer.postRender(waters, camera);
			renderer.postRender(guis, texts);
			DisplayManager.update();
		}
		buffers.clean();
		renderer.clean();
		loader.clean();
		DisplayManager.close();
	}
}