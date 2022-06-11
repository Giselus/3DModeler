package Windows;

import Controllers.SceneController;
import EntityTree.Entity;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImBoolean;

public class EntitiesWindow {
    static public void show(Entity root) {
        ImGui.begin("Entities", new ImBoolean(), 0);
        int baseFlags = ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.OpenOnDoubleClick |
                ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.DefaultOpen;
        showEntitiesTree(root, baseFlags);
        ImGui.end();
    }

    static private void showEntitiesTree(final Entity entity, final int baseFlags) {
        int nodeFlags = baseFlags;
        if(entity.getUnmodifiableChildren().size() == 0)
            nodeFlags |= ImGuiTreeNodeFlags.Bullet;
        if(entity == SceneController.getInstance().getSelectedEntity())
            nodeFlags |= ImGuiTreeNodeFlags.Selected;
        if(ImGui.treeNodeEx(entity.getName(), nodeFlags)) {
            if(ImGui.isItemClicked()){
                SceneController.getInstance().setSelectedEntity(entity);
            }
            for(Entity child : entity.getUnmodifiableChildren())
                showEntitiesTree(child, baseFlags);

            ImGui.treePop();
        }
    }
}
