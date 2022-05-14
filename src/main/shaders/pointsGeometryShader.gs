#version 330

layout (points) in;
layout (points, max_vertices=1) out;

in VS_OUT{
    vec3 normal;
} gs_in[];
in float Picked[];

out float picked;

uniform mat4 projection;

void main(void)
{
    int i;
    for (i = 0; i < gl_in.length(); i++)
    {
        gl_Position = projection * (gl_in[i].gl_Position + vec4(gs_in[i].normal,0.0) * 0.005);
        picked = Picked[i];
        EmitVertex();
    }
    EndPrimitive();
}