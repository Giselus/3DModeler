package UtilsCommon;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private ArrayList<VertexInstance> Vertices;

    public Mesh(ArrayList<Face> Faces){
        processTopology(Faces);
        setupMesh();
    }
    public void draw(Shader shader){
        shader.use();
//        int diffuseNr = 1;
//        int specularNr = 1;
//        for(int i = 0; i < Textures.size();i++){
//            glActiveTexture(GL_TEXTURE0+i);
//            String number;
//            String name = Textures.get(i).type;
//            if(name.equals("texture_diffuse")){
//                number = String.valueOf(diffuseNr);
//                diffuseNr++;
//            }else{
//                number = String.valueOf(specularNr);
//                specularNr++;
//            }
//            shader.setFloat(("material" + name + number),i);
//            glBindTexture(GL_TEXTURE_2D,Textures.get(i).ID);
//        }
//        glActiveTexture(GL_TEXTURE0);

        glBindVertexArray(VAO);
        glDrawArrays(GL_TRIANGLES,0,Vertices.size());
        glBindVertexArray(0);
    }

    private int VAO, VBO;

    private void processTopology(ArrayList<Face> Faces){
        Vertices = new ArrayList<>();
        for(Face face: Faces){
            for(VertexInstance vertex: face.Vertices){
                Vertices.add(vertex);
            }
        }
    }

    private void setupMesh(){
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER,VBO);

        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(Vertices.size() * 6);
        Vertices.forEach((v) -> verticesBuffer.put(v.getBufferedData()));
        verticesBuffer.flip();
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0,3,GL_FLOAT,false,24,0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1,3,GL_FLOAT,false,24,12);

        glBindVertexArray(0);
    }
}
