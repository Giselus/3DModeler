package UtilsCommon;

import org.joml.Vector3f;

public class VertexPosition {
    private final Vector3f Value;
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
}
