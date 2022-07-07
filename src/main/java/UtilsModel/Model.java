package UtilsModel;

import Scene.RenderingUpdater;

import java.util.ArrayList;

public class Model {
    private ArrayList<VertexPosition> vertices;
    private ArrayList<Face> faces;

    public Model(ArrayList<VertexPosition> vertices, ArrayList<Face> faces){
        this.vertices = vertices;
        this.faces = faces;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }

}
