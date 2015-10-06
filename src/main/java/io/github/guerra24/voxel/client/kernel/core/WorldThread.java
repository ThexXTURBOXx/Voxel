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

package io.github.guerra24.voxel.client.kernel.core;

import io.github.guerra24.voxel.client.kernel.api.VAPI;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.world.WorldHandler;

/**
 * World Thread
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Kernel
 */
public class WorldThread extends Thread {
	private GameResources gm;
	private WorldHandler world;
	private VAPI api;

	@Override
	public void run() {
		while (gm.getGameStates().loop) {
			switch (gm.getGameStates().state) {
			case MAINMENU:
				try {
					Thread.sleep((long) 33.3333);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case IN_PAUSE:
				world.getWorld(world.getActiveWorld()).updateChunkGeneration(gm, api);
				break;
			case GAME:
				world.getWorld(world.getActiveWorld()).updateChunkGeneration(gm, api);
				break;
			case LOADING_WORLD:
				try {
					Thread.sleep((long) 33.3333);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	public void setGm(GameResources gm) {
		this.gm = gm;
	}

	public void setWorldHandler(WorldHandler dimensionHandler) {
		this.world = dimensionHandler;
	}

	public void setApi(VAPI api) {
		this.api = api;
	}
}