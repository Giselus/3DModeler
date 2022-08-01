package Scene;

public interface IAppWindow {

    public boolean shouldBeClosed();

    public void setWidth(int width);
    public void setHeight(int height);

    public void initialize();
    public void destroy();
}
