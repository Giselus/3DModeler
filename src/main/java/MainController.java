import com.sun.tools.javac.Main;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiDockNodeFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class MainController{

    private static MainController Instance;

    public static MainController getInstance(){
        if(Instance == null)
            Instance = new MainController();
        return Instance;
    }

    private void initialize(){
        UIController.getInstance().initialize();

    }

    private void run(){
        initialize();

        while(!glfwWindowShouldClose(UIController.getInstance().getMainWindow())) {
            float deltaTime = ImGui.getIO().getDeltaTime();

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            UIController.getInstance().update();
            //TODO: move rendering, input and logic updates

            glfwSwapBuffers(UIController.getInstance().getMainWindow());
            glfwPollEvents();
        }
        UIController.getInstance().destroy();
    }

    public static void main(String[] args) {
        MainController.getInstance().run();
    }
}