/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.core.states;

import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.core.State;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;

public class WorldSelectionState implements State {

	@Override
	public void update(Voxel voxel, float delta) {
		GameResources gm = voxel.getGameResources();
		gm.getMenuSystem().worldSelectionMenu.update();
		if (gm.getMenuSystem().worldSelectionMenu.getExitButton().pressed())
			gm.getGlobalStates().setState(GameState.MAINMENU);
		else if (gm.getMenuSystem().worldSelectionMenu.getPlayButton().pressed()
				&& gm.getMenuSystem().worldSelectionMenu.getWorldName() != "")
			gm.getGlobalStates().setState(GameState.LOADING_WORLD);
	}

	@Override
	public void render(Voxel voxel, float alpha) {
		GameResources gm = voxel.getGameResources();
		gm.getRenderer().prepare();
		gm.getDisplay().beingNVGFrame();
		gm.getMenuSystem().worldSelectionMenu.render();
		VectorsRendering.renderMouse();
		gm.getDisplay().endNVGFrame();
	}

}
