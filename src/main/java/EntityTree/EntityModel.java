package EntityTree;

import UtilsCommon.Shader;
import UtilsModel.Model;
import org.joml.Matrix4fc;

public class EntityModel extends Entity {

    final private UtilsModel.Model model;

    public EntityModel(Model model, Entity parent){
        super(parent);
        this.model = model;
    }

    public void update() {
        //TODO wiesz co robic
        Shader shader = model.renderingUpdater.getActiveShader();
        Matrix4fc modelMatrix = transform.getGlobalModelMatrix();
        shader.setMatrix4("model",modelMatrix);
        model.Draw();
        super.update();
    }
}
