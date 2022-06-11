package UtilsModel;

import java.util.ArrayList;

public class Model {
    private ArrayList<VertexPosition> vertices;
    private ArrayList<Face> faces;

    private UtilsModel.IMesh mesh;

    public Model(ArrayList<VertexPosition> vertices, ArrayList<Face> faces){
        this.vertices = vertices;
        this.faces = faces;
        //TODO: make this dependency injection
        mesh = new Mesh(faces);
    }

    public void Draw(){
        mesh.draw();
    }
}
