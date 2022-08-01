package Windows;

//import Controllers.RenderingController;
import imgui.ImGui;
import imgui.type.ImBoolean;

public class SceneWindow {
    static public void show(final int width, final int height, final int sceneTexture) {
        ImGui.begin("SceneWindow", new ImBoolean(true), 0);
        ImGui.getWindowDrawList().addImage(
                sceneTexture,
                0,0,
                width,
                height,
                0,1,1,0
        );
        ImGui.end();
    }
}
