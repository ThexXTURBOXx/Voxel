//
// The MIT License (MIT)
//
// Copyright (c) 2015-2016 Guerra24
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

in float visibility;
in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec4 pass_position;
in vec4 ShadowCoord;

out vec4 [5] out_Color;

uniform sampler2D texture0;
uniform sampler2DShadow depth0;
uniform vec3 skyColour;
uniform vec3 lightPosition;

uniform int useShadows;

vec2 poissonDisk[16] = vec2[]( 
   vec2( -0.94201624, -0.39906216 ), 
   vec2( 0.94558609, -0.76890725 ), 
   vec2( -0.094184101, -0.92938870 ), 
   vec2( 0.34495938, 0.29387760 ), 
   vec2( -0.91588581, 0.45771432 ), 
   vec2( -0.81544232, -0.87912464 ), 
   vec2( -0.38277543, 0.27676845 ), 
   vec2( 0.97484398, 0.75648379 ), 
   vec2( 0.44323325, -0.97511554 ), 
   vec2( 0.53742981, -0.47373420 ), 
   vec2( -0.26496911, -0.41893023 ), 
   vec2( 0.79197514, 0.19090188 ), 
   vec2( -0.24188840, 0.99706507 ), 
   vec2( -0.81409955, 0.91437590 ), 
   vec2( 0.19984126, 0.78641367 ), 
   vec2( 0.14383161, -0.14100790 ) 
);

void main(void) {
    vec4 textureColour = texture(texture0, pass_textureCoords);
    float shadow = 0;
	if(useShadows == 1){
		for (int i=0;i<16;i++){
    		if (texture(depth0, vec3(ShadowCoord.xy + poissonDisk[i]/1600.0 , 0.0), 0)  <  ShadowCoord.z){
   		 		shadow += 0.05;
   	 		}
   	 		if(ShadowCoord.z > 1.0){
       			shadow = 0.0;
       			break;
       		}
		}
   	}
    
    if(textureColour.a<0.5) {
		discard;
	}
	
	out_Color[0] = textureColour;
	out_Color[1] = vec4(pass_position.xyz,0);
	out_Color[2] = vec4(surfaceNormal.xyz,0);
	out_Color[3] = vec4(0.0,1.0,0.0,shadow);
	out_Color[4] = vec4(0.0,0.0,0.0,0.0);
}