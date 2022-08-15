package EntityTree;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class EntityEmptyTest {

    @Test
    public void testGetName() {
        EntityEmpty entityEmpty = new EntityEmpty();
        String expected = "Entity";
        String result = entityEmpty.getName();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testSetName() {
        EntityEmpty entityEmpty = new EntityEmpty();
        String expected = "Some new name";
        entityEmpty.setName("Some new name");
        String result = entityEmpty.getName();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testDefaultParent() {
        EntityEmpty entityEmpty = new EntityEmpty();
        Entity result = entityEmpty.getParent();

        assertThat(result).isNull();
    }

    @Test
    public void testSetParentNoConflicts() {
        EntityEmpty parent = new EntityEmpty();
        EntityEmpty child = new EntityEmpty();

        child.setParent(parent);
        assertThat(parent.getParent()).isNull();
        assertThat(child.getParent()).isEqualTo(parent);
    }

    @Test
    public void testChildrenAsParent() {
        EntityEmpty parent = new EntityEmpty();
        EntityEmpty child = new EntityEmpty();
        EntityEmpty grandchild = new EntityEmpty();

        child.setParent(parent);
        grandchild.setParent(child);

        parent.setParent(grandchild);
        assertThat(parent.getParent()).isNull();
    }

    @Test
    public void testGetChildren() {
        EntityEmpty parent = new EntityEmpty();
        EntityEmpty child1 = new EntityEmpty();
        EntityEmpty child2 = new EntityEmpty();
        EntityEmpty child3 = new EntityEmpty();

        child1.setParent(parent);
        child2.setParent(parent);
        child3.setParent(parent);

        var result = parent.getChildren();
        assertThat(result).containsExactlyInAnyOrder(child1, child2, child3);
    }

    @Test
    public void testChildrenListIsModifiedAfterParentChange() {
        EntityEmpty oldParent = new EntityEmpty();
        EntityEmpty child = new EntityEmpty();

        child.setParent(oldParent);
        EntityEmpty newParent = new EntityEmpty();
        child.setParent(newParent);

        assertThat(oldParent.getChildren()).isEmpty();
        assertThat(newParent.getChildren()).containsExactly(child);
    }

    @Test
    public void testGetUnmodifiableChildren() {
        EntityEmpty parent = new EntityEmpty();
        EntityEmpty child1 = new EntityEmpty();
        EntityEmpty child2 = new EntityEmpty();
        EntityEmpty child3 = new EntityEmpty();

        child1.setParent(parent);
        child2.setParent(parent);
        child3.setParent(parent);

        var result = parent.getUnmodifiableChildren();
        assertThatException().isThrownBy(() -> result.add(new EntityEmpty()));
    }

    @Test
    public void testGetIndex() {
        ArrayList<EntityEmpty> entities = new ArrayList<>();
        for(int i = 0; i < 10; i++)
            entities.add(new EntityEmpty());

        List<Integer> result = entities.stream().map(Entity::getIndex).toList();

        assertThat(result).doesNotHaveDuplicates();
    }

    @Test
    public void testGetTransform() {
        EntityEmpty entity = new EntityEmpty();
        Transform expected = new Transform();

        Transform result = entity.getTransform();

        assertThat(result.getGlobalModelMatrix()).isEqualTo(expected.getGlobalModelMatrix());
        assertThat(result.getLocalRotation()).isEqualTo(expected.getLocalRotation());
        assertThat(result.getLocalTranslation()).isEqualTo(expected.getLocalTranslation());
        assertThat(result.getLocalScale()).isEqualTo(expected.getLocalScale());
    }

    @Test
    public void testSetTransform() {
        EntityEmpty entity = new EntityEmpty();
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
        EntityEmpty entity = new EntityEmpty();
        EntityEmpty child1 = new EntityEmpty();
        EntityEmpty child2 = new EntityEmpty();
        EntityEmpty child3 = new EntityEmpty();
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
        EntityEmpty counter = mock(EntityEmpty.class);

        EntityEmpty root = new EntityEmpty();
        EntityEmpty node1 = new EntityEmpty();
        EntityEmpty node2 = new EntityEmpty();
        EntityEmpty node3 = new EntityEmpty();
        EntityEmpty node4 = new EntityEmpty();

        node1.setParent(root);
        node2.setParent(node1);
        node3.setParent(node2);
        node4.setParent(node2);

        root.invokeFunctionOnSubtree(entity -> counter.setName("42"));

        verify(counter, times(5)).setName("42");
    }
}