import ModelLoader.OBJLoader;
import UtilsCommon.Mesh;
import UtilsCommon.Model;
import UtilsCommon.Shader;
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
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

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
        RenderingController.getInstance().initialize();

        glfwSetCursorPosCallback(UIController.getInstance().getMainWindow(), this::mouse_callback);
        glfwSetScrollCallback(UIController.getInstance().getMainWindow(), this::scroll_callback);
        //glfwSetInputMode(UIController.getInstance().getMainWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    private void run(){
        initialize();
        while(!glfwWindowShouldClose(UIController.getInstance().getMainWindow())) {
            process_input(UIController.getInstance().getMainWindow());
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
        if(glfwGetMouseButton(window,GLFW_MOUSE_BUTTON_2) == GLFW_PRESS)
            RenderingController.getInstance().getCamera().ProcessMousePosition(offsetX,offsetY);
    }

    private void scroll_callback(long window, double offsetXDouble, double offsetYDouble){
        RenderingController.getInstance().getCamera().ProcessMouseScroll((float)offsetYDouble);
    }

    private void process_input(long window){
        Camera camera = RenderingController.getInstance().getCamera();
        float deltaTime = ImGui.getIO().getDeltaTime();
        if(glfwGetKey(window,GLFW_KEY_W) == GLFW_PRESS)
            camera.ProcessMovement(Camera.Camera_Movement.FORWARD,deltaTime);
        if(glfwGetKey(window,GLFW_KEY_S) == GLFW_PRESS)
            camera.ProcessMovement(Camera.Camera_Movement.BACKWARD,deltaTime);
        if(glfwGetKey(window,GLFW_KEY_A) == GLFW_PRESS)
            camera.ProcessMovement(Camera.Camera_Movement.LEFT,deltaTime);
        if(glfwGetKey(window,GLFW_KEY_D) == GLFW_PRESS)
            camera.ProcessMovement(Camera.Camera_Movement.RIGHT,deltaTime);
    }
}