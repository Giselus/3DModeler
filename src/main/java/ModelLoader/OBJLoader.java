package ModelLoader;

import EntityTree.Entity;
import EntityTree.EntityEmpty;
import EntityTree.EntityModel;
import Scene.RenderingUpdater;
import UtilsModel.Model;

import java.util.LinkedList;

public class OBJLoader implements Loader {
    private final OBJParser objParser;

    public OBJLoader() {
        objParser = new OBJParser();
    }

    @Override
    public Entity load(String path) {
        Entity root = new EntityEmpty();
        LinkedList<Model> models = objParser.load(path);
        for(Model model : models)
                new EntityModel(model, root);
        return root;
    }
}
