package Scene;

import EntityTree.EntityModel;
import UtilsModel.Mesh;

public interface IRenderer {
    public void initialize();
    public void destroy();

    //Every frame has to be started with startFrame call, next all models should be rendered and then gui, which ends frame
    public void startFrame();
    public void render(EntityModel entity);
    public void renderGUI();
}
