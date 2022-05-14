#version 330
layout (location=0) in vec3 position;
layout (location=1) in vec3 normals;
layout (location=2) in float picked;

out VS_OUT{
    vec3 normal;
} vs_out;

out float Picked;

uniform mat4 model;
uniform mat4 view;

void main()
{
    mat3 normalMatrix = mat3(transpose(inverse(view*model)));
    vs_out.normal = vec3(vec4(normalMatrix * normals, 0.0));
    Picked = picked;
    gl_Position = view * model * vec4(position, 1.0);
}