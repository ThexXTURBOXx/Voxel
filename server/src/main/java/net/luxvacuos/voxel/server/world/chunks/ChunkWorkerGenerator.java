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

package net.luxvacuos.voxel.server.world.chunks;

import net.luxvacuos.voxel.server.world.IWorld;

public class ChunkWorkerGenerator implements Runnable {
	private final IWorld world;
	private Chunk chunk;

	public ChunkWorkerGenerator(final IWorld world, Chunk chunk) {
		this.world = world;
		this.chunk = chunk;
	}

	@Override
	public void run() {
		chunk.createBasicTerrain(world);
		chunk.created = true;
		chunk.creating = false;
	}
}