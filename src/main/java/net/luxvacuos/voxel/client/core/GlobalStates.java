/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Lux Vacuos
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

package net.luxvacuos.voxel.client.core;

import net.luxvacuos.voxel.client.api.mod.ModStateLoop;
import net.luxvacuos.voxel.client.core.states.AboutState;
import net.luxvacuos.voxel.client.core.states.GameSPState;
import net.luxvacuos.voxel.client.core.states.InPauseState;
import net.luxvacuos.voxel.client.core.states.LoadingSPState;
import net.luxvacuos.voxel.client.core.states.MainMenuState;
import net.luxvacuos.voxel.client.core.states.OptionsState;
import net.luxvacuos.voxel.client.core.states.WorldSelectionState;

/**
 * States Handler
 * 
 * @author danirod
 * @category Kernel
 */
public class GlobalStates {

	public boolean loop = false;

	private GameState state;

	private GameState oldState;

	public enum GameState {
		GAME_SP(new GameSPState()), MAINMENU(new MainMenuState()), IN_PAUSE(new InPauseState()), LOADING_WORLD(
				new LoadingSPState()), OPTIONS(new OptionsState()), WORLD_SELECTION(new WorldSelectionState()), ABOUT(
						new AboutState());

		GameState(State state) {
			this.state = state;
		}

		State state;
	}

	public GlobalStates() {
		loop = true;
		state = VoxelVariables.autostart ? GameState.LOADING_WORLD : GameState.MAINMENU;
	}

	public void doUpdate(Voxel voxel, float delta) {
		state.state.update(voxel, this, delta);
		for (ModStateLoop modStateLoop : voxel.getApi().getMoltenAPI().getModStateLoops()) {
			modStateLoop.update(this.state.state, delta);
		}
		if (voxel.getGameResources().getDisplay().isCloseRequested())
			loop = false;
	}

	public void doRender(Voxel voxel, float alpha) {
		state.state.render(voxel, this, alpha);
		for (ModStateLoop modStateLoop : voxel.getApi().getMoltenAPI().getModStateLoops()) {
			modStateLoop.render(state.state, alpha);
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
}