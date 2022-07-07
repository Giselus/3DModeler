package EntityTree;

import Scene.IModelDrawer;
import Scene.ModelDrawer;
import Scene.RenderingUpdater;
import UtilsCommon.Shader;
import UtilsModel.Model;
import org.joml.Matrix4fc;

public class EntityModel extends Entity {

    final private UtilsModel.Model model;
    private IModelDrawer modelDrawer;

    public EntityModel(Model model, Entity parent) {
        super(parent);
        this.model = model;
    }

    @Override
    public void drawSelfAndChildren() {
        modelDrawer.draw(this);
        super.drawSelfAndChildren();
    }

    @Override
    public void setDrawer(RenderingUpdater renderingUpdater) {
        this.modelDrawer = new ModelDrawer(model.getFaces(), renderingUpdater);
    }
}
