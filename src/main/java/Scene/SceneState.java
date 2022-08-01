package Scene;

import EntityTree.Entity;
import UtilsCommon.Camera;

public class SceneState {
    //TODO change from SceneController
    private Entity root;
    private Entity selectedEntity = null;
    private Camera camera = new Camera();

    private int sceneWindowWidth = 1200;
    private int sceneWindowHeight = 800;
    private int sceneTexture;

    public void setRoot(Entity root) {
        this.root = root;
    }
    public Entity getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(Entity selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public Entity getRoot() {
        return root;
    }

    public int getSceneWindowWidth(){
        return sceneWindowWidth;
    }

    public int getSceneWindowHeight(){
        return sceneWindowHeight;
    }

    public int getSceneTexture() {
        return sceneTexture;
    }

    public void setSceneTexture(int sceneTexture) {
        this.sceneTexture = sceneTexture;
    }

    public Camera getCamera(){return camera;}
}
