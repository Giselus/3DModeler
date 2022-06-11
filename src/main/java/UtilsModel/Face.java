package UtilsModel;

import UtilsCommon.NormalVector;

import java.util.ArrayList;

public class Face {
    public Face(ArrayList<VertexPosition> vertices, NormalVector normal){
        this.vertices = new ArrayList<>();
        for(VertexPosition vertex: vertices){
            this.vertices.add(new VertexInstance(vertex, this, normal));
        }
    }

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

    public void setVertices(ArrayList<VertexInstance> vertices) {
        this.vertices = vertices;
    }

    public void recalculateNormals(){
        NormalVector normal = new NormalVector(vertices.get(0).getPosition(), vertices.get(1).getPosition()
                , vertices.get(2).getPosition());
        for(VertexInstance vertex: vertices){
            vertex.setNormal(normal);
        }
    }

    private ArrayList<VertexInstance> vertices;
}
