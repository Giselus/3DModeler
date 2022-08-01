package EntityTree;


import Scene.IRenderer;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class Entity {
    protected final Transform transform;
    protected Entity parent;
    protected final Collection<Entity> children;
    protected String name;

    public Entity(Entity parent){
        this.transform = new Transform();
        this.children = new LinkedList<>();
        this.parent = null;
        this.name = "Entity";
        setParent(parent);
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

    public void drawSelfAndChildren(IRenderer renderer) {
        for(Entity child : children)
            child.drawSelfAndChildren(renderer);
    }

    public Collection<Entity> getChildren() {
        return children;
    }
}
