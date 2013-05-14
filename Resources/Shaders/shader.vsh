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

in vec4 position;
in vec3 normal;
in mediump vec2 textureCoordinateIn;

out lowp vec4 colorPass;
out mediump vec2 textureCoordinatePass;

void main()
{
    //vec3 eyeNormal = normalize(normalMatrix * normal);
    //vec3 lightPosition = vec3(0.0, 1.0, 0.0);
    vec4 diffuseColor = vec4(1, 1, 1, 1.0);
    
    //float nDotVP = max(1, dot(eyeNormal, normalize(lightPosition)));
    colorPass = diffuseColor;// * nDotVP;
    
    gl_Position = modelViewProjectionMatrix * position;
	textureCoordinatePass = textureCoordinateIn;
}
