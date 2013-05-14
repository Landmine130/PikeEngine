//
//  Shader.vsh
//  Game/Users/landon/Documents/Eclipse/Game/Resources/Shaders/shader.vsh
//
//  Created by Landon on 11/26/12.
//  Copyright (c) 2012 Landon. All rights reserved.
//

#version 150 core


uniform lowp mat4 modelViewProjectionMatrix;
uniform lowp mat3 normalMatrix;
uniform lowp mat4 joints[64];

in vec4 weight1;
in vec4 weight2;
in vec4 weight3;
in vec4 weight4;

in ivec4 jointIndexes;
in vec4 biases;

in vec3 bindNormal;
in mediump vec2 textureCoordinateIn;

out lowp vec4 colorPass;
out mediump vec2 textureCoordinatePass;

void main()
{
	//vec3 animatedNormal = joints[jointIndexes.x] * vec4(bindNormal, 0.0) * biases.x + joints[jointIndexes.x] * vec4(bindNormal, 0.0) * biases.x + joints[jointIndexes.x] * vec4(bindNormal, 0.0) * biases.x + joints[jointIndexes.x] * vec4(bindNormal, 0.0) * biases.x;
	//vec3 eyeNormal = normalize(normalMatrix * animatedNormal);
	//vec3 lightPosition = vec3(0.0, 1.0, 0.0);
	vec4 diffuseColor = vec4(1, 1, 1, 1);
	
	//float nDotVP = max(1, dot(eyeNormal, normalize(lightPosition)));
	colorPass = diffuseColor;// * nDotVP;
	
	vec4 vertexPosition = joints[jointIndexes.x] * weight1 * biases.x + joints[jointIndexes.y] * weight2 * biases.y + joints[jointIndexes.z] * weight3 * biases.z + joints[jointIndexes.w] * weight4 * biases.w;

	gl_Position = modelViewProjectionMatrix * vec4(vertexPosition.xyz, 1);
	
	textureCoordinatePass = textureCoordinateIn;
}
