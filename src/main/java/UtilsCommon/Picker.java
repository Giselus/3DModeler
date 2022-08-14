package UtilsCommon;

import EntityTree.EntityModel;
import Scene.IInput;
import Scene.SceneState;
import UtilsModel.Face;
import UtilsModel.VertexInstance;
import UtilsModel.VertexPosition;

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
                for(Face face: ((EntityModel) entity).getFaces()){
                    for(VertexInstance v: face.getVertices()){
                        tryToPick(ray, v);
                    }
                }
            }
        });

        if(lastlyChosenVertex != null && !lastlyChosenVertex.isPicked()){
            lastlyChosenVertex.pick();
            pickedVertices.add(lastlyChosenVertex);
        }
    }

    private void tryToPick(Ray ray, VertexInstance vertex){
        float radius = ray.getOrigin().distance(vertex.getPosition().getValue());
        radius *= vertexRadius;
        float distance = ray.distanceFromSphere(vertex.getPosition().getValue(),radius);
        if(distance > 0){
            if(lastlyChosenVertex == null || distance < minDistance){
                lastlyChosenVertex = vertex.getPosition();
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
