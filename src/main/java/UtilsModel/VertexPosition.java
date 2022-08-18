package UtilsModel;

import org.joml.Vector3f;

import java.util.ArrayList;

public class VertexPosition {
    private final Vector3f value;
    private boolean isPicked = false;
    private ArrayList<VertexInstance> instances = new ArrayList<>();
    public VertexPosition(Vector3f value){
        this.value = value;
    }
    public Vector3f getValue(){
        return new Vector3f(value);
    }
    public void setValue(Vector3f value) {
        this.value.x = value.x;
        this.value.y = value.y;
        this.value.z = value.z;
        for(VertexInstance instance: instances){
            instance.getFace().recalculateNormal();
        }
    }
    public void pick(){
        isPicked = true;
    }
    public void unpick() { isPicked = false; }
    public boolean isPicked(){
        return isPicked;
    }

    public void addInstance(VertexInstance instance){
        instances.add(instance);
    }

    public void removeInstance(VertexInstance instance){
        instances.remove(instance);
    }

    public ArrayList<VertexInstance> getInstances(){
        return instances;
    }
}
