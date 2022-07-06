package Application;

import ModelLoader.Loader;
import ModelLoader.OBJLoader;
import Scene.AppWindow;
import Scene.InputController;
import Scene.RenderingUpdater;
import Scene.SceneState;
import Windows.InspectorWindow;
import Windows.SceneWindow;

import static org.lwjgl.glfw.GLFW.*;

public class App {
    private final String INIT_FILE = "src/main/data/cube.obj";
    private SceneState sceneState;
    private AppWindow appWindow;
    private RenderingUpdater renderingUpdater;
    private InputController inputController;

    public void run() {

        initialize();
        while(!glfwWindowShouldClose(appWindow.getMainWindow())) {
            renderingUpdater.update();
            appWindow.update();

            glfwSwapBuffers(appWindow.getMainWindow());
            glfwPollEvents();
        }
        appWindow.destroy();
    }

    private void initialize() {
        sceneState = new SceneState();
        appWindowInit();
        renderingUpdaterInit();
        sceneState.setRoot(new OBJLoader().load(INIT_FILE, renderingUpdater));
        inputControllerInit();
    }

    private void sceneStateInit() {
        sceneState = new SceneState();
    }

    private void appWindowInit() {
        appWindow = new AppWindow(sceneState);
    }

    private void renderingUpdaterInit() {
        renderingUpdater = new RenderingUpdater(sceneState, appWindow);
        SceneWindow.renderingUpdater = renderingUpdater;
    }

    private void inputControllerInit() {
        inputController = new InputController(appWindow, renderingUpdater);
    }
}
