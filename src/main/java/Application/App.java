package Application;

import EntityTree.EntityModel;
import ModelLoader.OBJLoader;
import OpenGLImpl.OpenGLEngine;
import Scene.*;
import UtilsCommon.Camera;

public class App {
    private final String INIT_FILE = "src/main/data/sphere.obj";

    private SceneState sceneState;

    private IGraphicEngine graphicEngine;
    private IAppWindow appWindow;
    private IRenderer renderer;
    private IInput input;
    private Camera camera;
    private InputRegisterer inputRegisterer;
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

        inputRegisterer = new InputRegisterer(input,sceneState);
    }

    private void sceneStateInit() {
        sceneState = new SceneState();
        sceneState.setRoot(new OBJLoader().load(INIT_FILE));
    }

}
