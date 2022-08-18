package UtilsModel;

import java.util.ArrayList;

public class Mesh {
    private final ArrayList<VertexPosition> vertices;
    private final ArrayList<Face> faces;

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

    public void addVertex(VertexPosition vertex){
        vertices.add(vertex);
    }

    public void addFace(ArrayList<VertexPosition> listOfVertices){
        if(listOfVertices.size() == 3)
            faces.add(new Face(listOfVertices));
        else
            System.out.println("Wrong number of vertices");
    }

    public void deleteVertex(VertexPosition vertex){
        for(int i=faces.size()-1; i>=0; i--){
            if(faces.get(i).getVertices().stream().map(VertexInstance::getPosition).filter(v -> v==vertex).limit(1).count() > 0){
                faces.remove(i);
            }
        }
        vertices.remove(vertex);
    }
}
