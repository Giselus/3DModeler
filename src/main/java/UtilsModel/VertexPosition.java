package UtilsModel;

import org.joml.Vector3f;

import java.util.Random;

public class VertexPosition {
    private final Vector3f Value;
    private boolean isPicked = false;
    public VertexPosition(Vector3f Value){
        this.Value = Value;
    }
    public Vector3f getValue(){
        return new Vector3f(Value);
    }
    public void setValue(Vector3f Value) {
        this.Value.x = Value.x;
        this.Value.y = Value.y;
        this.Value.z = Value.z;
    }
    public void pick(){
        isPicked = true;
    }
    public boolean isPicked(){
        return isPicked;
    }
}
