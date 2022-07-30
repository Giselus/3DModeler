package ModelLoader;

import EntityTree.Entity;
import EntityTree.EntityEmpty;
import EntityTree.EntityModel;

import java.util.LinkedList;

public class OBJLoader implements Loader {
    private final OBJParser objParser;

    public OBJLoader() {
        objParser = new OBJParser();
    }

    @Override
    public Entity load(String path) {
        Entity root = new EntityEmpty();
        LinkedList<NamedModel> models = objParser.load(path);
        for(NamedModel model : models)
                new EntityModel(model.model, root).setName(model.name);
        return root;
    }
}
