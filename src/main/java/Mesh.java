import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private ArrayList<Vertex> Vertices;
    private ArrayList<Integer> Indices;
    private ArrayList<Texture> Textures;

    public Mesh(ArrayList<Vertex> Vertices, ArrayList<Integer> Indices, ArrayList<Texture> Textures){
        this.Vertices = Vertices;
        this.Indices = Indices;
        this.Textures = Textures;

        setupMesh();
    }

    public void draw(Shader shader){
        shader.use();
        int diffuseNr = 1;
        int specularNr = 1;
        for(int i = 0; i < Textures.size();i++){
            glActiveTexture(GL_TEXTURE0+i);
            String number;
            String name = Textures.get(i).type;
            if(name.equals("texture_diffuse")){
                number = String.valueOf(diffuseNr);
                diffuseNr++;
            }else{
                number = String.valueOf(specularNr);
                specularNr++;
            }
            shader.setFloat(("material" + name + number),i);
            glBindTexture(GL_TEXTURE_2D,Textures.get(i).ID);
        }
        glActiveTexture(GL_TEXTURE0);

        glBindVertexArray(VAO);
        glDrawElements(GL_TRIANGLES,Indices.size(),GL_UNSIGNED_INT,0);
        glBindVertexArray(0);
    }

    private int VAO, VBO, EBO;

    private void setupMesh(){
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER,VBO);
        MemoryStack stack = MemoryStack.stackPush();
        FloatBuffer verticesBuffer = stack.mallocFloat(Vertices.size() * 8);
        Vertices.forEach((v) -> verticesBuffer.put(v.getBufferedData()));
        verticesBuffer.flip();
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,EBO);
        IntBuffer indicesBuffer = stack.mallocInt(Indices.size());
        Indices.forEach((v) -> indicesBuffer.put(v));
        indicesBuffer.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,indicesBuffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0,3,GL_FLOAT,false,32,0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1,3,GL_FLOAT,false,32,12);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2,2,GL_FLOAT,false,32,24);

        glBindVertexArray(0);
    }
}
