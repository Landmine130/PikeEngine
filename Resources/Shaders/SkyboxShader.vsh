//
//  Shader.vsh
//  Game/Users/landon/Documents/Eclipse/Game/Resources/Shaders/shader.vsh
//
//  Created by Landon on 11/26/12.
//  Copyright (c) 2012 Landon. All rights reserved.
//

#version 150 core

uniform lowp mat4 modelViewProjectionMatrix;

in vec4 position;
in mediump vec2 textureCoordinateIn;

out mediump vec2 textureCoordinatePass;

void main()
{        
    gl_Position = modelViewProjectionMatrix * position;
	
	textureCoordinatePass = textureCoordinateIn;
}
