//
// The MIT License (MIT)
//
// Copyright (c) 2015 Guerra24
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

#version 330 core

/*--------------------------------------------------------*/
/*----------------------FXAA CONFIG-----------------------*/
/*--------------------------------------------------------*/

#define FXAA_SUBPIX_SHIFT (1.0/4.0)

in vec2 position;

out vec2 textureCoords;
out vec4 posPos;

uniform mat4 transformationMatrix;
uniform vec2 resolution;

uniform int useFXAA;

void main(void){

	gl_Position = transformationMatrix * vec4(position, -0.8, 1.0);
	textureCoords = vec2((position.x+1.0)/2.0, (position.y+1.0)/2.0);
	if(useFXAA == 1){
		vec2 rcpFrame = vec2(1.0/resolution.x, 1.0/resolution.y);
		posPos.xy = textureCoords;
		posPos.zw = textureCoords - (rcpFrame * (0.5 + FXAA_SUBPIX_SHIFT));
	}
	
}