package UtilsModel;

import EntityTree.Entity;
import EntityTree.EntityEmpty;
import EntityTree.EntityModel;
import Scene.IInput;
import Scene.SceneState;
import UtilsCommon.Camera;
import UtilsCommon.Ray;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class EditOperations {
    IInput input;
    ArrayList<VertexPosition> pickedVertices;
    SceneState sceneState;

    public EditOperations(IInput input, ArrayList<VertexPosition> pickedVertices, SceneState sceneState){
        this.input = input;
        this.pickedVertices = pickedVertices;
        this.sceneState = sceneState;
    }

    public void movePoints(float xOffset, float yOffset){
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

    public void createFace(){
        Entity temp = sceneState.getSelectedEntity();
        if(temp instanceof EntityModel){
            ((EntityModel)temp).getMesh().addFace(pickedVertices);
        } else {
            System.out.println("Wrong entity selected");
        }
    }

    public void copyVertices(){
        Entity temp = sceneState.getSelectedEntity();
        if(temp instanceof EntityModel){
            ArrayList<VertexPosition> newPickedList = new ArrayList<>();
            Mesh mesh = ((EntityModel)temp).getMesh();
            for(VertexPosition item: pickedVertices){
                newPickedList.add(new VertexPosition(item.getValue()));
                mesh.addVertex(newPickedList.get(newPickedList.size()-1));
                newPickedList.get(newPickedList.size()-1).pick();
                item.unpick();
            }
            pickedVertices.clear();
            pickedVertices.addAll(newPickedList);
        }
    }

    public void deleteVertex(){
        Entity temp = sceneState.getSelectedEntity();
        if(temp instanceof EntityModel){
            Mesh mesh = ((EntityModel)temp).getMesh();
            for(var item: pickedVertices){
                mesh.deleteVertex(item);
                item.unpick();
            }
        }
        pickedVertices.clear();
    }
}
