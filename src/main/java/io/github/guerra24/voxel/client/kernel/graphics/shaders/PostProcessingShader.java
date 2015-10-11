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

package io.github.guerra24.voxel.client.kernel.graphics.shaders;

import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.util.vector.Matrix4f;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector2f;

/**
 * Post Processing Shader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class PostProcessingShader extends ShaderProgram {

	/**
	 * Post Processing Shadaer Data
	 */
	private int loc_transformationMatrix;
	private int loc_resolution;
	private int loc_texture0;
	private int loc_depth0;

	/**
	 * Constructor
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public PostProcessingShader() {
		super(KernelConstants.VERTEX_FILE_POST, KernelConstants.FRAGMENT_FILE_POST);
	}

	@Override
	protected void getAllUniformLocations() {
		loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
		loc_resolution = super.getUniformLocation("resolution");
		loc_texture0 = super.getUniformLocation("texture0");
		loc_depth0 = super.getUniformLocation("depth0");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	/**
	 * Loads Textures ID
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void connectTextureUnits() {
		super.loadInt(loc_texture0, 0);
		super.loadInt(loc_depth0, 1);
	}

	/**
	 * Load Display Resolution
	 * 
	 * @param res
	 *            Resolution as Vector2f
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadResolution(Vector2f res) {
		super.load2DVector(loc_resolution, res);
	}

	/**
	 * Loads Transformation Matrix to the shader
	 * 
	 * @param matrix
	 *            Transformation Matrix
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(loc_transformationMatrix, matrix);
	}

}
