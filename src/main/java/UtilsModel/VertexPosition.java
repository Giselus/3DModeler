package UtilsModel;

import org.joml.Vector3f;

public class VertexPosition {
    private final Vector3f value;
    private boolean isPicked = false;
    public VertexPosition(Vector3f value){
        this.value = value;
    }
    public Vector3f getValue(){
        return new Vector3f(value);
    }
    public void setValue(Vector3f value) {
        this.value.x = value.x;
        this.value.y = value.y;
        this.value.z = value.z;
    }
    public void pick(){
        isPicked = true;
    }
    public boolean isPicked(){
        return isPicked;
    }
}
