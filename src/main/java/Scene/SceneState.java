package Scene;

import EntityTree.Entity;
import UtilsCommon.Camera;

public class SceneState {
    private Entity root = null;
    private Entity selectedEntity = null;
    private Camera camera = null;

    private final int sceneWindowWidth = 1200;
    private final int sceneWindowHeight = 800;
    private int sceneTexture;


    public Entity getRoot() {
        return root;
    }
    public void setRoot(Entity root) {
        this.root = root;
    }

    public Entity getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(Entity selectedEntity) {
        this.selectedEntity = selectedEntity;
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

    public void setCamera(Camera camera){
        this.camera = camera;
    }
}
