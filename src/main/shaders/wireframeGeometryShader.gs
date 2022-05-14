#version 330

layout (triangles) in;
layout (line_strip, max_vertices=3) out;

in VS_OUT{
    vec3 normal;
} gs_in[];

uniform mat4 projection;

void main(void)
{
    int i;
    for (i = 0; i < gl_in.length(); i++)
    {
        gl_Position = projection * (gl_in[i].gl_Position + vec4(gs_in[i].normal,0.0) * 0.003);
        EmitVertex();
    }
    EndPrimitive();
}