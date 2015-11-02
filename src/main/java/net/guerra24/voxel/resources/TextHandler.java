package net.guerra24.voxel.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.core.VoxelVariables;
import net.guerra24.voxel.graphics.TextMasterRenderer;
import net.guerra24.voxel.resources.models.FontType;
import net.guerra24.voxel.resources.models.GUIText;
import net.guerra24.voxel.util.vector.Vector2f;

public class TextHandler {

	private FontType font;

	private List<GUIText> mainMenuText;
	private List<GUIText> gameSPText;

	public TextHandler(Loader loader) {
		font = new FontType(loader.loadTextureFont("tahoma"), new File("assets/fonts/tahoma.fnt"));
		mainMenuText = new ArrayList<GUIText>();
		gameSPText = new ArrayList<GUIText>();
		initMainMenuText();
		initGAMESP();
	}

	private void initMainMenuText() {
		GUIText textVersion = new GUIText(
				"Voxel " + VoxelVariables.version + " " + VoxelVariables.state + " Build " + VoxelVariables.build, 1,
				font, new Vector2f(0.002f, 0.97f), 1, false);
		textVersion.setColour(0.79f, 0.79f, 0.79f);
		GUIText textVersionApi = new GUIText("Voxel API " + VoxelVariables.apiVersion, 1, font,
				new Vector2f(0.002f, 0.94f), 1, false);
		textVersionApi.setColour(0.79f, 0.79f, 0.79f);
		GUIText macWarning = new GUIText("Voxel is running in OS X, some things do not work well", 1, font,
				new Vector2f(0, 0.2f), 1, true);
		macWarning.setColour(1, 0, 0);
		mainMenuText.add(textVersion);
		mainMenuText.add(textVersionApi);
		if (VoxelVariables.runningOnMac)
			mainMenuText.add(macWarning);
	}

	private void initGAMESP() {
		GUIText textVersion = new GUIText(
				"Voxel " + VoxelVariables.version + " " + VoxelVariables.state + " Build " + VoxelVariables.build, 1,
				font, new Vector2f(0.002f, 0.97f), 1, false);
		textVersion.setColour(0.79f, 0.79f, 0.79f);
		gameSPText.add(textVersion);
	}

	public void remove(List<GUIText> source, TextMasterRenderer textMasterRenderer) {
		for (GUIText text : source) {
			text.remove(textMasterRenderer);
		}
	}

	public void add(List<GUIText> source, TextMasterRenderer textMasterRenderer) {
		for (GUIText text : source) {
			text.add(textMasterRenderer);
		}
	}

	public List<GUIText> getMainMenuText() {
		return mainMenuText;
	}

	public List<GUIText> getGameSPText() {
		return gameSPText;
	}
}