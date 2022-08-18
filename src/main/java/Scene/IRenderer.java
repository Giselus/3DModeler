package Scene;

import EntityTree.EntityModel;

public interface IRenderer {
    void initialize();
    void destroy();

    //Every frame has to be started with startFrame call, next all models should be rendered and then gui, which ends frame
    void startFrame();
    void render(EntityModel entity);
    void renderGUI();
}
