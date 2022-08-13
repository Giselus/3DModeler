package UtilsModel;

import UtilsCommon.NormalVector;

import java.util.ArrayList;

public class Face {
    public Face(ArrayList<VertexPosition> vertices){
        this.vertices = new ArrayList<>();
        NormalVector normal = new NormalVector(vertices.get(0),vertices.get(1),vertices.get(2));
        for(VertexPosition vertex: vertices){
            this.vertices.add(new VertexInstance(vertex, this, normal));
        }
    }

    public ArrayList<VertexInstance> getVertices() {
        return vertices;
    }

    private final ArrayList<VertexInstance> vertices;
}
