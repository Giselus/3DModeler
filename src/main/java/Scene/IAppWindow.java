package Scene;

public interface IAppWindow {
    boolean shouldBeClosed();

    void setWidth(int width);
    void setHeight(int height);

    void initialize();
    void destroy();
}
