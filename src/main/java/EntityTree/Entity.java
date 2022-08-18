package EntityTree;


import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.function.Consumer;

public abstract class Entity {
    protected Transform transform;
    protected Entity parent;
    protected final Collection<Entity> children;
    protected String name;
    protected final int index;
    static int maxIndex = 0;

    public Entity(Entity parent){
        this.transform = new Transform();
        this.children = new LinkedList<>();
        this.parent = null;
        this.name = "Entity";
        this.index = maxIndex++;
        setParent(parent);
    }

    public void setParent(Entity parent) {
        Entity tempEnt = parent;
        while(tempEnt != null) {
            if(tempEnt == this) {
                System.out.println("Cannot set child as a parent");
                return;
            }
            tempEnt = tempEnt.parent;
        }

        if(this.parent != null) {
            this.parent.removeChild(this);
        }

        this.parent = parent;

        if(parent != null) {
            parent.addChild(this);
        }
    }

    public Entity getParent() {
        return parent;
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

    public void setTransform(Transform transform) { this.transform = transform; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() { return index; }

    private void addChild(Entity child) {
        children.add(child);
    }

    private void removeChild(Entity child) {
        children.remove(child);
    }

    public void invokeFunctionOnSubtree(Consumer<Entity> f){
        f.accept(this);
        for(Entity child: children)
            child.invokeFunctionOnSubtree(f);
    }

    public Collection<Entity> getChildren() {
        return children;
    }
}
