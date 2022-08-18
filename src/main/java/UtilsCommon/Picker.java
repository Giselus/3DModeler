package UtilsCommon;

import EntityTree.Entity;
import EntityTree.EntityModel;
import Scene.IInput;
import Scene.SceneState;
import UtilsModel.Face;
import UtilsModel.VertexInstance;
import UtilsModel.VertexPosition;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

import java.util.ArrayList;
import static UtilsCommon.GeometryMath.*;

public class Picker {

    IInput input;
    ArrayList<VertexPosition> pickedVertices;
    ArrayList<Face> pickedFaces;
    SceneState sceneState;

    private static float vertexRadius = 0.0015f;
    private float minDistance;
    VertexPosition lastlyChosenVertex = null;
    Face lastlyChosenFace = null;

    public Picker(IInput input, ArrayList<VertexPosition> pickedVertices, ArrayList<Face> pickedFaces, SceneState sceneState) {
        this.input = input;
        this.pickedVertices = pickedVertices;
        this.pickedFaces = pickedFaces;
        this.sceneState = sceneState;
    }
    public void pick(){
        if(!input.isKeyPressed(IInput.KeyCode.KEY_LEFT_CTRL)){
            clearPicked();
        }
        if(input.isKeyPressed(IInput.KeyCode.KEY_LEFT_SHIFT)){
            castFaceRay();
        }else{
            castVertexRay();
        }
    }

    private void castFaceRay(){
        Camera camera = sceneState.getCamera();
        Ray ray = clickRay(input.getMouseX(),input.getMouseY(), camera);
        lastlyChosenFace = null;

        Entity entity = sceneState.getMainSelectedEntity();
        if(entity instanceof EntityModel){
            Matrix4fc transformation = entity.getTransform().getGlobalModelMatrix();
            for(Face f: ((EntityModel) entity).getMesh().getFaces()) {
                checkRayFaceIntersection(ray, f, transformation);
            }
        }

        if(lastlyChosenFace != null){
            addFaceToPicked(lastlyChosenFace);
        }
    }
    private void castVertexRay(){
        Camera camera = sceneState.getCamera();
        Ray ray = clickRay(input.getMouseX(),input.getMouseY(), camera);
        lastlyChosenVertex = null;

        Entity entity = sceneState.getMainSelectedEntity();
        if(entity instanceof EntityModel){
            Matrix4fc transformation = entity.getTransform().getGlobalModelMatrix();
            for(VertexPosition v: ((EntityModel) entity).getMesh().getVertices()) {
                checkRayVertexIntersection(ray, v, transformation);
            }
        }

        if(lastlyChosenVertex != null){
            addVertexToPicked(lastlyChosenVertex);
        }
    }

    private void checkRayVertexIntersection(Ray ray, VertexPosition vertex, Matrix4fc transformation){
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

    private void checkRayFaceIntersection(Ray ray, Face face, Matrix4fc transformation){
        ArrayList<Vector3f> vertices = new ArrayList<>();
        for(VertexInstance vertex: face.getVertices()){
            Vector3f position = vertex.getPosition().getValue();
            position.mulPosition(transformation);
            vertices.add(position);
        }
        float distance = ray.distanceFromPlane(vertices);
        if(distance > 0){
            if(lastlyChosenFace == null || distance < minDistance){
                lastlyChosenFace = face;
                minDistance = distance;
            }
        }
    }

    private void clearPicked(){
        for(VertexPosition vertex: pickedVertices){
            vertex.unpick();
        }
        for(Face face: pickedFaces){
            face.unpick();
        }
        pickedFaces.clear();
        pickedVertices.clear();
    }

    private void addVertexToPicked(VertexPosition vertex){
        if(vertex.isPicked())
            return;
        vertex.pick();
        pickedVertices.add(vertex);
        for(VertexInstance instance: vertex.getInstances()){
            Face face = instance.getFace();
            if(face.isPicked())
                continue;
            boolean isEveryVertexOnFacePicked = true;
            for(VertexInstance instanceOnFace: face.getVertices()){
                VertexPosition vertexOnFace = instanceOnFace.getPosition();
                if(!vertexOnFace.isPicked()){
                    isEveryVertexOnFacePicked = false;
                }
            }
            if(isEveryVertexOnFacePicked){
                face.pick();
                pickedFaces.add(face);
            }
        }
    }

    private void addFaceToPicked(Face face){
        if(face.isPicked())
            return;
        face.pick();
        pickedFaces.add(face);
        for(VertexInstance instance: face.getVertices()){
            VertexPosition vertex = instance.getPosition();
            if(!vertex.isPicked()){
                vertex.pick();
                pickedVertices.add(vertex);
            }
        }
    }
}
