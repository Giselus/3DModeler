package UtilsCommon;

import EntityTree.EntityModel;
import Scene.IInput;
import Scene.SceneState;
import UtilsModel.Face;
import UtilsModel.VertexInstance;
import UtilsModel.VertexPosition;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

import java.util.ArrayList;
import static UtilsCommon.GeometryMath.*;

public class Picker {

    IInput input;
    ArrayList<VertexPosition> pickedVertices;
    SceneState sceneState;

    private static float vertexRadius = 0.0015f;
    private float minDistance;
    VertexPosition lastlyChosenVertex = null;

    public Picker(IInput input, ArrayList<VertexPosition> pickedVertices, SceneState sceneState) {
        this.input = input;
        this.pickedVertices = pickedVertices;
        this.sceneState = sceneState;
    }

    public void pickVertex(){
        if(!input.isKeyPressed(IInput.KeyCode.KEY_LEFT_CTRL)){
            clearPicked();
        }
        Camera camera = sceneState.getCamera();
        Ray ray = clickRay(input.getMouseX(),input.getMouseY(), camera);
        lastlyChosenVertex = null;

        sceneState.getRoot().invokeFunctionOnSubtree(entity -> {
            if(entity instanceof EntityModel){
                Matrix4fc transformation = entity.getTransform().getGlobalModelMatrix();
                for(VertexPosition v: ((EntityModel) entity).getMesh().getVertices()) {
                    tryToPick(ray, v, transformation);
                }
            }
        });

        if(lastlyChosenVertex != null && !lastlyChosenVertex.isPicked()){
            lastlyChosenVertex.pick();
            pickedVertices.add(lastlyChosenVertex);
        }
    }

    private void tryToPick(Ray ray, VertexPosition vertex, Matrix4fc transformation){
        Vector3f position = vertex.getValue().mulPosition(transformation);
        float radius = ray.getOrigin().distance(position);
        radius *= vertexRadius;
        float distance = ray.distanceFromSphere(position,radius);
        if(distance > 0){
            if(lastlyChosenVertex == null || distance < minDistance){
                lastlyChosenVertex = vertex;
                minDistance = distance;
            }
        }
    }

    private void clearPicked(){
        for(VertexPosition v: pickedVertices){
            v.unpick();
        }
        pickedVertices.clear();
    }
}
