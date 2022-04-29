package UtilsCommon;

import java.util.ArrayList;

public class Face {
    //TODO: make this private
    public ArrayList<VertexInstance> Vertices;

    public Face(ArrayList<VertexPosition> Vertices, NormalVector Normal){
        for(VertexPosition vertex: Vertices){
            this.Vertices.add(new VertexInstance(vertex, this, Normal));
        }
    }

    public Face(ArrayList<VertexPosition> Vertices){
        NormalVector Normal = new NormalVector(Vertices.get(0),Vertices.get(1),Vertices.get(2));
        for(VertexPosition vertex: Vertices){
            this.Vertices.add(new VertexInstance(vertex, this, Normal));
        }
    }

    private void recalculateNormals(){
        NormalVector Normal = new NormalVector(Vertices.get(0).getPosition(),Vertices.get(1).getPosition()
                ,Vertices.get(2).getPosition());
        for(VertexInstance vertex: Vertices){
            vertex.SetNormal(Normal);
        }
    }
}
