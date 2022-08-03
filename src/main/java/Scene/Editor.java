package Scene;

import EntityTree.EntityModel;
import UtilsCommon.Camera;
import UtilsCommon.Ray;
import UtilsModel.EditOperations;
import UtilsModel.Face;
import UtilsModel.VertexInstance;
import UtilsModel.VertexPosition;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Editor {

    private static float vertexRadius = 0.0015f;

    private final ArrayList<VertexPosition> pickedVertices = new ArrayList<>();

    private IInput input;
    private SceneState sceneState;

    private final EditOperations editOperations;
    public Editor(IInput input, SceneState sceneState){
        this.input = input;
        this.sceneState = sceneState;
        editOperations = new EditOperations(input, pickedVertices, sceneState);
        registerInput();
    }

    private void registerInput(){
        input.addMouseKeyCallback(IInput.MouseKeyCode.MOUSE_LEFT,this::pick);
        input.addMouseMoveCallback(editOperations::movePoints);
        input.addKeyCallback(IInput.KeyCode.KEY_A, editOperations::createFace);
        input.addKeyCallback(IInput.KeyCode.KEY_C, editOperations::copyVertices);
        input.addKeyCallback(IInput.KeyCode.KEY_D, editOperations::deleteVertex);
    }

    private float minDistance;
    private VertexPosition lastlyChoosenVertex;

    private void pick(){
        if(!input.isKeyPressed(IInput.KeyCode.KEY_LEFT_CTRL)){
            clearPicked();
        }
        float mouseX = (2f * input.getMouseX()) / (float)sceneState.getSceneWindowWidth() - 1f;
        float mouseY = (2f * input.getMouseY()) / (float)sceneState.getSceneWindowHeight() - 1f;
        Camera camera = sceneState.getCamera();
        Ray ray = camera.getRay(mouseX,mouseY);
        lastlyChoosenVertex = null;

        sceneState.getRoot().invokeFunctionOnSubtree(entity -> {
            if(entity instanceof EntityModel){
                for(Face face: ((EntityModel) entity).getFaces()){
                    for(VertexInstance v: face.getVertices()){
                        tryToPick(ray,v);
                    }
                }
            }
        });
        if(lastlyChoosenVertex != null && !lastlyChoosenVertex.isPicked()){
            lastlyChoosenVertex.pick();
            pickedVertices.add(lastlyChoosenVertex);
        }
    }

    private void tryToPick(Ray ray, VertexInstance vertex){
        float radius = ray.origin.distance(vertex.getPosition().getValue());
        radius *= vertexRadius;
        float distance = ray.distanceFromSphere(vertex.getPosition().getValue(),radius);
        if(distance > 0){
            if(lastlyChoosenVertex == null || distance < minDistance){
                lastlyChoosenVertex = vertex.getPosition();
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
