package UtilsModel;

import UtilsCommon.NormalVector;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class VertexInstance {
    private VertexPosition position;
    private UtilsModel.Face face;
    private NormalVector normal;

    private Vector2f textureCoordinate;
    public VertexInstance(VertexPosition position, Face face, NormalVector normal){
        this.position = position;
        this.face = face;
        this.normal = normal;
    }

    public VertexPosition getPosition(){
        return position;
    }

    public void setNormal(NormalVector normal){
        this.normal = normal;
    }

    public float[] getBufferedData(){
        Vector3f pos = position.getValue();
        Vector3f normal = this.normal.getValue();
        boolean isPicked = position.isPicked();
        return new float[]{pos.x,pos.y,pos.z,normal.x,normal.y,normal.z,isPicked ? 1f : -1f};
    }
}
