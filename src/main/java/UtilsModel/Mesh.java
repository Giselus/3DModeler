package UtilsModel;

import Controllers.RenderingController;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;

public class Mesh implements IMesh {

    private ArrayList<VertexInstance> vertices;

    public Mesh(ArrayList<Face> faces){
        processTopology(faces);
        setupMesh();
    }
    public void draw(){
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

        glBindVertexArray(vao);
        glDrawArrays(RenderingController.getInstance().getDrawingMode(),0, vertices.size());
        glBindVertexArray(0);
    }

    private int vao, vbo;

    private void processTopology(ArrayList<Face> faces){
        vertices = new ArrayList<>();
        for(Face face: faces){
            vertices.addAll(face.getVertices());
        }
    }

    private void setupMesh(){
        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.size() * 7);
        vertices.forEach((v) -> verticesBuffer.put(v.getBufferedData()));
        verticesBuffer.flip();
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0,3,GL_FLOAT,false,28,0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1,3,GL_FLOAT,false,28,12);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2,1,GL_FLOAT,false,28,24);

        glBindVertexArray(0);
    }
}
