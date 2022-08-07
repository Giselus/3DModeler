package ModelLoader;

import EntityTree.Entity;
import EntityTree.EntityEmpty;
import EntityTree.EntityModel;
import org.joml.Vector3f;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class OBJSaver implements Saver{

    private HashMap<Vector3f, Integer> vertices;
    private ArrayList<ArrayList<Integer>> adjacencyList;
    private int vertexNumber;

    private int modelNumber;

    @Override
    public void save(Entity entity, String dst, SaveMode saveMode) {
        clear();
        adjacencyList.add(new ArrayList<>());
        String entityToSave = entity
                .getChildren().stream()
                .map(item -> preorderTraverse(item, 0))
                .reduce(new StringBuilder(), StringBuilder::append).toString();

        try{
            FileWriter myWriter = new FileWriter(dst);
            myWriter.write(entityToSave);
            if(saveMode == SaveMode.EXTENDED_OBJ){
                myWriter.write(entityTreeStructure());
            }
            myWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private StringBuilder preorderTraverse(Entity entity, int parentNode){
        int id = modelNumber + 1;
        adjacencyList.add(new ArrayList<>());
        adjacencyList.get(parentNode).add(id);
        StringBuilder result = new StringBuilder();
        if(entity instanceof EntityModel){
            result.append(processEntityModel((EntityModel) entity));
        } else if(entity instanceof EntityEmpty) {
            result.append(processEntityEmpty((EntityEmpty) entity));
        }
        result.append(entity.getChildren().stream()
                .map(item -> preorderTraverse(item, id))
                .reduce(new StringBuilder(), StringBuilder::append));
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
        StringBuilder result = new StringBuilder();
        result.append("o ").append(entity.getName()).append("\n");
        return result;
    }

    private StringBuilder vertexToStringBuilder(Vector3f vertex){
        return new StringBuilder().append(vertex.x).append(" ").append(vertex.y).append(" ").append(vertex.z).append(" ");
    }

    private String entityTreeStructure(){
        StringBuilder result = new StringBuilder();
        for(int i=0; i<adjacencyList.size(); i++){
            for(int child: adjacencyList.get(i)){
                result.append("ee ").append(i).append(" ").append(child).append("\n");
            }
        }
        return result.toString();
    }
    private void clear(){
        adjacencyList = new ArrayList<>();
        vertices = new HashMap<>();
        vertexNumber = 1;
        modelNumber = 0;
    }
}
