package Windows;

import Controllers.RenderingController;
import imgui.ImGui;
import imgui.type.ImBoolean;

public class SceneWindow {
    static public void show(final int width, final int height) {
        ImGui.begin("SceneWindow", new ImBoolean(true), 0);
        ImGui.getWindowDrawList().addImage(
                RenderingController.getInstance().getSceneTexture(),
                0,0,
                width,
                height,
                0,1,1,0
        );
        ImGui.end();
    }
}