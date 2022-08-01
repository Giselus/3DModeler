package Scene;

import org.lwjgl.glfw.Callbacks;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.*;

public class GLFWInput implements IInput{

    private HashSet<BiConsumer<Float,Float>> mousePositionCallbackSet = new HashSet<>();
    private HashSet<Consumer<Float>> mouseScrollCallbackSet = new HashSet<>();
    private TreeMap<Character, HashSet<Runnable>> keyCallbackMap = new TreeMap<>();


    private void mousePositionCallback(long window, double posX, double posY){
        mouseX = (float)posX;
        mouseY = (float)posY;
        for(BiConsumer<Float, Float> f: mousePositionCallbackSet){
            f.accept((float)posX,(float)posY);
        }
    }

    private void mouseScrollCallback(long window, double offsetXDouble, double offsetYDouble){
        for(Consumer<Float> f: mouseScrollCallbackSet){
            f.accept((float)offsetYDouble);
        }
    }

    private void keyCallback(long window, int key, int scancode, int action, int mods){
        if(action != GLFW_PRESS)
            return;
//        for(Runnable f: keyCallbackMap.get(key)){ //todo fix
//            f.run();
//        }
    }

    private float mouseX;
    private float mouseY;

    private GLFWAppWindow appWindow;
    public GLFWInput(GLFWAppWindow appWindow){
        this.appWindow = appWindow;
    }

    @Override
    public void initialize() {
        for(char c = 'A'; c <= 'Z'; c++){
            keyCallbackMap.put(c,new HashSet<>());
        }
        glfwSetCursorPosCallback(appWindow.getWindowID(),this::mousePositionCallback);
        glfwSetScrollCallback(appWindow.getWindowID(),this::mouseScrollCallback);
        glfwSetKeyCallback(appWindow.getWindowID(),this::keyCallback);
    }

    @Override
    public void destroy() {
        Callbacks.glfwFreeCallbacks(appWindow.getWindowID());
    }

    @Override
    public float getMouseX() {
        return mouseX;
    }

    @Override
    public float getMouseY() {
        return mouseY;
    }

    @Override
    public void addMouseMoveCallback(BiConsumer<Float, Float> f) {
        mousePositionCallbackSet.add(f);
    }

    @Override
    public void removeMouseMoveCallback(BiConsumer<Float, Float> f) {
        mousePositionCallbackSet.remove(f);
    }

    @Override
    public void addMouseScrollCallback(Consumer<Float> f) {
        mouseScrollCallbackSet.add(f);
    }

    @Override
    public void removeMouseScrollCallback(Consumer<Float> f) {
        mouseScrollCallbackSet.remove(f);
    }

    @Override
    public void addKeyCallback(char key, Runnable f) {
        keyCallbackMap.get(key).add(f);
    }

    @Override
    public void removeKeyCallback(char key, Runnable f) {
        keyCallbackMap.get(key).remove(f);
    }
}
