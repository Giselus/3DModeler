package Scene;

import EntityTree.Entity;

public class SceneState {
    //TODO change from SceneController
    private Entity root;
    private Entity selectedEntity = null;

    public SceneState() {}
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
}
