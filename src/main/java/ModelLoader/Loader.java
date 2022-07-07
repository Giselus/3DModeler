package ModelLoader;

import EntityTree.Entity;
import Scene.RenderingUpdater;
import UtilsModel.Model;

import java.util.LinkedList;

public interface Loader {
    Entity load(String path);
}