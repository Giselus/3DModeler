#version 330

layout (triangles) in;
layout (triangle_strip, max_vertices=3) out;

in vec3 GSFragmentPosition[];
in vec3 GSNormal[];
in float GSPicked[];

out float Picked;
out vec3 Normal;
out vec3 FragmentPosition;

void main(void)
{
    int i;
    int count = 0;

    for(i = 0; i < gl_in.length(); i++){
        if(GSPicked[i] > 0.0)
            count++;
    }
    for(i = 0; i < gl_in.length(); i++){
        if(count >= gl_in.length())
            Picked = 1.0;
        else
            Picked = -1.0;
        gl_Position = gl_in[i].gl_Position;
        Normal = GSNormal[i];
        FragmentPosition = GSFragmentPosition[i];
        EmitVertex();
    }
    EndPrimitive();
}