package Scene;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IInput {
    enum KeyCode{
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
        KEY_LEFT_SHIFT
    }
    enum MouseKeyCode{
        MOUSE_LEFT,
        MOUSE_RIGHT,
        MOUSE_SCROLL
    }

    void initialize();
    void destroy();
    void runContinuousCallbacks();
    void processInput();

    float getMouseX();
    float getMouseY();

    boolean isKeyPressed(KeyCode key);
    boolean isMouseKeyPressed(MouseKeyCode key);

    //Arguments are equal to offset of mouse position
    void addMouseMoveCallback(BiConsumer<Float, Float> f);
    //Arguments are equal to present mouse position
    void removeMouseMoveCallback(BiConsumer<Float, Float> f);

    void addMouseScrollCallback(Consumer<Float> f);
    void removeMouseScrollCallback(Consumer<Float> f);

    void addKeyCallback(KeyCode key, Runnable f);
    void removeKeyCallback(KeyCode key, Runnable f);

    void addMouseKeyCallback(MouseKeyCode key, Runnable f);
    void removeMouseKeyCallback(MouseKeyCode key, Runnable f);
}
