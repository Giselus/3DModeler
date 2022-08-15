package Windows;

import EntityTree.Entity;
import EntityTree.EntityEmpty;
import Scene.SceneState;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class InspectorWindow {

    static public void show(SceneState sceneState) {
        ImGui.begin("Inspector", new ImBoolean(), 0);
        if(sceneState.getMainSelectedEntity() != null)
            showInspector(sceneState.getMainSelectedEntity());
        ImGui.end();
    }

    static private void showInspector(Entity entity) {
        final int baseFlags = 0;

        if(ImGui.treeNodeEx("Properties", baseFlags)) {
            ImGui.text("Name: ");
            ImGui.sameLine();
            ImString resultString = new ImString(entity.getName(), 256);
            if (ImGui.inputText("Name: ", resultString)) {
                entity.setName(resultString.get());
            }
            ImGui.treePop();
        }

        if(ImGui.treeNodeEx("Transform", baseFlags)) {
            final int transformFlags = baseFlags | ImGuiTreeNodeFlags.DefaultOpen;
            show3DSetter("Position",
                    () -> entity.getTransform().getLocalTranslation(),
                    (val) -> entity.getTransform().setLocalTranslation(val),
                    transformFlags, 0.005f);
            show3DSetter("Scale",
                    () -> entity.getTransform().getLocalScale(),
                    (val) -> entity.getTransform().setLocalScale(val),
                    transformFlags, 0.005f);
            show3DSetter("Rotation",
                    () -> entity.getTransform().getLocalRotation(),
                    (val) -> entity.getTransform().setLocalRotation(val),
                    transformFlags, 0.1f);
            entity.updateSelfAndChildren();
            ImGui.treePop();
        }

        if(ImGui.treeNodeEx("Tools", baseFlags)) {
            if(ImGui.button("Add child node")) {
                Entity node = new EntityEmpty();
                node.setName("Node");
                node.setParent(entity);
            }
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
