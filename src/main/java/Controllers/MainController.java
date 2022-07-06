package Controllers;

import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.*;

public class MainController{

    private static MainController instance;

    public static MainController getInstance(){
        if(instance == null)
            instance = new MainController();
        return instance;
    }

    public enum Mode{
        VIEW,
        EDIT
    }

    private Mode mode = Mode.EDIT;

    public Mode getMode(){
        return mode;
    }

    private void initialize(){
        UIController.getInstance().initialize();
        SceneController.getInstance().initialize();
        RenderingController.getInstance().initialize();
        InputController.getInstance().initialize();

    }

    private void run(){
        initialize();
        while(!glfwWindowShouldClose(UIController.getInstance().getMainWindow())) {
            InputController.getInstance().update();
//            float deltaTime = ImGui.getIO().getDeltaTime();

            RenderingController.getInstance().update();
            UIController.getInstance().update();

            glfwSwapBuffers(UIController.getInstance().getMainWindow());
            glfwPollEvents();
        }
        UIController.getInstance().destroy();
    }

    public static void main(String[] args) {
        MainController.getInstance().run();
    }

}