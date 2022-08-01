package Application;

import ModelLoader.OBJLoader;
import Scene.*;

import java.util.function.BiConsumer;

import static org.lwjgl.glfw.GLFW.*;

public class App {
    //TODO: delete
    private final String INIT_FILE = "src/main/data/szybkaFura.obj";

    private SceneState sceneState;

    private IGraphicEngine graphicEngine;
    private IAppWindow appWindow;
    private IRenderer renderer;
    private IInput input;

    public void run() {
        initialize();

        while(!appWindow.shouldBeClosed()){
            renderer.startFrame();

            //render models here
            sceneState.getRoot().drawSelfAndChildren(renderer);

            renderer.renderGUI();
        }
        graphicEngine.destroy();
//        while(!glfwWindowShouldClose(appWindow.getMainWindow())) {
//            renderingUpdater.update();
//            appWindow.update();
//
//            glfwSwapBuffers(appWindow.getMainWindow());
//            glfwPollEvents();
//        }
//        appWindow.destroy();
    }

    private void initialize() {
        sceneStateInit();

        graphicEngine = new OpenGLEngine(sceneState);
        renderer = graphicEngine.getRenderer();
        appWindow = graphicEngine.getAppWindow();
        input = graphicEngine.getInput();

        appWindow.setHeight(sceneState.getSceneWindowHeight());
        appWindow.setWidth(sceneState.getSceneWindowWidth());

        graphicEngine.initialize();

    }

    private void sceneStateInit() {
        sceneState = new SceneState();
        sceneState.setRoot(new OBJLoader().load(INIT_FILE));
    }

}
