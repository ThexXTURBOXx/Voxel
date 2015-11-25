/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.guerra24.voxel.client.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.esotericsoftware.kryo.Kryo;

import net.guerra24.voxel.client.core.GlobalStates;
import net.guerra24.voxel.client.graphics.Frustum;
import net.guerra24.voxel.client.graphics.GuiRenderer;
import net.guerra24.voxel.client.graphics.MasterRenderer;
import net.guerra24.voxel.client.graphics.MasterShadowRenderer;
import net.guerra24.voxel.client.graphics.OcclusionRenderer;
import net.guerra24.voxel.client.graphics.PostProcessingRenderer;
import net.guerra24.voxel.client.graphics.SkyboxRenderer;
import net.guerra24.voxel.client.graphics.TextMasterRenderer;
import net.guerra24.voxel.client.menu.Menu;
import net.guerra24.voxel.client.resources.models.GuiTexture;
import net.guerra24.voxel.client.sound.LibraryLWJGLOpenAL;
import net.guerra24.voxel.client.sound.soundsystem.SoundSystem;
import net.guerra24.voxel.client.sound.soundsystem.SoundSystemConfig;
import net.guerra24.voxel.client.sound.soundsystem.SoundSystemException;
import net.guerra24.voxel.client.sound.soundsystem.codecs.CodecJOgg;
import net.guerra24.voxel.client.util.Logger;
import net.guerra24.voxel.client.world.Physics;
import net.guerra24.voxel.client.world.block.Block;
import net.guerra24.voxel.client.world.entities.Camera;
import net.guerra24.voxel.client.world.entities.Entity;
import net.guerra24.voxel.client.world.entities.Mob;
import net.guerra24.voxel.universal.resources.UniversalResources;
import net.guerra24.voxel.universal.util.vector.Vector3f;

/**
 * Game Resources
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class GameResources {

	/**
	 * GameResources Data
	 */
	public List<GuiTexture> guis2 = new ArrayList<GuiTexture>();
	public List<GuiTexture> guis3 = new ArrayList<GuiTexture>();
	public List<GuiTexture> guis4 = new ArrayList<GuiTexture>();

	private Random rand;
	private Loader loader;
	private Camera camera;
	private Camera sun_Camera;
	private MasterRenderer renderer;
	private SkyboxRenderer skyboxRenderer;
	private GuiRenderer guiRenderer;
	private TextMasterRenderer textMasterRenderer;
	private TextHandler textHandler;
	private GlobalStates globalStates;
	private PostProcessingRenderer postProcessing;
	private MasterShadowRenderer masterShadowRenderer;
	private OcclusionRenderer occlusionRenderer;
	private SoundSystem soundSystem;
	private Frustum frustum;
	private Kryo kryo;
	private Physics physics;
	private Menu menuSystem;

	private Vector3f sunRotation = new Vector3f(0, 0, -30);
	private Vector3f lightPos = new Vector3f(0, 0, 0);

	public Mob player;

	/**
	 * Constructor
	 * 
	 */
	public GameResources() {
	}

	/**
	 * Initialize the Game Objects
	 * 
	 */
	public void init() {
		loader = new Loader();
		rand = new Random();
		sun_Camera = new Camera();
		sun_Camera.setPosition(new Vector3f(0, 0, 0));
		sun_Camera.setYaw(sunRotation.x);
		sun_Camera.setPitch(sunRotation.y);
		sun_Camera.setRoll(sunRotation.z);
		renderer = new MasterRenderer(loader);
		camera = new Camera(renderer.getProjectionMatrix());
		kryo = new Kryo();
		guiRenderer = new GuiRenderer(loader);
		occlusionRenderer = new OcclusionRenderer(renderer.getProjectionMatrix());
		skyboxRenderer = new SkyboxRenderer(loader, renderer.getProjectionMatrix());
		postProcessing = new PostProcessingRenderer(loader, this);
		masterShadowRenderer = new MasterShadowRenderer();
		physics = new Physics(this);
		frustum = new Frustum();
		textMasterRenderer = new TextMasterRenderer(loader);
		textHandler = new TextHandler(this);
		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
		} catch (SoundSystemException e) {
			Logger.error("Unable to bind SoundSystem Libs");
			e.printStackTrace();
		}
		soundSystem = new SoundSystem();
		globalStates = new GlobalStates(loader);
		Block.initBasicBlocks();
		UniversalResources.loadUniversalResources(this);
		MenuResources.load(this);
		menuSystem = new Menu(this);
		loadMusic();
	}

	/**
	 * Load Music
	 * 
	 */
	public void loadMusic() {
		soundSystem.backgroundMusic("menu1", "menu/menu1.ogg", false);
		soundSystem.backgroundMusic("menu2", "menu/menu2.ogg", false);
	}

	/**
	 * Load Resources like Mobs
	 * 
	 */
	public void addRes() {
		player = new Mob(new Entity(UniversalResources.player, new Vector3f(0, 80, 0), 0, 0, 0, 1));
		physics.getMobManager().registerMob(player);
	}

	public void update(float rot) {
		sunRotation.setY(rot);
		sun_Camera.setYaw(sunRotation.x);
		sun_Camera.setPitch(sunRotation.y);
		sun_Camera.setRoll(sunRotation.z);
		lightPos = new Vector3f(1000 * (float) Math.sin(Math.toRadians(sun_Camera.getRoll())),
				1000 * (float) Math.sin(Math.toRadians(sun_Camera.getPitch())),
				1000 * (float) Math.cos(Math.toRadians(sun_Camera.getPitch())));
		Vector3f.add(sun_Camera.getPosition(), lightPos, lightPos);
	}

	/**
	 * Disposes all objects
	 * 
	 */
	public void cleanUp() {
		textMasterRenderer.cleanUp();
		masterShadowRenderer.cleanUp();
		occlusionRenderer.cleanUp();
		postProcessing.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		soundSystem.cleanup();
	}

	public List<GuiTexture> getGuis2() {
		return guis2;
	}

	public List<GuiTexture> getGuis3() {
		return guis3;
	}

	public List<GuiTexture> getGuis4() {
		return guis4;
	}

	public Random getRand() {
		return rand;
	}

	public Loader getLoader() {
		return loader;
	}

	public Kryo getKryo() {
		return kryo;
	}

	public Camera getCamera() {
		return camera;
	}

	public MasterRenderer getRenderer() {
		return renderer;
	}

	public SkyboxRenderer getSkyboxRenderer() {
		return skyboxRenderer;
	}

	public GuiRenderer getGuiRenderer() {
		return guiRenderer;
	}

	public SoundSystem getSoundSystem() {
		return soundSystem;
	}

	public PostProcessingRenderer getPostProcessing() {
		return postProcessing;
	}

	public Frustum getFrustum() {
		return frustum;
	}

	public TextMasterRenderer getTextMasterRenderer() {
		return textMasterRenderer;
	}

	public Physics getPhysics() {
		return physics;
	}

	public TextHandler getTextHandler() {
		return textHandler;
	}

	public GlobalStates getGlobalStates() {
		return globalStates;
	}

	public MasterShadowRenderer getMasterShadowRenderer() {
		return masterShadowRenderer;
	}

	public Camera getSun_Camera() {
		return sun_Camera;
	}

	public Vector3f getLightPos() {
		return lightPos;
	}

	public OcclusionRenderer getOcclusionRenderer() {
		return occlusionRenderer;
	}

	public Menu getMenuSystem() {
		return menuSystem;
	}

	public Vector3f getSunRotation() {
		return sunRotation;
	}

}