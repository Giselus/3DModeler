package UtilsModel;

import Scene.RenderingUpdater;

import java.util.ArrayList;

public class Model {
    private ArrayList<VertexPosition> vertices;
    private ArrayList<Face> faces;

    private UtilsModel.IMesh mesh;

    public RenderingUpdater renderingUpdater;
    public Model(ArrayList<VertexPosition> vertices, ArrayList<Face> faces, RenderingUpdater renderingUpdater){
        this.renderingUpdater = renderingUpdater;
        this.vertices = vertices;
        this.faces = faces;
        //TODO: make this dependency injection
        mesh = new Mesh(faces, renderingUpdater);
    }

    public void Draw(){
        mesh.draw();
    }
}
