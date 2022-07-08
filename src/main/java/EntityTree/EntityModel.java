package EntityTree;

import Scene.IModelDrawer;
import Scene.ModelDrawer;
import UtilsModel.Face;
import UtilsModel.Model;

import java.util.ArrayList;

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

    public void setDrawer(ModelDrawer modelDrawer) {
        this.modelDrawer = modelDrawer;
    }

    public ArrayList<Face> getFaces() {
        return model.getFaces();
    }
}
