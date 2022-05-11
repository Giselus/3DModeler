package UtilsCommon;

import java.util.Collection;
import java.util.LinkedList;

public abstract class Entity {
    private final Transform transform;
    private Entity parent;
    private final Collection<Entity> children;

    public Entity() {
        transform = new Transform();
        children = new LinkedList<>();
        parent = null;
    }

    public void setParent(Entity parent) {
        if(parent != null) {
            parent.removeChild(this);
            this.parent = null;
        }

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

    private void removeChild(Entity child) {
        children.remove(child);
    }
}
