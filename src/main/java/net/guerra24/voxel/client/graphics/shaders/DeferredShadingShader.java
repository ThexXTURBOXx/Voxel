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

package net.guerra24.voxel.client.graphics.shaders;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.util.Maths;
import net.guerra24.voxel.client.world.entities.Camera;
import net.guerra24.voxel.universal.util.vector.Matrix4f;
import net.guerra24.voxel.universal.util.vector.Vector2f;
import net.guerra24.voxel.universal.util.vector.Vector3f;

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
	private int loc_skyColor;

	private int loc_useFXAA;
	private int loc_useDOF;
	private int loc_useMotionBlur;
	private int loc_useBloom;
	private int loc_useVolumetricLight;

	private float time;

	/**
	 * Constructor
	 * 
	 */
	public DeferredShadingShader(String num) {
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
		loc_lightPosition = super.getUniformLocation("lightPosition");
		loc_composite0 = super.getUniformLocation("composite0");
		loc_composite1 = super.getUniformLocation("composite1");
		loc_sunPositionInScreen = super.getUniformLocation("sunPositionInScreen");
		loc_skyColor = super.getUniformLocation("skyColor");

		loc_useFXAA = super.getUniformLocation("useFXAA");
		loc_useDOF = super.getUniformLocation("useDOF");
		loc_useMotionBlur = super.getUniformLocation("useMotionBlur");
		loc_useBloom = super.getUniformLocation("useBloom");
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

	public void loadSkyColor(Vector3f color) {
		super.loadVector(loc_skyColor, color);
	}

	public void loadLightPosition(Vector3f pos) {
		super.loadVector(loc_lightPosition, pos);
	}

	public void loadSunPosition(Vector2f pos) {
		super.load2DVector(loc_sunPositionInScreen, pos);
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

	public void loadSettings() {
		super.loadBoolean(loc_useDOF, VoxelVariables.useDOF);
		super.loadBoolean(loc_useFXAA, VoxelVariables.useFXAA);
		super.loadBoolean(loc_useMotionBlur, VoxelVariables.useMotionBlur);
		super.loadBoolean(loc_useBloom, VoxelVariables.useBloom);
		super.loadBoolean(loc_useVolumetricLight, VoxelVariables.useVolumetricLight);
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