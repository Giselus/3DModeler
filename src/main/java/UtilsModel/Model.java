package UtilsModel;

import java.util.ArrayList;

public class Model {
    private ArrayList<VertexPosition> Vertices;
    private ArrayList<Face> Faces;

    private UtilsModel.IMesh Mesh;

    public Model(ArrayList<VertexPosition> Vertices, ArrayList<Face> Faces){
        this.Vertices = Vertices;
        this.Faces = Faces;
        //TODO: make this dependency injection
        Mesh = new Mesh(Faces);
    }

    public void Draw(){
        Mesh.draw();
    }
}
