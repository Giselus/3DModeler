package Controllers;

import static org.lwjgl.glfw.GLFW.*;

public class InputController {

    private InputController(){

    }

    private static InputController instance;

    public static InputController getInstance(){
        if(instance == null)
            instance = new InputController();
        return instance;
    }

    public void initialize(){
        glfwSetCursorPosCallback(UIController.getInstance().getMainWindow(), this::mouse_callback);
        glfwSetScrollCallback(UIController.getInstance().getMainWindow(), this::scroll_callback);
        //glfwSetInputMode(Controllers.UIController.getInstance().getMainWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    public void update(){
        process_input(UIController.getInstance().getMainWindow());
    }

    private boolean firstMouse = true;
    float lastX;
    float lastY;

    private void mouse_callback(long window, double posXDouble, double posYDouble){
        float posX = (float)posXDouble;
        float posY = (float)posYDouble;
        if(firstMouse){
            firstMouse = false;
            lastX = posX;
            lastY = posY;
        }
        float offsetX = posX - lastX;
        float offsetY = lastY - posY;

        lastX = posX;
        lastY = posY;
        if(glfwGetMouseButton(window,GLFW_MOUSE_BUTTON_3) == GLFW_PRESS)
            RenderingController.getInstance().getCamera().ProcessMousePosition(offsetX,offsetY);
    }

    private void scroll_callback(long window, double offsetXDouble, double offsetYDouble){
        RenderingController.getInstance().getCamera().ProcessMouseScroll((float)offsetYDouble);
    }

    private void process_input(long window){
    }

}
