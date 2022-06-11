package Windows;

import Controllers.SceneController;
import EntityTree.Entity;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImBoolean;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class InspectorWindow {
    static public void show() {
        ImGui.begin("Inspector", new ImBoolean(), 0);
        if(SceneController.getInstance().getSelectedEntity() != null)
            showInspector(SceneController.getInstance().getSelectedEntity());
        ImGui.end();
    }

    static private void showInspector(Entity entity) {
        final int baseFlags = ImGuiTreeNodeFlags.DefaultOpen;
        if(ImGui.treeNodeEx("Transform",baseFlags)) {
            show3DSetter("Position",
                    () -> entity.getTransform().getLocalTranslation(),
                    (val) -> entity.getTransform().setLocalTranslation(val),
                    baseFlags, 0.005f);
            show3DSetter("Scale",
                    () -> entity.getTransform().getLocalScale(),
                    (val) -> entity.getTransform().setLocalScale(val),
                    baseFlags, 0.005f);
            show3DSetter("Rotation",
                    () -> entity.getTransform().getLocalRotation(),
                    (val) -> entity.getTransform().setLocalRotation(val),
                    baseFlags, 0.1f);
            entity.updateSelfAndChildren();
            ImGui.treePop();
        }
    }

    static private void show3DSetter(String name, Supplier<Vector3fc> getter, Consumer<Vector3f> setter,
                              final int baseFlags, final float step) {
        final float[] tmp = new float[1];
        if(ImGui.treeNodeEx(name,baseFlags)) {
            float x,y,z;
            tmp[0] = getter.get().x();
            ImGui.dragFloat("x",tmp,step);
            x = tmp[0];
            tmp[0] = getter.get().y();
            ImGui.dragFloat("y",tmp,step);
            y = tmp[0];
            tmp[0] = getter.get().z();
            ImGui.dragFloat("z",tmp,step);
            z = tmp[0];
            setter.accept(new Vector3f(x,y,z));
            ImGui.treePop();
        }
    }
}
