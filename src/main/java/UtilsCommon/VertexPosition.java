package UtilsCommon;

import org.joml.Vector3f;

import java.util.ArrayList;

public class VertexPosition {
    private Vector3f Value;
    private ArrayList<VertexInstance> Indices;
    public VertexPosition(Vector3f Value, ArrayList<VertexInstance> Indices){
        this.Value = Value;
        this.Indices = Indices;
    }

    public Vector3f getValue(){
        return Value;
    }
}
