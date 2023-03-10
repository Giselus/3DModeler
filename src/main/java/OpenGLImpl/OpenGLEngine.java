package OpenGLImpl;

import Scene.IGraphicEngine;
import Scene.SceneState;

public class OpenGLEngine extends IGraphicEngine {

    private String glslVersion = "#version 130";

    public OpenGLEngine(SceneState sceneState){
        super(sceneState);
        appWindow = new GLFWAppWindow();
        renderer = new OpenGLRenderer(sceneState, (GLFWAppWindow) appWindow, glslVersion);
        input = new GLFWInput((GLFWAppWindow) appWindow, sceneState);
    }

}
