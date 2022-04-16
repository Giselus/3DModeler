#version 330
out vec4 fragColor;

struct Material{
    uniform sampler2D texture_diffuse1;
    uniform sampler2D texture_diffuse2;
    uniform sampler2D texture_diffuse3;
    uniform sampler2D texture_specular1;
    uniform sampler2D texture_specular2;
    uniform sampler2D texture_specular3;
}

void main()
{
    fragColor = vec4(0.0, 0.5, 0.5, 1.0);
}