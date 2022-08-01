package Scene;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IInput {

    public void initialize();
    public void destroy();

    public float getMouseX();
    public float getMouseY();

    public void addMouseMoveCallback(BiConsumer<Float, Float> f);
    public void removeMouseMoveCallback(BiConsumer<Float, Float> f);

    public void addMouseScrollCallback(Consumer<Float> f);
    public void removeMouseScrollCallback(Consumer<Float> f);

    public void addKeyCallback(char key, Runnable f);
    public void removeKeyCallback(char key, Runnable f);

}
