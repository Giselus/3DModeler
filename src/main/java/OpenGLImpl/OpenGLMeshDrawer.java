package OpenGLImpl;

import Scene.IMeshDrawer;
import UtilsModel.Face;
import UtilsModel.VertexInstance;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class OpenGLMeshDrawer implements IMeshDrawer {
    private ArrayList<VertexInstance> vertices;
    private ArrayList<Face> faces;

    public OpenGLMeshDrawer(ArrayList<Face> faces){
        this.faces = faces;
        processTopology();
        setupMesh();
    }

    @Override
    public void draw(int mode){
        glBindVertexArray(vao);
        glDrawArrays(mode,0,vertices.size());
        glBindVertexArray(0);
    }

    public void recalculate(){
        processTopology();
        setupMesh();
    }

    public int getVAO(){
        return vao;
    }
//    public void draw(EntityModel entity) {
//        Shader shader = renderer.getActiveShader();
//        Matrix4fc modelMatrix = entity.getTransform().getGlobalModelMatrix();
//        shader.setMatrix4("model",modelMatrix);
//
//        glBindVertexArray(vao);
//        glDrawArrays(renderer.getDrawingMode(),0, vertices.size());
//        glBindVertexArray(0);
//
////        Ray ray = InputController.ray;
////        if(ray == null)
////            return;
////        for(Face f: entity.getFaces()){
////            ArrayList<Vector3f> positions = new ArrayList<>();
////
////            for(VertexInstance v: f.getVertices()){
////                float distanceToCenter = ray.origin.distance(v.getPosition().getValue());
////                float radius = 0.001f * distanceToCenter;
////                float dist = ray.distanceFromSphere(v.getPosition().getValue(),radius);
////                if(dist > 0){
////                    System.out.println(dist);
////                }
////            }
////        }
//
//        //        model.Draw();
//        //        super.update();
//    }

    private int vao, vbo;

    private void processTopology(){
        vertices = new ArrayList<>();
        for(Face face: faces){
            var faceVertices = face.getVertices();
            for(int i = 2; i < faceVertices.size(); i++){
                vertices.add(faceVertices.get(0));
                vertices.add(faceVertices.get(i-1));
                vertices.add(faceVertices.get(i));
            }
        }
    }

    private void setupMesh(){
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
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

        MemoryUtil.memFree(verticesBuffer);

        glBindVertexArray(0);
    }
}
