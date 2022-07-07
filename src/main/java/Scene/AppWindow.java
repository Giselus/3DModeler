package Scene;

import Windows.EntitiesWindow;
import Windows.InspectorWindow;
import Windows.MainWindow;
import Windows.SceneWindow;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class AppWindow implements IUpdater {

    private final SceneState sceneState;
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private long mainWindow;
    private String glslVersion;

    public AppWindow(SceneState sceneState) {
        this.sceneState = sceneState;
        initWindow();
        initImGui();

        imGuiGlfw.init(mainWindow, true);
        imGuiGl3.init(glslVersion);
    }

    @Override
    public void update() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        MainWindow.show();
        SceneWindow.show(sceneState.getSceneWindowWidth(), sceneState.getSceneWindowHeight(), sceneState.getSceneTexture());
        EntitiesWindow.show(sceneState.getRoot(), sceneState);
        InspectorWindow.show(sceneState);

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    private void initWindow(){
        if(!glfwInit())
            return;
        glslVersion = "#version 130";
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_CORE_PROFILE);

        mainWindow = glfwCreateWindow(sceneState.getSceneWindowWidth(),sceneState.getSceneWindowHeight(),"Title==null",NULL,NULL);

        glfwMakeContextCurrent(mainWindow);
        glfwSwapInterval(1);
        glfwShowWindow(mainWindow);

        GL.createCapabilities();
    }

    private void initImGui(){
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        ImGui.styleColorsDark();
    }

    public void destroy(){
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(mainWindow);
        glfwDestroyWindow(mainWindow);
        glfwTerminate();
    }

//    public int getWidth(){
//        return width;
//    }
//    public int getHeight(){
//        return height;
//    }
    public long getMainWindow(){
        return mainWindow;
    }
}
