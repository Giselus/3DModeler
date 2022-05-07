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
        camera = new Camera();

        glfwSetCursorPosCallback(UIController.getInstance().getMainWindow(), this::mouse_callback);
        glfwSetScrollCallback(UIController.getInstance().getMainWindow(), this::scroll_callback);
        glfwSetInputMode(UIController.getInstance().getMainWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    private void run(){
        initialize();

        OBJLoader loader = new OBJLoader();
        var tmp = loader.load("src/main/data/cube.obj");

        Shader shader = new Shader("src/main/shaders/vertexShader.vs","src/main/shaders/fragmentShader.fs");

        while(!glfwWindowShouldClose(UIController.getInstance().getMainWindow())) {
            process_input(UIController.getInstance().getMainWindow());
            float deltaTime = ImGui.getIO().getDeltaTime();

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Matrix4f projection = new Matrix4f().setPerspective((float)Math.toRadians(camera.getZoom()),
                    1200.0f/800.0f, 0.1f, 100.0f);
            Matrix4f view = camera.getViewMatrix();
            Matrix4f model = new Matrix4f().identity();
            model.translate(new Vector3f(0,0,-5));
            shader.setMatrix4("projection",projection);
            shader.setMatrix4("view",view);
            shader.setMatrix4("model",model);
            shader.setVector3f("viewPos", camera.getPosition());
            shader.setVector3f("pointLights[0].position", new Vector3f(1.0f,0.0f,1.0f));
            shader.setVector3f("pointLights[0].ambient", new Vector3f(0.05f));
            shader.setVector3f("pointLights[0].diffuse", new Vector3f(0.8f));
            shader.setVector3f("pointLights[0].specular", new Vector3f(1f));
            shader.setFloat("pointLights[0].constant", 1.0f);
            shader.setFloat("pointLights[0].linear", 0.09f);
            shader.setFloat("pointLights[0].quadratic", 0.032f);
            shader.setFloat("material.shininess",32.0f);
            shader.setFloat("material.diffuse",0.1f);
            shader.setFloat("material.specular",0.5f);
            for(Model m: tmp)
                m.Draw(shader);

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

    private boolean firstMouse = true;
    float lastX;
    float lastY;

    private Camera camera;

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

        camera.ProcessMousePosition(offsetX,offsetY);
    }

    private void scroll_callback(long window, double offsetXDouble, double offsetYDouble){
        camera.ProcessMouseScroll((float)offsetYDouble);
    }

    private void process_input(long window){

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