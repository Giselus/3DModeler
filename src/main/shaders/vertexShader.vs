#version 330
layout (location=0) in vec3 position;
layout (location=1) in vec3 normals;

out vec3 FragmentPosition;
out vec3 Normals;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    FragmentPosition = vec3(model * vec4(position,1.0));
    Normals = mat3(transpose(inverse(model))) * normals;
    gl_Position = projection * view * vec4(FragmentPosition, 1.0);
}