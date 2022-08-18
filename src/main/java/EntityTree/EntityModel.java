package EntityTree;

import UtilsModel.Mesh;

public class EntityModel extends Entity {

    final private Mesh mesh;

    public EntityModel(Mesh mesh, Entity parent) {
        super(parent);
        this.mesh = mesh;
    }

    public Mesh getMesh(){
        return mesh;
    }
}
