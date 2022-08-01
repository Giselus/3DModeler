package Windows;

import EntityTree.Entity;
import Scene.SceneState;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImBoolean;

public class EntitiesWindow {
    static public void show(Entity root, SceneState sceneState) {
        ImGui.begin("Entities", new ImBoolean(), 0);
        int baseFlags = ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.OpenOnDoubleClick |
                ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.DefaultOpen;
        showEntitiesTree(root, baseFlags, sceneState);
        ImGui.end();
    }

    static private boolean showEntitiesTree(final Entity entity, final int baseFlags, SceneState sceneState) {
        int nodeFlags = baseFlags;
        if(entity.getUnmodifiableChildren().size() == 0)
            nodeFlags |= ImGuiTreeNodeFlags.Bullet;
        if(entity == sceneState.getSelectedEntity())
            nodeFlags |= ImGuiTreeNodeFlags.Selected;
        ImGui.pushID(entity.getIndex());
        if(ImGui.treeNodeEx(entity.getName(), nodeFlags)) {
            if(ImGui.isItemClicked()){
                sceneState.setSelectedEntity(entity);
            }

            if(ImGui.beginDragDropSource()) {
                ImGui.setDragDropPayload("ENTITY_NODE", entity, 0);
                ImGui.text(entity.getName());
                ImGui.endDragDropSource();
            }

            if(ImGui.beginDragDropTarget()) {
                Entity payload = ImGui.acceptDragDropPayload("ENTITY_NODE");
                if(payload != null) {
                    payload.setParent(entity);
                    ImGui.treePop();
                    ImGui.popID();
                    return false;
                }
                ImGui.endDragDropTarget();
            }

            for(Entity child : entity.getUnmodifiableChildren())
                if(!showEntitiesTree(child, baseFlags, sceneState)) {
                    ImGui.treePop();
                    ImGui.popID();
                    return false;
                }

            ImGui.treePop();
        }
        ImGui.popID();
        return true;
    }
}
