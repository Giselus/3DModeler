package Scene;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

public class OpenGLEngine extends IGraphicEngine{
//    private GLFWAppWindow appWindow;
//    private OpenGLRenderer renderer;
//    private GLFWInput input;

    private String glslVersion = "#version 130";

    public OpenGLEngine(SceneState sceneState){
        super(sceneState);
        appWindow = new GLFWAppWindow();
        renderer = new OpenGLRenderer(sceneState, (GLFWAppWindow) appWindow, glslVersion);
        input = new GLFWInput((GLFWAppWindow) appWindow, sceneState);
    }

}
