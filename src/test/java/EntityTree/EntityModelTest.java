package EntityTree;

import UtilsModel.Face;
import UtilsModel.Mesh;
import UtilsModel.VertexPosition;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class EntityModelTest {

    @Test
    public void testGetName() {
        Mesh mesh = mock(Mesh.class);
        EntityModel entityModel = new EntityModel(mesh, null);
        String expected = "Entity";
        String result = entityModel.getName();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testSetName() {
        Mesh mesh = mock(Mesh.class);
        EntityModel entityModel = new EntityModel(mesh, null);
        String expected = "Some new name";
        entityModel.setName("Some new name");
        String result = entityModel.getName();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testSetParentNoConflicts() {
        Mesh mesh = mock(Mesh.class);
        EntityModel parent = new EntityModel(mesh, null);
        EntityModel child = new EntityModel(mesh, parent);

        child.setParent(parent);

        assertThat(parent.getParent()).isNull();
        assertThat(child.getParent()).isEqualTo(parent);
    }

    @Test
    public void testChildrenAsParent() {
        Mesh mesh = mock(Mesh.class);
        EntityModel parent = new EntityModel(mesh, null);
        EntityModel child = new EntityModel(mesh, null);
        EntityModel grandchild = new EntityModel(mesh, null);

        child.setParent(parent);
        grandchild.setParent(child);

        parent.setParent(grandchild);
        assertThat(parent.getParent()).isNull();
    }

    @Test
    public void testGetChildren() {
        Mesh mesh = mock(Mesh.class);
        EntityModel parent = new EntityModel(mesh, null);
        EntityModel child1 = new EntityModel(mesh, parent);
        EntityModel child2 = new EntityModel(mesh, parent);
        EntityModel child3 = new EntityModel(mesh, parent);

        var result = parent.getChildren();
        assertThat(result).containsExactlyInAnyOrder(child1, child2, child3);
    }

    @Test
    public void testChildrenListIsModifiedAfterParentChange() {
        Mesh mesh = mock(Mesh.class);
        EntityModel oldParent = new EntityModel(mesh, null);
        EntityModel child = new EntityModel(mesh, null);

        child.setParent(oldParent);
        EntityModel newParent = new EntityModel(mesh, null);
        child.setParent(newParent);

        assertThat(oldParent.getChildren()).isEmpty();
        assertThat(newParent.getChildren()).containsExactly(child);
    }

    @Test
    public void testGetUnmodifiableChildren() {
        Mesh mesh = mock(Mesh.class);
        EntityModel parent = new EntityModel(mesh, null);
        EntityModel child1 = new EntityModel(mesh, parent);
        EntityModel child2 = new EntityModel(mesh, parent);
        EntityModel child3 = new EntityModel(mesh, parent);

        var result = parent.getUnmodifiableChildren();
        assertThatException().isThrownBy(() -> result.add(new EntityEmpty()));
    }

    @Test
    public void testGetIndex() {
        Mesh mesh = mock(Mesh.class);
        ArrayList<EntityModel> entities = new ArrayList<>();
        for(int i = 0; i < 10; i++)
            entities.add(new EntityModel(mesh, null));

        List<Integer> result = entities.stream().map(Entity::getIndex).toList();

        assertThat(result).doesNotHaveDuplicates();
    }

    @Test
    public void testGetTransform() {
        Mesh mesh = mock(Mesh.class);
        EntityModel entity = new EntityModel(mesh, null);
        Transform expected = new Transform();

        Transform result = entity.getTransform();

        assertThat(result.getGlobalModelMatrix()).isEqualTo(expected.getGlobalModelMatrix());
        assertThat(result.getLocalRotation()).isEqualTo(expected.getLocalRotation());
        assertThat(result.getLocalTranslation()).isEqualTo(expected.getLocalTranslation());
        assertThat(result.getLocalScale()).isEqualTo(expected.getLocalScale());
    }

    @Test
    public void testSetTransform() {
        Mesh mesh = mock(Mesh.class);
        EntityModel entity = new EntityModel(mesh, null);
        Transform expected = new Transform();
        expected.setLocalRotation(new Vector3f(1f, 2f, 3f));
        expected.setLocalTranslation(new Vector3f(4f, 5f, 6f));
        expected.setLocalScale(new Vector3f(7f, 8f, 9f));

        entity.setTransform(expected);

        Transform result = entity.getTransform();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testUpdateSelfAndChildren() {
        Mesh mesh = mock(Mesh.class);
        EntityModel entity = new EntityModel(mesh, null);
        EntityModel child1 = new EntityModel(mesh, null);
        EntityModel child2 = new EntityModel(mesh, null);
        EntityModel child3 = new EntityModel(mesh, null);
        child1.setParent(entity);
        child2.setParent(entity);
        child3.setParent(child2);
        Transform entityTransform = mock(Transform.class);
        Transform child1Transform = mock(Transform.class);
        Transform child2Transform = mock(Transform.class);
        Transform child3Transform = mock(Transform.class);
        entity.setTransform(entityTransform);
        child1.setTransform(child1Transform);
        child2.setTransform(child2Transform);
        child3.setTransform(child3Transform);

        entity.updateSelfAndChildren();

        verify(entityTransform).updateGlobalModelMatrix();
        verify(child1Transform).updateGlobalModelMatrix(any());
        verify(child2Transform).updateGlobalModelMatrix(any());
        verify(child3Transform).updateGlobalModelMatrix(any());
    }

    @Test
    public void testInvokeFunctionOnSubtree() {
        Mesh mesh = mock(Mesh.class);
        EntityEmpty counter = mock(EntityEmpty.class);

        EntityModel root = new EntityModel(mesh, null);
        EntityModel node1 = new EntityModel(mesh, root);
        EntityModel node2 = new EntityModel(mesh, node1);
        EntityModel node3 = new EntityModel(mesh, node2);
        EntityModel node4 = new EntityModel(mesh, node2);

        root.invokeFunctionOnSubtree(entity -> counter.setName("42"));

        verify(counter, times(5)).setName("42");
    }

    @Test
    public void testGetMesh() {
        Mesh mesh = mock(Mesh.class);
        EntityModel entity = new EntityModel(mesh, null);

        Mesh result = entity.getMesh();

        assertThat(result).isEqualTo(mesh);
    }
}