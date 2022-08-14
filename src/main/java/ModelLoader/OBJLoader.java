package ModelLoader;

import EntityTree.Entity;
import EntityTree.EntityEmpty;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OBJLoader implements Loader {
    private final OBJParser objParser;

    public OBJLoader() {
        objParser = new OBJParser();
    }

    @Override
    public Entity load(String path) {
        Entity root = new EntityEmpty();
        ParsingResult parsingResult = objParser.load(path);
        if(parsingResult != null) {
            if (parsingResult.adjacencyList == null) {
                parsingResult.adjacencyList =
                        (ArrayList<Integer>) IntStream.of(new int[parsingResult.readObjects.size()]).boxed().collect(Collectors.toList());
            }
            parsingResult.readObjects.set(0, root);

            for (int i = 1; i < parsingResult.adjacencyList.size(); i++) {
                parsingResult.readObjects.get(i).setParent(parsingResult.readObjects.get(parsingResult.adjacencyList.get(i)));
            }
        }
        return root;
    }
}
