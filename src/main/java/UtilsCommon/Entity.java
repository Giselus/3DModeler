package UtilsCommon;

import imgui.ImGui;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public abstract class Entity {
    private final Transform transform;
    private Entity parent;
    private final Collection<Entity> children;
    private String name;

    public Entity() {
        transform = new Transform();
        children = new LinkedList<>();
        parent = null;
        name = "Entity";
    }

    public void setParent(Entity parent) {
        if(this.parent != null) {
            this.parent.removeChild(this);
        }

        this.parent = parent;

        if(parent != null) {
            parent.addChild(this);
        }
    }

    public Collection<Entity> getUnmodifiableChildren() {
        return Collections.unmodifiableCollection(children);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void addChild(Entity child) {
        children.add(child);
    }

    private void removeChild(Entity child) {
        children.remove(child);
    }

    public void showInspector(){
        transform.showInspector();
    }
}
