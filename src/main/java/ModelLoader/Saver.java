package ModelLoader;

import EntityTree.Entity;

public interface Saver {
    void save(Entity entity, String dst, SaveMode saveMode);
}
