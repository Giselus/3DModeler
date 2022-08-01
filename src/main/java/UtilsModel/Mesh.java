package UtilsModel;

import Scene.IMeshDrawer;

import java.util.ArrayList;

public class Mesh {
    private ArrayList<VertexPosition> vertices;
    private ArrayList<Face> faces;

    private IMeshDrawer meshDrawer;

    public Mesh(ArrayList<VertexPosition> vertices, ArrayList<Face> faces){
        this.vertices = vertices;
        this.faces = faces;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }

    public ArrayList<VertexPosition> getVertices(){
        return vertices;
    }

    public IMeshDrawer getMeshDrawer(){
        return meshDrawer;
    }

    public void setMeshDrawer(IMeshDrawer meshDrawer){
        this.meshDrawer = meshDrawer;
    }
}
