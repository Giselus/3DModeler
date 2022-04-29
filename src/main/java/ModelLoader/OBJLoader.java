package ModelLoader;

import UtilsCommon.Vertex;
import UtilsCommon.Mesh;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.File;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class OBJLoader implements Loader {
    private boolean firstFace = true;
    private ArrayList<Vector3f> Vertices = new ArrayList<>();
    private ArrayList<Vector3f> Normals = new ArrayList<>();
    private ArrayList<Vector2f> Tex = new ArrayList<>();
    private ArrayList<Integer> Indices = new ArrayList<>();
    private ArrayList<Vertex> CreatedVertices = new ArrayList<>();
    private LinkedList<Mesh> ReadObjects;

    @Override
    public LinkedList<Mesh> load(String path) {
        clear();
        ReadObjects = new LinkedList<>();
        File file;
        try {
            file = new File(path);
        } catch (Exception e) {
            System.out.println("File: " + path + " has not been found.");
            return null;
        }
        try {
            readFile(file);
        } catch (Exception e){
            System.out.println("An error has occurred while reading file " + path);
            return null;
        }
        return ReadObjects;
    }
    private void readFile(File file) throws RuntimeException, FileNotFoundException {
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
                ReadObjects.add(new Mesh(CreatedVertices, Indices));
                clear();
                continue;
            }
            switch (splintedLine[0]) {
                case "v" -> Vertices.add(new Vector3f(
                        Float.parseFloat(splintedLine[1]),
                        Float.parseFloat(splintedLine[2]),
                        Float.parseFloat(splintedLine[3])));
                case "vn" -> Normals.add(new Vector3f(
                        Float.parseFloat(splintedLine[1]),
                        Float.parseFloat(splintedLine[2]),
                        Float.parseFloat(splintedLine[3])));
                case "vt" -> Tex.add(new Vector2f(
                        Float.parseFloat(splintedLine[1]),
                        Float.parseFloat(splintedLine[2])));
                case "f" -> readFace(splintedLine);
            }
        }
        ReadObjects.add(new Mesh(CreatedVertices, Indices));
    }
    private void readFace(String[] line){
        if(firstFace){
            CreatedVertices = new ArrayList<>();
            for(int i = 0; i < Vertices.size(); i++){
                CreatedVertices.add(null);
            }
            firstFace = false;
        }
        for(int i = 1; i <= 3; i++){
            createVertex(line[i].split("/"));
        }
    }

    private void createVertex(String[] line){
        int index = Integer.parseInt(line[0]) - 1;
        Indices.add(index);
        if(CreatedVertices.get(index) != null){
            return;
        }
        Vector3f cords = Vertices.get(index);
        Vector2f texture = Tex.get(Integer.parseInt(line[1])-1);
        Vector3f norm = Normals.get(Integer.parseInt(line[2])-1);
        CreatedVertices.set(index, new Vertex(cords, norm, texture));
    }

    private void clear(){
        firstFace = true;
        Vertices = new ArrayList<>();
        CreatedVertices = new ArrayList<>();
        Tex = new ArrayList<>();
        Normals = new ArrayList<>();
        Indices = new ArrayList<>();
    }
}
