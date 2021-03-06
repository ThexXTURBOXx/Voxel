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

package net.luxvacuos.voxel.client.core;

import net.luxvacuos.voxel.client.core.states.AboutState;
import net.luxvacuos.voxel.client.core.states.GameSPInventoryState;
import net.luxvacuos.voxel.client.core.states.GameSPState;
import net.luxvacuos.voxel.client.core.states.InPauseState;
import net.luxvacuos.voxel.client.core.states.LoadingMPState;
import net.luxvacuos.voxel.client.core.states.LoadingSPState;
import net.luxvacuos.voxel.client.core.states.MPSelectionState;
import net.luxvacuos.voxel.client.core.states.MainMenuState;
import net.luxvacuos.voxel.client.core.states.OptionsState;
import net.luxvacuos.voxel.client.core.states.WorldSelectionState;
import net.luxvacuos.voxel.universal.api.mod.ModStateLoop;

/**
 * States Handler
 * 
 * @author danirod
 * @category Kernel
 */
public class GlobalStates {

	private GameState state;

	private GameState oldState;

	private InternalState internalState;

	public enum GameState {
		GAME_SP(new GameSPState()), MAINMENU(new MainMenuState()), IN_PAUSE(new InPauseState()), LOADING_WORLD(
				new LoadingSPState()), OPTIONS(new OptionsState()), WORLD_SELECTION(new WorldSelectionState()), ABOUT(
						new AboutState()), MP_SELECTION(new MPSelectionState()), LOADING_MP_WORLD(
								new LoadingMPState()), GAME_SP_INVENTORY(new GameSPInventoryState());

		GameState(State state) {
			this.state = state;
		}

		State state;
	}

	public enum InternalState {
		STOPPED, RUNNIG;
	}

	public GlobalStates() {
		internalState = InternalState.STOPPED;
		state = VoxelVariables.autostart ? GameState.LOADING_WORLD : GameState.MAINMENU;
	}

	public void doUpdate(Voxel voxel, float delta) throws Exception {
		state.state.update(voxel, delta);
		for (ModStateLoop modStateLoop : voxel.getApi().getMoltenAPI().getModStateLoops()) {
			modStateLoop.update(voxel, delta);
		}
		if (voxel.getGameResources().getDisplay().isCloseRequested())
			internalState = InternalState.STOPPED;
	}

	public void doRender(Voxel voxel, float alpha) {
		state.state.render(voxel, alpha);
		for (ModStateLoop modStateLoop : voxel.getApi().getMoltenAPI().getModStateLoops()) {
			modStateLoop.render(voxel, alpha);
		}
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		if (!state.equals(this.state)) {
			this.oldState = this.state;
			this.state = state;
		}
	}

	public GameState getOldState() {
		return oldState;
	}

	public void setInternalState(InternalState internalState) {
		this.internalState = internalState;
	}

	public InternalState getInternalState() {
		return internalState;
	}
}
