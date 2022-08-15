package EntityTree;

import Scene.IInput;
import Scene.SceneState;
import UtilsModel.Face;
import UtilsModel.Mesh;
import UtilsModel.VertexPosition;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class EntityOperationsTest {

    @Test
    public void testPickAllEntityVertices() {
        IInput input = mock(IInput.class);
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();
        VertexPosition v1 = new VertexPosition(new Vector3f(1f, 6f, 4f));
        VertexPosition v2 = new VertexPosition(new Vector3f(5f, 1f, 2f));
        pickedVertices.add(v1);
        pickedVertices.add(v2);

        SceneState sceneState = mock(SceneState.class);

        EntityOperations entityOperations = new EntityOperations(input, pickedVertices, sceneState);

        ArrayList<VertexPosition> meshVertices = new ArrayList<>();
        VertexPosition vm1 = new VertexPosition(new Vector3f(2f, 7f, 1f));
        VertexPosition vm2 = new VertexPosition(new Vector3f(8f, 6f, 3f));
        VertexPosition vm3 = new VertexPosition(new Vector3f(1f, 6f, 4f));
        meshVertices.add(vm1);
        meshVertices.add(vm2);
        meshVertices.add(vm3);
        ArrayList<Face> meshFaces = new ArrayList<>();
        meshFaces.add(new Face(meshVertices));
        Mesh mesh = new Mesh(meshVertices, meshFaces);
        EntityModel mainEntity = new EntityModel(mesh, null);


        when(sceneState.getMainSelectedEntity()).thenReturn(mainEntity);

        entityOperations.pickAllEntityVertices();

        assertThat(pickedVertices).containsExactlyInAnyOrderElementsOf(meshVertices);
        assertThat(pickedVertices).extracting(VertexPosition::isPicked).doesNotContain(false);
    }
}