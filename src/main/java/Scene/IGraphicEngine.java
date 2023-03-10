package Scene;

public abstract class IGraphicEngine{
    SceneState sceneState;
    protected IAppWindow appWindow;
    protected IRenderer renderer;
    protected IInput input;

    public IGraphicEngine(SceneState sceneState){
        this.sceneState = sceneState;
    }

    public void initialize(){
        appWindow.initialize();
        renderer.initialize();
        input.initialize();
    }

    public void destroy(){
        input.destroy();
        renderer.destroy();
        appWindow.destroy();
    }

    public IAppWindow getAppWindow(){
        return appWindow;
    }

    public IRenderer getRenderer(){
        return renderer;
    }

    public IInput getInput(){
        return input;
    }
}
