#version 330

layout (triangles) in;
layout (line_strip, max_vertices=3) out;

in vec3 Normal[];
in float Picked[];

out float picked;

uniform mat4 projection;

void main(void)
{
    int i;
    for (i = 0; i < gl_in.length(); i++)
    {
        gl_Position = projection * (gl_in[i].gl_Position + vec4(Normal[i],0.0) * 0.001);
        picked = Picked[i];
        EmitVertex();
    }
    EndPrimitive();
}