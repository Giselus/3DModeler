package Scene;

import EntityTree.Entity;
import UtilsCommon.Camera;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SceneState {
    private Entity root = null;
    private Entity mainSelectedEntity = null;
    private final Set<Entity> selectedEntities = new HashSet<>();
    private Camera camera = null;

    private final int sceneWindowWidth = 1200;
    private final int sceneWindowHeight = 800;

    public Entity getRoot() {
        return root;
    }
    public void setRoot(Entity root) {
        this.root = root;
    }

    public Entity getMainSelectedEntity() {
        return mainSelectedEntity;
    }

    public void setMainSelectedEntity(Entity selectedEntity) {
        this.mainSelectedEntity = selectedEntity;
    }

    public Set<Entity> getSelectedEntities() {
        return Collections.unmodifiableSet(selectedEntities);
    }

    public void addSelectedEntity(Entity entity) {
        selectedEntities.add(entity);
    }

    public void clearSelectedEntities() {
        selectedEntities.clear();
    }

    public int getSceneWindowWidth(){
        return sceneWindowWidth;
    }

    public int getSceneWindowHeight(){
        return sceneWindowHeight;
    }

    public Camera getCamera(){return camera;}

    public void setCamera(Camera camera){
        this.camera = camera;
    }
}
