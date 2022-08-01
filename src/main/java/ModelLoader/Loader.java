package ModelLoader;

import EntityTree.Entity;

public interface Loader {
    Entity load(String path);
}