package Controllers;

import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.*;

public class MainController{

    private static MainController Instance;

    public static MainController getInstance(){
        if(Instance == null)
            Instance = new MainController();
        return Instance;
    }

    public enum Mode{
        View,
        Edit
    }

    private Mode mode = Mode.Edit;

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
            float deltaTime = ImGui.getIO().getDeltaTime();

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