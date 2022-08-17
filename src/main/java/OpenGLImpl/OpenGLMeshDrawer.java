package OpenGLImpl;

import UtilsModel.Face;
import UtilsModel.Mesh;
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

public class OpenGLMeshDrawer{
    private ArrayList<VertexInstance> triangulatedVertices;
    private ArrayList<VertexInstance> lineVertices;

    private Mesh mesh;

    public OpenGLMeshDrawer(Mesh mesh){
        this.mesh = mesh;
        processTopology();
        setupMesh();
    }

    public void draw(int mode){
        if(mode == GL_LINES){
            glBindVertexArray(lineVao);
            glDrawArrays(mode, 0, lineVertices.size());
        }else{
            glBindVertexArray(triangulatedVao);
            glDrawArrays(mode, 0, triangulatedVertices.size());
        }
        glBindVertexArray(0);
    }

    public void recalculate(){
        processTopology();
        setupMesh();
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

    private int triangulatedVao, triangulatedVbo;
    private int lineVao, lineVbo;

    private void processTopology(){
        lineVertices = new ArrayList<>();
        triangulatedVertices = new ArrayList<>();
        for(Face face: mesh.getFaces()){
            var faceVertices = face.getVertices();
            for(int i = 2; i < faceVertices.size(); i++){
                triangulatedVertices.add(faceVertices.get(0));
                triangulatedVertices.add(faceVertices.get(i-1));
                triangulatedVertices.add(faceVertices.get(i));
            }

            for(int i = 1; i < faceVertices.size(); i++) {
                lineVertices.add(faceVertices.get(i - 1));
                lineVertices.add(faceVertices.get(i));
            }
            lineVertices.add(faceVertices.get(faceVertices.size()-1));
            lineVertices.add(faceVertices.get(0));
        }
    }

    private void setupMesh(){
        glDeleteVertexArrays(triangulatedVao);
        glDeleteVertexArrays(lineVao);
        glDeleteBuffers(triangulatedVbo);
        glDeleteBuffers(lineVbo);
        lineVao = glGenVertexArrays();
        triangulatedVao = glGenVertexArrays();
        lineVbo = glGenBuffers();
        triangulatedVbo = glGenBuffers();

        glBindVertexArray(lineVao);

        glBindBuffer(GL_ARRAY_BUFFER, lineVbo);

        FloatBuffer lineVerticesBuffer = MemoryUtil.memAllocFloat(lineVertices.size() * 7);
        lineVertices.forEach((v) -> lineVerticesBuffer.put(v.getBufferedData()));
        lineVerticesBuffer.flip();

        glBufferData(GL_ARRAY_BUFFER, lineVerticesBuffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0,3,GL_FLOAT,false,28,0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1,3,GL_FLOAT,false,28,12);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2,1,GL_FLOAT,false,28,24);

        MemoryUtil.memFree(lineVerticesBuffer);

        glBindVertexArray(triangulatedVao);

        glBindBuffer(GL_ARRAY_BUFFER, triangulatedVbo);

        FloatBuffer triangulatedVerticesBuffer = MemoryUtil.memAllocFloat(triangulatedVertices.size() * 7);
        triangulatedVertices.forEach((v) -> triangulatedVerticesBuffer.put(v.getBufferedData()));
        triangulatedVerticesBuffer.flip();

        glBufferData(GL_ARRAY_BUFFER, triangulatedVerticesBuffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0,3,GL_FLOAT,false,28,0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1,3,GL_FLOAT,false,28,12);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2,1,GL_FLOAT,false,28,24);

        MemoryUtil.memFree(triangulatedVerticesBuffer);

        glBindVertexArray(0);
    }
}
