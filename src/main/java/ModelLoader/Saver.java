package ModelLoader;

import EntityTree.Entity;

public interface Saver {
    boolean save(Entity entity, String dst, SaveMode saveMode);
}
