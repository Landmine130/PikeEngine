//
//  Shader.fsh
//  Game
//
//  Created by Landon on 11/26/12.
//  Copyright (c) 2012 Landon. All rights reserved.
//

#version 150 core

uniform sampler2D Texture;

in lowp vec4 colorPass;
in mediump vec2 textureCoordinatePass;

out lowp vec4 colorOut;

void main()
{
    colorOut = colorPass * texture(Texture, textureCoordinatePass);
}
