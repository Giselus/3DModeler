package ModelLoader;

import EntityTree.Entity;
import EntityTree.EntityEmpty;
import EntityTree.EntityModel;
import org.joml.Vector3f;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class OBJSaver implements Saver{

    private HashMap<Vector3f, Integer> vertices;

    private int vertexNumber = 1;

    @Override
    public void save(Entity entity, String dst) {
        clear();
        String entityToSave = entity
                .getChildren().stream()
                .map(this::preorderTraverse)
                .reduce(new StringBuilder(), StringBuilder::append).toString();

        try{
            FileWriter myWriter = new FileWriter(dst);
            myWriter.write(entityToSave);
            myWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private StringBuilder preorderTraverse(Entity entity){
        StringBuilder result = new StringBuilder();
        if(entity instanceof EntityModel){
            result.append(processEntityModel((EntityModel) entity));
        } else if(entity instanceof EntityEmpty) {
            result.append(processEntityEmpty((EntityEmpty) entity));
        }
        result.append(entity.getChildren().stream().map(this::preorderTraverse).reduce(new StringBuilder(), StringBuilder::append));
        return result;
    }

    private StringBuilder processEntityModel(EntityModel entity){
        StringBuilder result = new StringBuilder();
        result.append("o ").append(entity.getName()).append("\n");
        for(var vertex: entity.getVertices()){
            if (!vertices.containsKey(vertex.getValue())) {
                vertices.put(vertex.getValue(), vertexNumber);
                vertexNumber++;
                result.append("v ").append(vertexToStringBuilder(vertex.getValue())).append("\n");
            }
        }
        for(var face: entity.getFaces()){
            result.append("f ");
            for(var vertex: face.getVertices()){
                var value = vertex.getPosition().getValue();
                result.append(vertices.get(value)).append("/").append(1).append("/").append("1").append(" ");
            }
            result.append("\n");
        }
        return result;

    }

    private StringBuilder processEntityEmpty(EntityEmpty entity){
        return new StringBuilder();
    }

    private StringBuilder vertexToStringBuilder(Vector3f vertex){
        return new StringBuilder().append(vertex.x).append(" ").append(vertex.y).append(" ").append(vertex.z).append(" ");
    }
    private void clear(){
        vertices = new HashMap<>();
        vertexNumber = 1;
    }
}
