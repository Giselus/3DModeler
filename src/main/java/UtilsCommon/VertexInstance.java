package UtilsCommon;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class VertexInstance {
    private VertexPosition Position;
    private Face Face;
    private NormalVector Normal;

    private Vector2f TextureCoordinate;
    public VertexInstance(VertexPosition Position, Face Face, NormalVector Normal){
        this.Position = Position;
        this.Face = Face;
        this.Normal = Normal;
    }

    public VertexPosition getPosition(){
        return Position;
    }

    public void SetNormal(NormalVector Normal){
        this.Normal = Normal;
    }

    public float[] getBufferedData(){
        Vector3f pos = Position.getValue();
        Vector3f normal = Normal.getValue();
        boolean isPicked = Position.isPicked();
        return new float[]{pos.x,pos.y,pos.z,normal.x,normal.y,normal.z,isPicked ? 1f : -1f};
    }
}
