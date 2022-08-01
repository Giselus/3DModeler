package Scene;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IInput {

    public static enum KeyCode{
        KEY_A,
        KEY_B,
        KEY_C,
        KEY_D,
        KEY_E,
        KEY_F,
        KEY_G,
        KEY_H,
        KEY_I,
        KEY_J,
        KEY_K,
        KEY_L,
        KEY_M,
        KEY_N,
        KEY_O,
        KEY_P,
        KEY_Q,
        KEY_R,
        KEY_S,
        KEY_T,
        KEY_U,
        KEY_V,
        KEY_W,
        KEY_X,
        KEY_Y,
        KEY_Z,
        KEY_LEFT_CTRL,
    }
    public static enum MouseKeyCode{
        MOUSE_LEFT,
        MOUSE_RIGHT,
        MOUSE_SCROLL
    }

    public void initialize();
    public void destroy();
    public void runContinuousCallbacks();
    public void processInput();

    public float getMouseX();
    public float getMouseY();

    public boolean isKeyPressed(KeyCode key);
    public boolean isMouseKeyPressed(MouseKeyCode key);

    //Arguments are equal to offset of mouse position
    public void addMouseMoveCallback(BiConsumer<Float, Float> f);
    //Arguments are equal to present mouse position
    public void removeMouseMoveCallback(BiConsumer<Float, Float> f);

    public void addMouseScrollCallback(Consumer<Float> f);
    public void removeMouseScrollCallback(Consumer<Float> f);

    public void addKeyCallback(KeyCode key, Runnable f);
    public void removeKeyCallback(KeyCode key, Runnable f);

    public void addMouseKeyCallback(MouseKeyCode key, Runnable f);
    public void removeMouseKeyCallback(MouseKeyCode key, Runnable f);
}
