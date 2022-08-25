package EntityTree;

import Scene.IInput;
import Scene.SceneState;
import UtilsModel.Face;
import UtilsModel.Mesh;
import UtilsModel.VertexInstance;
import UtilsModel.VertexPosition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class EntityOperations {
    IInput input;
    ArrayList<VertexPosition> pickedVertices;
    SceneState sceneState;

    public EntityOperations(IInput input, ArrayList<VertexPosition> pickedVertices, SceneState sceneState) {
        this.input = input;
        this.pickedVertices = pickedVertices;
        this.sceneState = sceneState;
    }

    public void mergeTwoEntities() {
        Entity firstSelectedEntity = sceneState.getMainSelectedEntity();
        if(sceneState.getSelectedEntities().size() != 2){
            return;
        }
        var tempList = sceneState.getSelectedEntities().stream().toList();
        Entity secondSelectedEntity = tempList.get(0) == firstSelectedEntity ? tempList.get(1) : tempList.get(0);

        Entity iterEntity = firstSelectedEntity.getParent();
        while(iterEntity != null) {
            if(iterEntity == secondSelectedEntity) {
                Entity swapEntity = secondSelectedEntity;
                secondSelectedEntity = firstSelectedEntity;
                firstSelectedEntity = swapEntity;
                break;
            }
            iterEntity = iterEntity.getParent();
        }

        ArrayList<Face> faces = new ArrayList<>();
        ArrayList<VertexPosition> vertices = new ArrayList<>();
        ArrayList<Entity> children = new ArrayList<>();

        for(Entity entity: sceneState.getSelectedEntities()) {
            if(entity instanceof EntityModel) {
                faces.addAll(((EntityModel) entity).getMesh().getFaces());
                vertices.addAll(((EntityModel) entity).getMesh().getVertices());
            }
            children.addAll(entity.getChildren());
        }

        Entity resultEntity = new EntityModel(new Mesh(vertices, faces), firstSelectedEntity.getParent());
        resultEntity.setName(firstSelectedEntity.getName());
        for(var child : children) {
            child.setParent(resultEntity);
        }
        resultEntity.setTransform(firstSelectedEntity.getTransform());

        firstSelectedEntity.setParent(null);
        secondSelectedEntity.setParent(null);

        sceneState.clearSelectedEntities();
        sceneState.setMainSelectedEntity(resultEntity);
        sceneState.addSelectedEntity(resultEntity);
    }

    public void pickAllEntityVertices() {
        Entity entity = sceneState.getMainSelectedEntity();
        if(entity instanceof EntityModel) {
            for(var vertex: pickedVertices) {
                vertex.unpick();
            }
            pickedVertices.clear();
            for(var vertex: ((EntityModel) entity).getMesh().getVertices()){
                vertex.pick();
                pickedVertices.add(vertex);
            }
        }
    }

    public void deleteEntities() {
        for(Entity entity : sceneState.getSelectedEntities()){
            if(entity.getParent() == null)
                continue;
            for(Entity child: entity.getChildren()){
                child.setParent(entity.getParent());
            }
            entity.setParent(null);
        }
        sceneState.clearSelectedEntities();
        sceneState.setMainSelectedEntity(null);
    }

    public void copyEntity() {
        Entity oldEntity = sceneState.getMainSelectedEntity();
        if(oldEntity instanceof EntityModel) {
            ArrayList<Face> faces = new ArrayList<>();
            HashSet<VertexPosition> set = new HashSet<>();
            ArrayList<Face> oldFaces = ((EntityModel) oldEntity).getMesh().getFaces();
            for(Face face : oldFaces) {
                ArrayList<VertexPosition> curVertices = face.getVertices().stream().map(VertexInstance::getPosition)
                        .map(VertexPosition::getValue).map(VertexPosition::new).collect(Collectors.toCollection(ArrayList::new));
                faces.add(new Face(curVertices));
                set.addAll(curVertices);
            }
            ArrayList<VertexPosition> vertices = new ArrayList<>(set);
            Mesh newMesh = new Mesh(vertices, faces);
            EntityModel newEntity = new EntityModel(newMesh, oldEntity.getParent());
            newEntity.setName(oldEntity.getName());
        }
    }
}
