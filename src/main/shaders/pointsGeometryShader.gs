#version 330

layout (points) in;
layout (points, max_vertices=1) out;

in vec3 GSNormal[];
in float GSPicked[];

out float Picked;

uniform mat4 projection;

void main(void)
{
    int i;
    for (i = 0; i < gl_in.length(); i++)
    {
        gl_Position = projection * (gl_in[i].gl_Position + vec4(GSNormal[i],0.0) * 0.005);
        Picked = GSPicked[i];
        EmitVertex();
    }
    EndPrimitive();
}