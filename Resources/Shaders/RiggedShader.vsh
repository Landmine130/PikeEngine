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
uniform mat4 joints[64];

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
	vec3 animatedNormal = normalize(bones[boneIndexes.x] * bindNormal + bones[boneIndexes.y] * bindNormal + bones[boneIndexes.z] * bindNormal + bones[boneIndexes.w] * bindNormal);
    vec3 eyeNormal = normalize(normalMatrix * animatedNormal);
    vec3 lightPosition = vec3(0.0, 1.0, 0.0);
    vec4 diffuseColor = vec4(1, 1, 1, 1.0);
    	
    float nDotVP = max(1, dot(eyeNormal, normalize(lightPosition)));
    colorPass = diffuseColor;// * nDotVP;
	
	mat4 vertexPosition = bones[boneIndexes.x] * weight1 * biases.x + bones[boneIndexes.y] * weight2 * biases.y + bones[boneIndexes.z] * weight3 * biases.z + bones[boneIndexes.w] * weight4 * biases.w;
    gl_Position = modelViewProjectionMatrix * vertexPosition;
	
	textureCoordinatePass = textureCoordinateIn;
}
