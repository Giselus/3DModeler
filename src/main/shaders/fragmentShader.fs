#version 330
out vec4 fragColor;

in vec3 FragmentPosition;
in vec3 Normals;

void main()
{
    fragColor = vec4(0.0, 0.5, 0.5, 1.0);
}