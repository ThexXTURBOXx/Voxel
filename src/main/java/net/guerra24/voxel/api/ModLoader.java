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

package net.guerra24.voxel.api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.guerra24.voxel.util.Logger;

public class ModLoader {

	public ModLoader() {
	}

	public void loadMods() {
		try {
			Files.walk(Paths.get("assets/mods")).forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					try {
						if (filePath.toFile().getAbsolutePath().endsWith(".jar")) {
							URLClassLoader child = new URLClassLoader(new URL[] { filePath.toFile().toURI().toURL() },
									this.getClass().getClassLoader());
							String name = filePath.getFileName().toString();
							name = name.substring(0, name.lastIndexOf('.'));
							Class<?> classToLoad = Class.forName("mod_" + name, true, child);
							Method method = classToLoad.getDeclaredMethod("loadMod");
							Object instance = classToLoad.newInstance();
							method.invoke(instance);
						}
					} catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | SecurityException
							| InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						Logger.error("Error Loading Mod");
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			Logger.error("Invalid Mod File: " + e.getMessage());
		}
	}
}