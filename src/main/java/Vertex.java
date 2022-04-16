import org.joml.*;

public class Vertex{
    Vector3f Position;
    Vector3f Normal;
    Vector2f TexCoords;

    public Vertex(Vector3f position, Vector3f normal, Vector2f texCoords){
        this.Position = position;
        this.Normal = normal;
        this.TexCoords = texCoords;
    }

    public Vertex(float positionX, float positionY, float positionZ, float normalX, float normalY, float normalZ,
                  float texCoordsX, float texCoordsY){
        this.Position = new Vector3f(positionX,positionY,positionZ);
        this.Normal = new Vector3f(normalX,normalY,normalZ);
        this.TexCoords = new Vector2f(texCoordsX,texCoordsY);
    }

    public float[] getBufferedData(){
        return new float[]{Position.x,Position.y,Position.z,Normal.x,Normal.y,Normal.z,TexCoords.x,TexCoords.y};
    }

}


