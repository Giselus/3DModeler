package Scene;

import EntityTree.Entity;
import EntityTree.EntityModel;
import UtilsCommon.Shader;
import UtilsModel.Face;
import UtilsModel.VertexInstance;
import org.joml.Matrix4fc;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class ModelDrawer implements IModelDrawer {
    private ArrayList<VertexInstance> vertices;
    private final RenderingUpdater renderingUpdater;

    public ModelDrawer(ArrayList<Face> faces, RenderingUpdater renderingUpdater){
        processTopology(faces);
        this.renderingUpdater = renderingUpdater;
        setupMesh();
    }

    public void draw(EntityModel entity) {
        Shader shader = renderingUpdater.getActiveShader();
        Matrix4fc modelMatrix = entity.getTransform().getGlobalModelMatrix();
        shader.setMatrix4("model",modelMatrix);

        glBindVertexArray(vao);
        glDrawArrays(renderingUpdater.getDrawingMode(),0, vertices.size());
        glBindVertexArray(0);
        //        model.Draw();
        //        super.update();
    }

    private int vao, vbo;

    private void processTopology(ArrayList<Face> faces){
        vertices = new ArrayList<>();
        for(Face face: faces){
            var faceVertices = face.getVertices();
            for(int i = 2; i < faceVertices.size(); i++){
                vertices.add(faceVertices.get(i-2));
                vertices.add(faceVertices.get(i-1));
                vertices.add(faceVertices.get(i));
            }
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

