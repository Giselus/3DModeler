package UtilsCommon;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImFloat;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Transform {
    private Vector3fc localTranslation = new Vector3f(0, 0, 0);
    private Vector3fc localRotation = new Vector3f(0, 0, 0);
    private Vector3fc localScale = new Vector3f(1, 1, 1);

    private Matrix4f globalModelMatrix = new Matrix4f().identity();

    public Vector3fc getLocalTranslation() {
        return localTranslation;
    }

    public void setLocalTranslation(Vector3fc localTranslation) {
        this.localTranslation = localTranslation;
    }

    public Vector3fc getLocalRotation() {
        return localRotation;
    }

    public void setLocalRotation(Vector3fc localRotation) {
        this.localRotation = localRotation;
    }

    public Vector3fc getLocalScale() {
        return localScale;
    }

    public void setLocalScale(Vector3fc localScale) {
        this.localScale = localScale;
    }

    public void updateGlobalModelMatrix() {
        globalModelMatrix = getLocalModelMatrix();
    }

    public void updateGlobalModelMatrix(Matrix4fc parentGlobalModelMatrix) {
        parentGlobalModelMatrix.mul(getLocalModelMatrix(), globalModelMatrix);
    }

    public Matrix4fc getGlobalModelMatrix() {
        return globalModelMatrix;
    }

    private Matrix4f getLocalModelMatrix() {
        //TODO: rotation
        return new Matrix4f().identity().translate(localTranslation).scale(localScale);
    }

    public void showInspector(Entity entity){
        final int baseFlags = ImGuiTreeNodeFlags.DefaultOpen;
        final float[] tmp = new float[1];
        if(ImGui.treeNodeEx("Transform",baseFlags)){
            if(ImGui.treeNodeEx("Position",baseFlags)){
                float x,y,z;
                tmp[0] = localTranslation.x();
                ImGui.dragFloat("x",tmp,0.005f);
                x = tmp[0];
                tmp[0] = localTranslation.y();
                ImGui.dragFloat("y",tmp,0.005f);
                y = tmp[0];
                tmp[0] = localTranslation.z();
                ImGui.dragFloat("z",tmp,0.005f);
                z = tmp[0];
                localTranslation = new Vector3f(x,y,z);
                entity.updateSelfAndChildren();
                ImGui.treePop();
            }
            ImGui.treePop();
        }
    }
}
