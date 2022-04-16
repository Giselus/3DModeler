import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    public ArrayList<Vertex> vertices;
    public ArrayList<Integer> indices;
    public ArrayList<Texture> textures;

    public Mesh(ArrayList<Vertex> vertices, ArrayList<Integer> indices, ArrayList<Texture> textures){
        this.vertices = vertices;
        this.indices = indices;
        this.textures = textures;

        setupMesh();
    }

    public void draw(Shader shader){
        shader.use();
        int diffuseNr = 1;
        int specularNr = 1;
        for(int i = 0; i < textures.size();i++){
            glActiveTexture(GL_TEXTURE0+i);
            String number;
            String name = textures.get(i).type;
            if(name.equals("texture_diffuse")){
                number = String.valueOf(diffuseNr);
                diffuseNr++;
            }else{
                number = String.valueOf(specularNr);
                specularNr++;
            }
            shader.setFloat(("material" + name + number),i);
            glBindTexture(GL_TEXTURE_2D,textures.get(i).ID);
        }
        glActiveTexture(GL_TEXTURE0);

        glBindVertexArray(VAO);
        glDrawElements(GL_TRIANGLES,indices.size(),GL_UNSIGNED_INT,0);
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
        FloatBuffer verticesBuffer = stack.mallocFloat(vertices.size() * 8);
        vertices.forEach((v) -> verticesBuffer.put(v.getBufferedData()));
        verticesBuffer.flip();
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,EBO);
        IntBuffer indicesBuffer = stack.mallocInt(indices.size());
        indices.forEach((v) -> indicesBuffer.put(v));
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
