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

package net.luxvacuos.voxel.client.rendering.api.opengl;

import org.lwjgl.opengl.GL11;

public class GLUtil {

	private GLUtil() {
	}

	public static int getTextureMaxSize() {
		return GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
	}

	public static String getString(int name) {
		String res = "null";
		try {
			res = GL11.glGetString(name);
		} catch (Exception e) {
		}
		return res;
	}

}
