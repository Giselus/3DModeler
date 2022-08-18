package UtilsModel;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class VertexInstance {
    private final VertexPosition position;
    private final UtilsModel.Face face;
    //private NormalVector normal;

    private Vector2f textureCoordinate;
    public VertexInstance(VertexPosition position, Face face){
        position.addInstance(this);
        this.position = position;
        this.face = face;
        //this.normal = normal;
    }

    public VertexPosition getPosition(){
        return position;
    }

    public Face getFace(){
        return face;
    }

    public float[] getBufferedData(){
        Vector3f pos = position.getValue();
        Vector3f normal = this.face.getNormal();
        boolean isPicked = position.isPicked();
        return new float[]{pos.x,pos.y,pos.z,normal.x,normal.y,normal.z,isPicked ? 1f : -1f};
    }
}
