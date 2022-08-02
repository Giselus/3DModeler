package ModelLoader;

import EntityTree.Entity;
import EntityTree.EntityEmpty;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class EOBJLoader implements Loader {
    ArrayList<ArrayList<Integer>> childrenList;
    @Override
    public Entity load(String path) {
        var modelList = new OBJParser().load(path);
        childrenList = new ArrayList<>(modelList.size());

        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            String line;
            while(scanner.hasNext()){
                line = scanner.nextLine();
                if(line == null){
                    continue;
                }
                String[] splintedLine = line.split(" ");
                if(splintedLine[0].equals("ee")){
                    childrenList.get(Integer.parseInt(splintedLine[1])).add(Integer.parseInt(splintedLine[2]));
                }
            }
            Entity root = new EntityEmpty();

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
