package UtilsCommon;

import UtilsModel.VertexPosition;
import org.joml.Vector3f;

public class NormalVector {
    private final Vector3f value;
    public NormalVector(Vector3f value){
        this.value = value;
    }
    public NormalVector(VertexPosition v1, VertexPosition v2, VertexPosition v3){
        Vector3f u1 = new Vector3f(v2.getValue());
        Vector3f u2 = new Vector3f(v3.getValue());
        u1.sub(v1.getValue());
        u2.sub(v1.getValue());

        u1.cross(u2);
        u1.normalize();
        this.value = u1;
    }
    //TODO recalculate instead of creating new instance
    public Vector3f getValue(){
        return new Vector3f(value);
    }
}
