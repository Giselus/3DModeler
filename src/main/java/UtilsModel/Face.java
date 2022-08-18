package UtilsModel;

import org.joml.Vector3f;

import java.util.ArrayList;

public class Face {
    private final ArrayList<VertexInstance> vertices;
    private boolean isPicked = false;

    private Vector3f normal;

    public Face(ArrayList<VertexPosition> vertices){
        this.vertices = new ArrayList<>();
        //NormalVector normal = new NormalVector(vertices.get(0),vertices.get(1),vertices.get(2));
        for(VertexPosition vertex: vertices){
            this.vertices.add(new VertexInstance(vertex, this));
        }
        recalculateNormal();
    }

    public ArrayList<VertexInstance> getVertices() {
        return vertices;
    }

    public Vector3f getNormal(){
        return new Vector3f(normal);
    }

    public void recalculateNormal(){
        Vector3f u1 = vertices.get(0).getPosition().getValue();
        Vector3f u2 = vertices.get(1).getPosition().getValue();
        Vector3f v1 = vertices.get(2).getPosition().getValue();
        u1.sub(v1);
        u2.sub(v1);

        u1.cross(u2);
        u1.normalize();
        normal = u1;
    }

    public void pick(){
        isPicked = true;
    }
    public void unpick() { isPicked = false; }
    public boolean isPicked(){
        return isPicked;
    }
}
