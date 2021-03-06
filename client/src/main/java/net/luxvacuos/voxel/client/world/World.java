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

package net.luxvacuos.voxel.client.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Inventory;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;

public abstract class World {

	protected String name;
	protected Dimension activeDimension;

	private Map<Integer, Dimension> dimensions;
	private File file;

	public World(String name) {
		this.name = name;
		dimensions = new HashMap<>();
		file = new File(VoxelVariables.worldPath + name + "/world.dat");
	}

	public void init(GameResources gm) {
		Logger.log("Loading " + name);
		((PlayerCamera) gm.getCamera()).getInventory().clearInventorty();
		load(gm);
		localInit(gm);
	}

	protected abstract void localInit(GameResources gm);

	private void load(GameResources gm) {
		if (file.exists()) {
			Input input;
			try {
				input = new Input(new FileInputStream(file));
				((PlayerCamera) gm.getCamera()).setInventory(gm.getKryo().readObject(input, Inventory.class));
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void save(GameResources gm) {
		Output output;
		try {
			output = new Output(new FileOutputStream(file));
			gm.getKryo().writeObject(output, ((PlayerCamera) gm.getCamera()).getInventory());
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void addDimension(Dimension dim) {
		dimensions.put(dim.getDimensionID(), dim);
	}

	public Dimension getDimension(int id) {
		return dimensions.get(id);
	}

	public void setActiveDimension(Dimension dim) {
		this.activeDimension = dim;
	}

	public void switchDimension(int id, GameResources gm) {
		activeDimension.getPhysicsEngine().removeEntity(gm.getCamera());
		activeDimension = getDimension(id);
		activeDimension.getPhysicsEngine().addEntity(gm.getCamera());
	}

	public Dimension getActiveDimension() {
		return activeDimension;
	}

	public String getName() {
		return name;
	}

	public void dispose(GameResources gm) throws Exception {
		Logger.log("Saving " + name);
		save(gm);
		for (Dimension dim : dimensions.values()) {
			dim.clearDimension(gm);
		}
		activeDimension.getPhysicsEngine().removeEntity(gm.getCamera());
		((PlayerCamera) gm.getCamera()).getInventory().clearInventorty();
	}

	public Map<Integer, Dimension> getDimensions() {
		return dimensions;
	}
}
