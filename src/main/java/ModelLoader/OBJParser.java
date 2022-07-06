package ModelLoader;

import Scene.RenderingUpdater;
import UtilsModel.Face;
import UtilsModel.Model;
import UtilsModel.VertexPosition;
import org.joml.Vector3f;

import java.io.File;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class OBJParser {
    private LinkedList<Model> readObjects;
    private ArrayList<VertexPosition> cords;
    private ArrayList<Face> faces;

    public LinkedList<Model> load(String path, RenderingUpdater renderingUpdater) {
        clear();
        readObjects = new LinkedList<>();
        File file;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("File: " + path + " has not been found.");
            return null;
        }
        try {
            readFile(file, renderingUpdater);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("An error has occurred while reading file " + path);
            return null;
        }
        return readObjects;
    }
    private void readFile(File file, RenderingUpdater renderingUpdater) throws RuntimeException, FileNotFoundException {
        boolean firstObject = true;
        Scanner scanner = new Scanner(file);
        String line;
        while(scanner.hasNext()){
            line = scanner.nextLine();
            if(line == null){
                continue;
            }
            String[] splintedLine = line.split(" ");
            if(splintedLine[0].equals("o")){
                if(firstObject){
                    firstObject = false;
                    continue;
                }
                readObjects.add(new Model(cords, faces, renderingUpdater));
                clear();
                continue;
            }
            switch (splintedLine[0]) {
                case "v" -> readVertexPosition(splintedLine);
                case "vn" -> readNormals(splintedLine);
                case "vt" -> readTextures(splintedLine);
                case "f" -> readFace(splintedLine);
            }
        }
        readObjects.add(new Model(cords, faces, renderingUpdater));
    }
    private void readVertexPosition(String[] line){
        Vector3f cords = new Vector3f(
                Float.parseFloat(line[1]),
                Float.parseFloat(line[2]),
                Float.parseFloat(line[3]));
        this.cords.add(new VertexPosition(cords));
    }

    private void readNormals(String[] line){
        // TODO read if it useful for something
    }
    private void readTextures(String[] line){
        // TODO read if it useful for something
    }
    private void readFace(String[] line){
        ArrayList<VertexPosition> tempVertices = new ArrayList<>();
        for(int i=1; i<=3; i++){
            String[] oneVertex = line[i].split("/");
            tempVertices.add(cords.get(Integer.parseInt(oneVertex[0]) - 1));
        }
        faces.add(new Face(tempVertices));
    }
    private void clear(){
        if(cords == null)
            cords = new ArrayList<>();
        faces = new ArrayList<>();
    }
}
