package Scene;

import EntityTree.EntityModel;
import UtilsCommon.Camera;
import UtilsCommon.Ray;
import UtilsModel.Face;
import UtilsModel.VertexInstance;
import UtilsModel.VertexPosition;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Editor {

    private static float vertexRadius = 0.0015f;

    private ArrayList<VertexPosition> pickedVertices = new ArrayList<>();

    private IInput input;
    private SceneState sceneState;

    public Editor(IInput input, SceneState sceneState){
        this.input = input;
        this.sceneState = sceneState;
        registerInput();
    }

    private void movePoints(float xOffset, float yOffset){
        if(!input.isKeyPressed(IInput.KeyCode.KEY_X)){
            return;
        }
        float mouseX = input.getMouseX();
        float mouseY = input.getMouseY();
        float lastX = mouseX - xOffset;
        float lastY = mouseY - yOffset;

        Vector3f meanPoint = new Vector3f();
        for(VertexPosition v: pickedVertices){
            meanPoint.add(v.getValue());
        }
        meanPoint.div((float)pickedVertices.size());
        Vector3f cameraDir = sceneState.getCamera().getDirection();
        Vector4f normalPlane = new Vector4f(0f);
        //(x + y + z = w)
        normalPlane.x = cameraDir.x;
        normalPlane.y = cameraDir.y;
        normalPlane.z = cameraDir.z;
        normalPlane.w = cameraDir.x *meanPoint.x + cameraDir.y * meanPoint.y + cameraDir.z * meanPoint.z;

        float clipX = (2f * mouseX) / (float)sceneState.getSceneWindowWidth() - 1f;
        float clipY = (2f * mouseY) / (float)sceneState.getSceneWindowHeight() -1f;
        Camera camera = sceneState.getCamera();
        Ray ray = camera.getRay(clipX,clipY);
        Vector3f anotherRay = ray.direction;
        Vector3f cameraPosition = camera.getPosition();
        float parameter = (normalPlane.w - normalPlane.x*cameraPosition.x
                - normalPlane.y*cameraPosition.y - normalPlane.z*cameraPosition.z)
                /(normalPlane.x * anotherRay.x + normalPlane.y * anotherRay.y + normalPlane.z * anotherRay.z);
        Vector3f intersectionPoint = new Vector3f(0f);
        anotherRay.mul(parameter, intersectionPoint);
        intersectionPoint.add(cameraPosition);

        float clipX2 = (2f * lastX) / (float)sceneState.getSceneWindowWidth() - 1f;
        float clipY2 = (2f * lastY) / (float)sceneState.getSceneWindowHeight() -1f;
        Ray ray2 = camera.getRay(clipX2,clipY2);
        Vector3f anotherRay2 = ray2.direction;
        float parameter2 = (normalPlane.w - normalPlane.x*cameraPosition.x
                - normalPlane.y*cameraPosition.y - normalPlane.z*cameraPosition.z)
                /(normalPlane.x * anotherRay.x + normalPlane.y * anotherRay.y + normalPlane.z * anotherRay.z);
        Vector3f intersectionPoint2 = new Vector3f(0f);
        anotherRay2.mul(parameter2, intersectionPoint2);
        intersectionPoint2.add(cameraPosition);

        Vector3f offset = new Vector3f();
        intersectionPoint.sub(intersectionPoint2,offset);

        for(VertexPosition v: pickedVertices){
            v.setValue(v.getValue().add(offset));
        }
    }

    private void registerInput(){
        input.addMouseKeyCallback(IInput.MouseKeyCode.MOUSE_LEFT,this::pick);
        input.addMouseMoveCallback(this::movePoints);
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
