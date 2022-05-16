package UtilsCommon;

import java.util.ArrayList;

public class Model {
    private ArrayList<VertexPosition> Vertices;
    private ArrayList<Face> Faces;

    private Mesh Mesh;

    public Model(ArrayList<VertexPosition> Vertices, ArrayList<Face> Faces){
        this.Vertices = Vertices;
        this.Faces = Faces;

        Mesh = new Mesh(Faces);
    }

    public void Draw(){
        Mesh.draw();
    }
}
