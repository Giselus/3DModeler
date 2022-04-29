package UtilsCommon;

import org.joml.Vector3f;

public class NormalVector {
    private Vector3f Value;
    public NormalVector(Vector3f Value){
        this.Value = Value;
    }
    public NormalVector(VertexPosition v1, VertexPosition v2, VertexPosition v3){
        Vector3f u1 = new Vector3f(v2.getValue());
        Vector3f u2 = new Vector3f(v3.getValue());
        u1.sub(v1.getValue());
        u2.sub(v1.getValue());

        u1.cross(u2);
        this.Value = u1;
    }

    public Vector3f getValue(){
        return Value;
    }
}
