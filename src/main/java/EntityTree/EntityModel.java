package EntityTree;

import Controllers.RenderingController;
import UtilsCommon.Shader;
import UtilsModel.Model;
import org.joml.Matrix4fc;

public class EntityModel extends Entity {

    final private UtilsModel.Model Model;

    public EntityModel(Model model, Entity parent){
        super(parent);
        this.Model = model;
    }

    public void update(){
        Shader shader = RenderingController.getInstance().getActiveShader();
        Matrix4fc modelMatrix = transform.getGlobalModelMatrix();
        shader.setMatrix4("model",modelMatrix);
        Model.Draw();
        super.update();
    }

}
