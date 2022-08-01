package Application;

import EntityTree.EntityModel;
import ModelLoader.OBJLoader;
import Scene.*;
import UtilsCommon.Camera;

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
    private Camera camera;
    private Editor editor;
    public void run() {
        initialize();

        while(!appWindow.shouldBeClosed()){
            input.runContinuousCallbacks();
            renderer.startFrame();

            //render models here
            sceneState.getRoot().invokeFunctionOnSubtree(entity -> {
                if (entity instanceof EntityModel){
                    renderer.render((EntityModel) entity);
                }
            });
            renderer.renderGUI();
            input.processInput();
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
        camera = new Camera(input,sceneState);
        sceneState.setCamera(camera);

        editor = new Editor(input,sceneState);
    }

    private void sceneStateInit() {
        sceneState = new SceneState();
        sceneState.setRoot(new OBJLoader().load(INIT_FILE));
    }

}
