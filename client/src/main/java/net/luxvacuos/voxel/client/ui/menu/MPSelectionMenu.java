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

package net.luxvacuos.voxel.client.ui.menu;

import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;

public class MPSelectionMenu {

	private float xScale, yScale;

	private Button exitButton;
	private Button playButton;
	private String ip = "";

	public MPSelectionMenu(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		exitButton = new Button(new Vector2f(655, 30), new Vector2f(215, 80), xScale, yScale);
		playButton = new Button(new Vector2f(410, 30), new Vector2f(215, 80), xScale, yScale);
	}

	public void render() {
		VectorsRendering.renderWindow("Multiplayer", "Roboto-Bold", 20 * xScale, 20 * yScale, 1240 * xScale,
				540 * yScale);
		VectorsRendering.renderText("IP:  ", "Roboto-Regular", 280 * xScale, 279 * yScale, 60 * yScale,
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
		VectorsRendering.renderWindow(20 * xScale, 570 * yScale, 1240 * xScale, 130 * yScale);
		while (Keyboard.next())
			ip = Keyboard.keyWritten(ip);
		VectorsRendering.renderSearchBox(ip, "Roboto-Regular", "Entypo", 340 * xScale, 260 * yScale, 600 * xScale,
				40 * yScale);

		exitButton.render("Back");
		playButton.render("Play");
	}

	public Button getExitButton() {
		return exitButton;
	}

	public Button getPlayButton() {
		return playButton;
	}

	public String getIP() {
		return ip;
	}

}
