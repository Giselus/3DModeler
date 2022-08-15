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

import static UtilsCommon.GeometryMath.*;

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
        float previousX = mouseX - xOffset;
        float previousY = mouseY - yOffset;

        Vector3f meanPoint = meanPoint(pickedVertices);

        Camera camera = sceneState.getCamera();

        Vector4f normalPlane = normalPlane(camera.getDirection(), meanPoint);

        Vector3f cameraPosition = camera.getPosition();

        Vector3f currentMouseDirection = clickRay(mouseX, mouseY, camera).getDirection();
        Vector3f currentIntersectionPoint = intersectionVectorPlane(cameraPosition, currentMouseDirection, normalPlane);

        Vector3f previousMouseDirection = clickRay(previousX, previousY, camera).getDirection();
        Vector3f previousIntersectionPoint = intersectionVectorPlane(cameraPosition, previousMouseDirection, normalPlane);

        Vector3f offset = new Vector3f();
        currentIntersectionPoint.sub(previousIntersectionPoint, offset);

        translatePoints(pickedVertices, offset);
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
            pickedVertices.clear();
        }
    }
}
