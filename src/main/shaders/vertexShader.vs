#version 330
layout (location=0) in vec3 position;
layout (location=1) in vec3 normals;
layout (location=2) in vec2 textureCoordinates;

out vec3 FragmentPosition;
out vec3 Normals;
out vec2 TextureCoordinates;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    FragmentPosition = vec3(model * vec4(position,1.0));
    Normals = mat3(transpose(inverse(model))) * normals;
    TextureCoordinates = textureCoordinates;
    //gl_Position = vec4(position, 1.0);
    gl_Position = projection * view * model * vec4(FragmentPosition, 1.0);
}