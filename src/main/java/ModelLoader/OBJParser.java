package ModelLoader;

import EntityTree.Entity;
import EntityTree.EntityModel;
import UtilsModel.Face;
import UtilsModel.Mesh;
import UtilsModel.VertexPosition;
import org.joml.Vector3f;

import java.io.File;

import java.io.FileNotFoundException;
import java.util.*;

public class OBJParser {
    private ArrayList<Entity> readObjects;
    private ArrayList<VertexPosition> cords;
    private ArrayList<VertexPosition> currCords;
    private ArrayList<Face> faces;
    private ArrayList<Integer> adjacencyList;
    String modelName;
    ParsingResult load(String path) {
        cords = new ArrayList<>();
        adjacencyList = null;
        clear();
        readObjects = new ArrayList<>();
        readObjects.add(null);
        File file = new File(path);
        if(!file.canRead()){
            return null;
        }
        try {
            readFile(file);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("An error has occurred while reading file " + path);
            return null;
        }
        return new ParsingResult(readObjects, adjacencyList);
    }
    private void readFile(File file) throws RuntimeException, FileNotFoundException {
        boolean firstObject = true;
        Scanner scanner = new Scanner(file);
        String line;
        while(scanner.hasNext()){
            line = scanner.nextLine();
            if(line.length() == 0){
                continue;
            }
            String[] splintedLine = line.split(" ");
            if(splintedLine[0].equals("o")){
                if(firstObject){
                    firstObject = false;
                    modelName = splintedLine[1];
                    continue;
                }

                var temp = new EntityModel(new Mesh(currCords, faces), null);
                temp.setName(modelName);
                readObjects.add(temp);

                modelName = splintedLine[1];
                clear();
                continue;
            }
            switch (splintedLine[0]) {
                case "v" -> readVertexPosition(splintedLine);
                case "f" -> readFace(splintedLine);
                case "ee" -> readEdge(splintedLine);
            }
        }
        var temp = new EntityModel(new Mesh(currCords, faces), null);
        temp.setName(modelName);
        readObjects.add(temp);
    }
    private void readVertexPosition(String[] line){
        Vector3f cords = new Vector3f(
                Float.parseFloat(line[1]),
                Float.parseFloat(line[2]),
                Float.parseFloat(line[3]));
        this.cords.add(new VertexPosition(cords));
        this.currCords.add(new VertexPosition(cords));
    }

    private void readFace(String[] line){
        ArrayList<VertexPosition> tempVertices = new ArrayList<>();
        int limit = line.length;
        for(int i=1; i<limit; i++){
            String[] oneVertex = line[i].split("/");
            tempVertices.add(cords.get(Integer.parseInt(oneVertex[0]) - 1));
        }
        faces.add(new Face(tempVertices));
    }

    private void readEdge(String[] line){
        if(adjacencyList == null){
            adjacencyList = new ArrayList<>();
        }
        int parent = Integer.parseInt(line[1]);
        int children = Integer.parseInt(line[2]);
        while(adjacencyList.size() <= children){
            adjacencyList.add(-1);
        }
        adjacencyList.set(children, parent);
    }
    private void clear(){
        currCords = new ArrayList<>();
        faces = new ArrayList<>();
    }
}

class ParsingResult {
    ArrayList<Entity> readObjects;
    ArrayList<Integer> adjacencyList;

    ParsingResult(ArrayList<Entity> readObjects, ArrayList<Integer> adjacencyList){
        this.readObjects = readObjects;
        this.adjacencyList = adjacencyList;
    }
}