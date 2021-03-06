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

package net.luxvacuos.voxel.client.rendering.api.opengl.shaders;

import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.Camera;

/**
 * Post Processing Shader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class DeferredShadingShader extends ShaderProgram {

	/**
	 * Post Processing Shadaer Data
	 */
	private int loc_transformationMatrix;
	private int loc_projectionMatrix;
	private int loc_viewMatrix;
	private int loc_inverseProjectionMatrix;
	private int loc_inverseViewMatrix;
	private int loc_previousViewMatrix;
	private int loc_sunPositionInScreen;
	private int loc_camUnderWater;
	private int loc_camUnderWaterOffset;
	private int loc_resolution;
	private int loc_gDiffuse;
	private int loc_gPosition;
	private int loc_gNormal;
	private int loc_depth0;
	private int loc_gData0;
	private int loc_gData1;
	private int loc_composite0;
	private int loc_composite1;
	private int loc_cameraPosition;
	private int loc_previousCameraPosition;
	private int loc_lightPosition;
	private int loc_invertedLightPosition;
	private int loc_skyColor;
	private int loc_time;
	private int loc_exposure;

	private int loc_useFXAA;
	private int loc_useDOF;
	private int loc_useMotionBlur;
	private int loc_useVolumetricLight;
	private int loc_useReflections;

	private float time;

	/**
	 * Constructor
	 * @throws Exception 
	 * 
	 */
	public DeferredShadingShader(String num) throws Exception {
		super(VoxelVariables.VERTEX_FILE_COMPOSITE + num + ".glsl",
				VoxelVariables.FRAGMENT_FILE_COMPOSITE + num + ".glsl");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
		loc_viewMatrix = super.getUniformLocation("viewMatrix");
		loc_inverseProjectionMatrix = super.getUniformLocation("inverseProjectionMatrix");
		loc_inverseViewMatrix = super.getUniformLocation("inverseViewMatrix");
		loc_previousViewMatrix = super.getUniformLocation("previousViewMatrix");
		loc_cameraPosition = super.getUniformLocation("cameraPosition");
		loc_previousCameraPosition = super.getUniformLocation("previousCameraPosition");
		loc_camUnderWater = super.getUniformLocation("camUnderWater");
		loc_camUnderWaterOffset = super.getUniformLocation("camUnderWaterOffset");
		loc_resolution = super.getUniformLocation("resolution");
		loc_gDiffuse = super.getUniformLocation("gDiffuse");
		loc_gPosition = super.getUniformLocation("gPosition");
		loc_gNormal = super.getUniformLocation("gNormal");
		loc_depth0 = super.getUniformLocation("gDepth");
		loc_gData0 = super.getUniformLocation("gData0");
		loc_gData1 = super.getUniformLocation("gData1");
		loc_composite0 = super.getUniformLocation("composite0");
		loc_composite1 = super.getUniformLocation("composite1");
		loc_lightPosition = super.getUniformLocation("lightPosition");
		loc_invertedLightPosition = super.getUniformLocation("invertedLightPosition");
		loc_sunPositionInScreen = super.getUniformLocation("sunPositionInScreen");
		loc_skyColor = super.getUniformLocation("skyColor");
		loc_exposure = super.getUniformLocation("exposure");
		loc_time = super.getUniformLocation("time");

		loc_useFXAA = super.getUniformLocation("useFXAA");
		loc_useDOF = super.getUniformLocation("useDOF");
		loc_useMotionBlur = super.getUniformLocation("useMotionBlur");
		loc_useReflections = super.getUniformLocation("useReflections");
		loc_useVolumetricLight = super.getUniformLocation("useVolumetricLight");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	/**
	 * Loads Textures ID
	 * 
	 */
	public void connectTextureUnits() {
		super.loadInt(loc_gDiffuse, 0);
		super.loadInt(loc_gPosition, 1);
		super.loadInt(loc_gNormal, 2);
		super.loadInt(loc_depth0, 3);
		super.loadInt(loc_gData0, 4);
		super.loadInt(loc_gData1, 5);
		super.loadInt(loc_composite0, 6);
		super.loadInt(loc_composite1, 7);
	}

	public void loadUnderWater(boolean value) {
		super.loadBoolean(loc_camUnderWater, value);
		super.loadFloat(loc_camUnderWaterOffset, time += 0.1f);
		time %= 10;
	}

	public void loadExposure(float bright) {
		super.loadFloat(loc_exposure, bright);
	}

	public void loadSkyColor(Vector3f color) {
		super.loadVector(loc_skyColor, color);
	}

	public void loadLightPosition(Vector3f pos, Vector3f invertPos) {
		super.loadVector(loc_lightPosition, pos);
		super.loadVector(loc_invertedLightPosition, invertPos);
	}

	public void loadSunPosition(Vector2f pos) {
		super.load2DVector(loc_sunPositionInScreen, pos);
	}

	public void loadTime(float time) {
		super.loadFloat(loc_time, time);
	}

	/**
	 * Load Display Resolution
	 * 
	 * @param res
	 *            Resolution as Vector2f
	 */
	public void loadResolution(Vector2f res) {
		super.load2DVector(loc_resolution, res);
	}

	public void loadSettings(boolean useDOF, boolean useFXAA, boolean useMotionBlur, boolean useVolumetricLight,
			boolean useReflections) {
		super.loadBoolean(loc_useDOF, useDOF);
		super.loadBoolean(loc_useFXAA, useFXAA);
		super.loadBoolean(loc_useMotionBlur, useMotionBlur);
		super.loadBoolean(loc_useVolumetricLight, useVolumetricLight);
		super.loadBoolean(loc_useReflections, useReflections);
	}

	public void loadMotionBlurData(Matrix4f projectionMatrix, Camera camera, Matrix4f previousViewMatrix,
			Vector3f previousCameraPosition) {
		super.loadMatrix(loc_projectionMatrix, projectionMatrix);
		super.loadMatrix(loc_inverseProjectionMatrix, Matrix4f.invert(projectionMatrix, null));
		super.loadMatrix(loc_inverseViewMatrix, Matrix4f.invert(Maths.createViewMatrix(camera), null));
		super.loadMatrix(loc_previousViewMatrix, previousViewMatrix);
		super.loadVector(loc_cameraPosition, camera.getPosition());
		super.loadVector(loc_previousCameraPosition, previousCameraPosition);
	}

	/**
	 * Loads View Matrix to the shader
	 * 
	 * @param camera
	 *            Camera
	 */
	public void loadviewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(loc_viewMatrix, viewMatrix);
	}

	/**
	 * Loads Transformation Matrix to the shader
	 * 
	 * @param matrix
	 *            Transformation Matrix
	 */
	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(loc_transformationMatrix, matrix);
	}

}
