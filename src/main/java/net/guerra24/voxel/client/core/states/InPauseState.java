package net.guerra24.voxel.client.core.states;

import net.guerra24.voxel.client.core.GlobalStates;
import net.guerra24.voxel.client.core.GlobalStates.GameState;
import net.guerra24.voxel.client.core.State;
import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.menu.PauseMenu;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.world.WorldsHandler;
import net.guerra24.voxel.universal.util.vector.Vector3f;

/**
 * In Pause State
 * 
 * @author danirod
 * @category Kernel
 */
public class InPauseState implements State {

	private boolean switchToMainMenu = false;

	private PauseMenu pauseMenu;

	public InPauseState() {
		pauseMenu = new PauseMenu();
	}

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();

		if (pauseMenu.getBackToMain().pressed()) {
			switchToMainMenu = true;
			voxel.getWorldsHandler().getActiveWorld().clearDimension(gm);
			if (gm.getRand().nextBoolean())
				gm.getSoundSystem().play("menu1");
			else
				gm.getSoundSystem().play("menu2");
			gm.getCamera().setPosition(new Vector3f(0, 0, 1));
			gm.getCamera().setPitch(0);
			gm.getCamera().setYaw(0);
			states.state = GameState.MAINMENU;
		}
	}

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		WorldsHandler worlds = voxel.getWorldsHandler();
		if (switchToMainMenu) {
			gm.getMenuSystem().mainMenu.load(gm);
			switchToMainMenu = false;
		}
		worlds.getActiveWorld().lighting();
		gm.getFrustum().calculateFrustum(gm.getRenderer().getProjectionMatrix(), gm.getCamera());
		if (VoxelVariables.useShadows) {
			gm.getMasterShadowRenderer().being();
			gm.getRenderer().prepare();
			worlds.getActiveWorld().updateChunksShadow(gm);
			gm.getMasterShadowRenderer().end();
		}
		gm.getPostProcessing().getPost_fbo().begin();
		gm.getRenderer().prepare();
		gm.getRenderer().begin(gm);
		worlds.getActiveWorld().updateChunksRender(gm);
		gm.getRenderer().end(gm);
		gm.getSkyboxRenderer().render(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, delta, gm);
		gm.getRenderer().renderEntity(gm.getPhysics().getMobManager().getMobs(), gm);
		gm.getPostProcessing().getPost_fbo().end();
		gm.getRenderer().prepare();
		gm.getPostProcessing().render(gm);
		gm.getGuiRenderer().renderGui(gm.guis4);
	}

}
