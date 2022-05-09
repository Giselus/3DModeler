package UtilsCommon;

import java.util.ArrayList;

public class Entity extends Model {
    private final Transform transform;
    private Entity parent;
    private final ArrayList<Entity> children;

    public Entity(ArrayList<VertexPosition> Vertices, ArrayList<Face> Faces) {
        super(Vertices, Faces);
        transform = new Transform();
        children = new ArrayList<>();
    }

    public void setParent(Entity parent) {
        this.parent = parent;
        parent.addChild(this);
    }

    public void updateSelfAndChildren() {
        if(parent != null) {
            transform.updateGlobalModelMatrix(parent.transform.getGlobalModelMatrix());
        } else {
            transform.updateGlobalModelMatrix();
        }

        for (Entity child : children) {
            child.updateSelfAndChildren();
        }
    }

    public Transform getTransform() {
        return transform;
    }

    private void addChild(Entity child) {
        children.add(child);
    }
}
