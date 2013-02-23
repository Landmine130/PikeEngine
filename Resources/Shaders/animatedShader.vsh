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
uniform mat4 bones[64];

in vec4 weight1;
in int boneIndex1;
in float bias1;
in vec4 weight2;
in int boneIndex2;
in float bias2;
in vec4 weight3;
in int boneIndex3;
in float bias3;
in vec4 weight4;
in int boneIndex4;
in float bias4;

in vec3 bindNormal;
in mediump vec2 textureCoordinateIn;

out lowp vec4 colorPass;
out mediump vec2 textureCoordinatePass;

void main()
{
	vec3 animatedNormal = normalize(bones[boneIndex1] * bindNormal + bones[boneIndex2] * bindNormal + bones[boneIndex3] * bindNormal + bones[boneIndex4] * bindNormal);
    vec3 eyeNormal = normalize(normalMatrix * animatedNormal);
    vec3 lightPosition = vec3(0.0, 1.0, 0.0);
    vec4 diffuseColor = vec4(1, 1, 1, 1.0);
    	
    float nDotVP = max(1, dot(eyeNormal, normalize(lightPosition)));
    colorPass = diffuseColor * nDotVP;
	
	mat4 vertexPosition = bones[boneIndex1] * weight1 * bias1 + bones[boneIndex2] * weight2 * bias2 + bones[boneIndex3] * weight3 * bias3 + bones[boneIndex4] * weight4 * bias4;
    gl_Position = modelViewProjectionMatrix * vertexPosition;
	
	textureCoordinatePass = textureCoordinateIn;
}
