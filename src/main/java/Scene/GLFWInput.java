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

    //TODO: Add continuous callback functions

    private HashSet<BiConsumer<Float,Float>> mousePositionCallbackSet = new HashSet<>();
    private HashSet<BiConsumer<Float,Float>> mousePositionContinuousCallbackSet = new HashSet<>();

    private HashSet<Consumer<Float>> mouseScrollCallbackSet = new HashSet<>();
    private HashSet<Consumer<Float>> mouseScrollContinuousCallbackSet = new HashSet<>();

    private TreeMap<KeyCode, HashSet<Runnable>> keyCallbackMap = new TreeMap<>();
    private TreeMap<KeyCode, HashSet<Runnable>> keyContinuousCallbackMap = new TreeMap<>();

    private TreeMap<MouseKeyCode, HashSet<Runnable>> mouseKeyCallbackMap = new TreeMap<>();
    private TreeMap<MouseKeyCode, HashSet<Runnable>> mouseKeyContinuousCallbackMap = new TreeMap<>();

    private TreeMap<Integer, KeyCode> keyCodeTransformMap = new TreeMap<>();
    {
        keyCodeTransformMap.put(GLFW_KEY_A,KeyCode.KEY_A);
        keyCodeTransformMap.put(GLFW_KEY_B,KeyCode.KEY_B);
        keyCodeTransformMap.put(GLFW_KEY_C,KeyCode.KEY_C);
        keyCodeTransformMap.put(GLFW_KEY_D,KeyCode.KEY_D);
        keyCodeTransformMap.put(GLFW_KEY_E,KeyCode.KEY_E);
        keyCodeTransformMap.put(GLFW_KEY_F,KeyCode.KEY_F);
        keyCodeTransformMap.put(GLFW_KEY_G,KeyCode.KEY_G);
        keyCodeTransformMap.put(GLFW_KEY_H,KeyCode.KEY_H);
        keyCodeTransformMap.put(GLFW_KEY_I,KeyCode.KEY_I);
        keyCodeTransformMap.put(GLFW_KEY_J,KeyCode.KEY_J);
        keyCodeTransformMap.put(GLFW_KEY_K,KeyCode.KEY_K);
        keyCodeTransformMap.put(GLFW_KEY_L,KeyCode.KEY_L);
        keyCodeTransformMap.put(GLFW_KEY_M,KeyCode.KEY_M);
        keyCodeTransformMap.put(GLFW_KEY_N,KeyCode.KEY_N);
        keyCodeTransformMap.put(GLFW_KEY_O,KeyCode.KEY_O);
        keyCodeTransformMap.put(GLFW_KEY_P,KeyCode.KEY_P);
        keyCodeTransformMap.put(GLFW_KEY_Q,KeyCode.KEY_Q);
        keyCodeTransformMap.put(GLFW_KEY_R,KeyCode.KEY_R);
        keyCodeTransformMap.put(GLFW_KEY_S,KeyCode.KEY_S);
        keyCodeTransformMap.put(GLFW_KEY_T,KeyCode.KEY_T);
        keyCodeTransformMap.put(GLFW_KEY_U,KeyCode.KEY_U);
        keyCodeTransformMap.put(GLFW_KEY_V,KeyCode.KEY_V);
        keyCodeTransformMap.put(GLFW_KEY_W,KeyCode.KEY_W);
        keyCodeTransformMap.put(GLFW_KEY_X,KeyCode.KEY_X);
        keyCodeTransformMap.put(GLFW_KEY_Y,KeyCode.KEY_Y);
        keyCodeTransformMap.put(GLFW_KEY_Z,KeyCode.KEY_Z);
        keyCodeTransformMap.put(GLFW_KEY_LEFT_CONTROL,KeyCode.KEY_LEFT_CTRL);
    }

    private TreeMap<Integer, MouseKeyCode> mouseKeyCodeTransformMap = new TreeMap<>();
    {
        mouseKeyCodeTransformMap.put(GLFW_MOUSE_BUTTON_1,MouseKeyCode.MOUSE_LEFT);
        mouseKeyCodeTransformMap.put(GLFW_MOUSE_BUTTON_2,MouseKeyCode.MOUSE_RIGHT);
        mouseKeyCodeTransformMap.put(GLFW_MOUSE_BUTTON_3,MouseKeyCode.MOUSE_SCROLL);
    }

    private HashSet<KeyCode> pressedKeys = new HashSet<>();
    private HashSet<MouseKeyCode> pressedMouseKeys = new HashSet<>();

    private void mousePositionCallback(long window, double posX, double posY){
        posY = (float)sceneState.getSceneWindowHeight() - posY;
        if(firstTick){
            firstTick = false;
            lastX = (float)posX;
            lastY = (float)posY;
        }
        mouseX = (float)posX;
        mouseY = (float)posY;
        float offsetX = mouseX - lastX;
        float offsetY = mouseY - lastY;



        for(BiConsumer<Float, Float> f: mousePositionCallbackSet){
            f.accept(offsetX,offsetY);
        }

        lastX = mouseX;
        lastY = mouseY;
    }

    private void mouseScrollCallback(long window, double offsetXDouble, double offsetYDouble){
        deltaScroll = (float)offsetYDouble;
        for(Consumer<Float> f: mouseScrollCallbackSet){
            f.accept((float)offsetYDouble);
        }
    }

    private void keyCallback(long window, int key, int scancode, int action, int mods){
        KeyCode code = keyCodeTransformMap.get(key);
        if(code == null)
            return;
        if(action == GLFW_RELEASE){
            pressedKeys.remove(code);
            return;
        }
        if(action != GLFW_PRESS)
            return;
        pressedKeys.add(code);
        for(Runnable f: keyCallbackMap.get(code)){
            f.run();
        }
    }

    private void mouseKeyCallback(long window, int key, int action, int mods){
        MouseKeyCode code = mouseKeyCodeTransformMap.get(key);
        if(code == null)
            return;
        if(action == GLFW_RELEASE){
            pressedMouseKeys.remove(code);
            return;
        }
        if(action != GLFW_PRESS)
            return;
        pressedMouseKeys.add(code);
        for(Runnable f: mouseKeyCallbackMap.get(code)){
            f.run();
        }
    }

    private void runMousePositionContinuousCallbacks(){
        for(BiConsumer<Float, Float> f: mousePositionContinuousCallbackSet){
            f.accept(mouseX,mouseY);
        }
    }

    private void runScrollContinuousCallbacks(){
        for(Consumer<Float> f: mouseScrollContinuousCallbackSet){
            f.accept(deltaScroll);
        }
    }

    private void runKeyContinuousCallbacks(){
        for(KeyCode code: pressedKeys){
            for(Runnable f: keyContinuousCallbackMap.get(code)){
                f.run();
            }
        }
    }

    private void runMouseKeyContinuousCallbacks(){
        for(MouseKeyCode code: pressedMouseKeys){
            for(Runnable f: mouseKeyContinuousCallbackMap.get(code)){
                f.run();
            }
        }
    }

    private float mouseX;
    private float mouseY;
    private float lastX;
    private float lastY;
    private boolean firstTick = true;
    private float deltaScroll = 0f;

    private GLFWAppWindow appWindow;
    private SceneState sceneState;
    public GLFWInput(GLFWAppWindow appWindow, SceneState sceneState){
        this.appWindow = appWindow;
        this.sceneState = sceneState;
    }

    @Override
    public void initialize() {
        for(KeyCode code: KeyCode.values()){
            keyCallbackMap.put(code, new HashSet<>());
            keyContinuousCallbackMap.put(code, new HashSet<>());
        }
        for(MouseKeyCode code: MouseKeyCode.values()){
            mouseKeyCallbackMap.put(code, new HashSet<>());
            mouseKeyContinuousCallbackMap.put(code, new HashSet<>());
        }
        glfwSetCursorPosCallback(appWindow.getWindowID(),this::mousePositionCallback);
        glfwSetScrollCallback(appWindow.getWindowID(),this::mouseScrollCallback);
        glfwSetKeyCallback(appWindow.getWindowID(),this::keyCallback);
        glfwSetMouseButtonCallback(appWindow.getWindowID(),this::mouseKeyCallback);
    }

    @Override
    public void runContinuousCallbacks(){
        runMousePositionContinuousCallbacks();
        runScrollContinuousCallbacks();
        runKeyContinuousCallbacks();
        runMouseKeyContinuousCallbacks();
        deltaScroll = 0f;
    }

    @Override
    public void processInput(){
        glfwPollEvents();
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
    public boolean isKeyPressed(KeyCode key){
        return pressedKeys.contains(key);
    }

    @Override
    public boolean isMouseKeyPressed(MouseKeyCode key){
        return pressedMouseKeys.contains(key);
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
    public void addKeyCallback(KeyCode key, Runnable f) {
        keyCallbackMap.get(key).add(f);
    }

    @Override
    public void removeKeyCallback(KeyCode key, Runnable f) {
        keyCallbackMap.get(key).remove(f);
    }

    @Override
    public void addMouseKeyCallback(MouseKeyCode key, Runnable f) {
        mouseKeyCallbackMap.get(key).add(f);
    }

    @Override
    public void removeMouseKeyCallback(MouseKeyCode key, Runnable f) {
        mouseKeyCallbackMap.get(key).remove(f);
    }


}
