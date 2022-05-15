#version 330
layout (location=0) in vec3 position;
layout (location=1) in vec3 normals;
layout (location=2) in float picked;

out vec3 GSFragmentPosition;
out vec3 GSNormal;
out float GSPicked;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    GSFragmentPosition = vec3(model * vec4(position,1.0));
    GSNormal = mat3(transpose(inverse(model))) * normals;
    GSPicked = picked;
    gl_Position = projection * view * vec4(GSFragmentPosition, 1.0);
}

