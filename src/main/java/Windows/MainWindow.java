package Windows;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiDockNodeFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;

public class MainWindow {
    static public void show() {
        ImGuiIO io = ImGui.getIO();

        ImGui.setNextWindowPos(0,0);
        ImGui.setNextWindowSize(io.getDisplaySizeX(),io.getDisplaySizeY());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding,0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize,0);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding,0,0);

        ImGui.begin("MainWindow", new ImBoolean(), ImGuiWindowFlags.MenuBar |
                ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus |
                ImGuiWindowFlags.NoNavFocus | ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoDecoration);

        ImGui.popStyleVar(3);

        int dockSpaceId = ImGui.getID("MyDockSpace");
        ImGui.dockSpace(dockSpaceId, 0,0, ImGuiDockNodeFlags.PassthruCentralNode);

        showMainMenuBar();

        ImGui.end();
    }

    static private void showMainMenuBar() {
        if(ImGui.beginMainMenuBar()) {
            if(ImGui.beginMenu("File")){
                ImGui.endMenu();
            }
            if(ImGui.beginMenu("Windows")){
                ImGui.endMenu();
            }
            if(ImGui.beginMenu("Help")){
                ImGui.endMenu();
            }
        }
        ImGui.endMainMenuBar();
    }
}
