import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiViewport;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiDockNodeFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;

public class Main extends Application {
    @Override
    protected void configure(Configuration config) {
        config.setTitle("Dear ImGui is Awesome!");
    }

    @Override
    protected void initImGui(final Configuration config){
        super.initImGui(config);

        final ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
    }

    @Override
    public void process() {
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

        int dockspace_id = ImGui.getID("MyDockSpace");
        ImGui.dockSpace(dockspace_id, 0,0, ImGuiDockNodeFlags.PassthruCentralNode);

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

        ImGui.end();

        ImGui.begin("TestWindow", new ImBoolean(true), 0);
        ImGui.text("test");
        ImGui.end();

        ImGui.begin("TestWindow2", new ImBoolean(), 0);
        ImGui.text("test2");
        ImGui.end();
    }

    public static void main(String[] args) {
        launch(new Main());
    }
}