#version 330
out vec4 fragColor;

in float Picked;

void main(){
    if(Picked > 0.0){
        fragColor = vec4(1.0,0.5,0.0,1.0);
    }else{
        fragColor = vec4(0.0,0.0,0.0,1.0);
    }
}