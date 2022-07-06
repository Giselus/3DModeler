package Scene;

import static org.lwjgl.glfw.GLFW.*;

public class InputController {
    //TODO from input controller

    private final AppWindow appWindow;
    private final RenderingUpdater renderingUpdater;
    public InputController(AppWindow appWindow, RenderingUpdater renderingUpdater){
        this.appWindow = appWindow;
        this.renderingUpdater = renderingUpdater;

        initialize();
    }

    public void initialize() {
        glfwSetCursorPosCallback(appWindow.getMainWindow(), this::mouse_callback);
        glfwSetScrollCallback(appWindow.getMainWindow(), this::scroll_callback);
        //glfwSetInputMode(appWindow.getMainWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    public void update(){
        process_input(appWindow.getMainWindow());
    }

    private boolean firstMouse = true;
    float lastX;
    float lastY;

    private void mouse_callback(long window, double posXDouble, double posYDouble) {
        float posX = (float)posXDouble;
        float posY = (float)posYDouble;
        if(firstMouse) {
            firstMouse = false;
            lastX = posX;
            lastY = posY;
        }
        float offsetX = posX - lastX;
        float offsetY = lastY - posY;

        lastX = posX;
        lastY = posY;
        if(glfwGetMouseButton(window,GLFW_MOUSE_BUTTON_3) == GLFW_PRESS)
            renderingUpdater.getCamera().ProcessMousePosition(offsetX,offsetY);
    }

    private void scroll_callback(long window, double offsetXDouble, double offsetYDouble) {
        renderingUpdater.getCamera().ProcessMouseScroll((float)offsetYDouble);
    }

    private void process_input(long window) {
    }
}
