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

package net.luxvacuos.voxel.client.world;

import java.util.HashMap;
import java.util.Map;

import net.luxvacuos.voxel.client.util.Logger;

public class WorldsHandler {

	private Map<String, IWorld> worlds;
	private IWorld activeWorld;

	public WorldsHandler() {
		init();
	}

	public void init() {
		worlds = new HashMap<String, IWorld>();
	}

	public void registerWorld(String name, IWorld world) {
		worlds.put(name, world);
		Logger.log("The World type " + name + " has been successfully registered");
	}

	public void setActiveWorld(String name) {
		activeWorld = worlds.get(name);
	}

	public IWorld getActiveWorld() {
		return activeWorld;
	}

	public void dispose() {
		worlds.clear();
	}

}