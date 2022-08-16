package EntityTree;

import Scene.IInput;
import Scene.SceneState;
import UtilsModel.Face;
import UtilsModel.Mesh;
import UtilsModel.VertexPosition;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
        Mesh mesh = new Mesh(meshVertices, null);
        EntityModel mainEntity = new EntityModel(mesh, null);

        when(sceneState.getMainSelectedEntity()).thenReturn(mainEntity);

        entityOperations.pickAllEntityVertices();

        assertThat(pickedVertices).containsExactlyInAnyOrderElementsOf(meshVertices);
        assertThat(pickedVertices).extracting(VertexPosition::isPicked).doesNotContain(false);
    }

    @Test
    public void testDeleteEntities() {
        IInput input = mock(IInput.class);
        SceneState sceneState = mock(SceneState.class);

        EntityOperations entityOperations = new EntityOperations(input, new ArrayList<>(), sceneState);

        Entity root = new EntityEmpty();
        Entity node1 = new EntityEmpty();
        node1.setParent(root);
        Entity node2 = new EntityEmpty();
        node2.setParent(root);
        Entity node3 = new EntityEmpty();
        node3.setParent(node2);
        Entity node4 = new EntityEmpty();
        node4.setParent(node3);

        Set<Entity> set = new HashSet<>();
        set.add(root);
        set.add(node2);
        set.add(node3);

        when(sceneState.getSelectedEntities()).thenReturn(set);

        entityOperations.deleteEntities();

        assertThat(root).isNotNull();
        assertThat(node1.getParent()).isEqualTo(root);
        assertThat(node2.getParent()).isNull();
        assertThat(node3.getParent()).isNull();
        assertThat(node4.getParent()).isEqualTo(root);

        verify(sceneState).clearSelectedEntities();
        verify(sceneState).setMainSelectedEntity(null);
        verify(sceneState).getSelectedEntities();
        verifyNoMoreInteractions(sceneState);
    }

    @Test
    public void testMergeTwoEntitiesTooMuchEntities() {
        IInput input = mock(IInput.class);
        SceneState sceneState = new SceneState();

        EntityOperations entityOperations = new EntityOperations(input, new ArrayList<>(), sceneState);

        sceneState.addSelectedEntity(new EntityEmpty());
        sceneState.addSelectedEntity(new EntityEmpty());
        sceneState.addSelectedEntity(new EntityEmpty());

        entityOperations.mergeTwoEntities();

        assertThat(sceneState.getSelectedEntities().size()).isEqualTo(3);
    }

    @Test
    public void testMergeTwoEntitiesReversed() {
        IInput input = mock(IInput.class);
        SceneState sceneState = new SceneState();

        EntityOperations entityOperations = new EntityOperations(input, new ArrayList<>(), sceneState);

        ArrayList<VertexPosition> verticesNode1 = new ArrayList<>();
        ArrayList<Face> facesNode1 = new ArrayList<>();
        Mesh meshNode1 = new Mesh(verticesNode1, facesNode1);
        ArrayList<VertexPosition> verticesNode3 = new ArrayList<>();
        verticesNode3.add(new VertexPosition(new Vector3f()));
        verticesNode3.add(new VertexPosition(new Vector3f()));
        verticesNode3.add(new VertexPosition(new Vector3f()));
        ArrayList<Face> facesNode3 = new ArrayList<>();
        facesNode3.add(new Face(verticesNode3));
        Mesh meshNode3 = new Mesh(verticesNode3, facesNode3);

        Entity root = new EntityEmpty();
        Entity node1 = new EntityModel(meshNode1, root);
        node1.setParent(root);
        Entity node2 = new EntityEmpty();
        node2.setParent(node1);
        Entity node3 = new EntityModel(meshNode3, node2);

        sceneState.addSelectedEntity(node1);
        sceneState.addSelectedEntity(node3);
        sceneState.setMainSelectedEntity(node3);

        entityOperations.mergeTwoEntities();

        Entity resultEntity = sceneState.getMainSelectedEntity();

        assertThat(sceneState.getSelectedEntities().size()).isEqualTo(1);
        assertThat(resultEntity.getParent()).isEqualTo(root);
        assertThat(node2.getParent()).isEqualTo(resultEntity);
        assertThat(resultEntity.getChildren()).containsExactly(node2);
        assertThat(resultEntity).isInstanceOf(EntityModel.class);
        assertThat(((EntityModel)resultEntity).getMesh().getVertices())
                .containsExactlyInAnyOrderElementsOf(verticesNode3);
        assertThat(((EntityModel)resultEntity).getMesh().getFaces())
                .containsExactlyInAnyOrderElementsOf(facesNode3);
        assertThat(root.getChildren()).containsExactly(resultEntity);
    }
}