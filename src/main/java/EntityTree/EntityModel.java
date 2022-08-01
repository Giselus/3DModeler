package EntityTree;

import Scene.IRenderer;
import UtilsModel.Face;
import UtilsModel.Mesh;
import UtilsModel.VertexPosition;

import java.util.ArrayList;

public class EntityModel extends Entity {

    final private Mesh mesh;

    public EntityModel(Mesh mesh, Entity parent) {
        super(parent);
        this.mesh = mesh;
    }

    @Override
    public void drawSelfAndChildren(IRenderer renderer) {
        renderer.render(this);
        super.drawSelfAndChildren(renderer);
    }

    public ArrayList<Face> getFaces() {
        return mesh.getFaces();
    }

    public ArrayList<VertexPosition> getVertices() {
        return mesh.getVertices();
    }

    public Mesh getMesh(){
        return mesh;
    }
}
