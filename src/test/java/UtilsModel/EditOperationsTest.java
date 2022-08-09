package UtilsModel;

import EntityTree.EntityEmpty;
import EntityTree.EntityModel;
import Scene.IInput;
import Scene.SceneState;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditOperationsTest {

    @Test
    public void testCreateFaceGoodEntity() {
        IInput input = mock(IInput.class);
        SceneState sceneState = mock(SceneState.class);
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();

        EditOperations editOperations = new EditOperations(input, pickedVertices, sceneState);

        EntityModel entityModel = mock(EntityModel.class);
        Mesh mesh = mock(Mesh.class);

        when(sceneState.getSelectedEntity()).thenReturn(entityModel);
        when(entityModel.getMesh()).thenReturn(mesh);

        editOperations.createFace();

        verify(sceneState).getSelectedEntity();
        verify(entityModel).getMesh();
        verify(mesh).addFace(pickedVertices);
        verifyNoMoreInteractions(sceneState, mesh, input);
    }

    @Test
    public void testCreateFaceBadEntity() {
        IInput input = mock(IInput.class);
        SceneState sceneState = mock(SceneState.class);
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();

        EditOperations editOperations = new EditOperations(input, pickedVertices, sceneState);

        EntityEmpty entityEmpty = mock(EntityEmpty.class);
        when(sceneState.getSelectedEntity()).thenReturn(entityEmpty);

        editOperations.createFace();

        verify(sceneState).getSelectedEntity();
        verifyNoMoreInteractions(sceneState, entityEmpty, input);
    }

    @Test
    public void testCopyVerticesBadEntity() {
        IInput input = mock(IInput.class);
        SceneState sceneState = mock(SceneState.class);
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();

        EditOperations editOperations = new EditOperations(input, pickedVertices, sceneState);

        EntityEmpty entityEmpty = mock(EntityEmpty.class);
        when(sceneState.getSelectedEntity()).thenReturn(entityEmpty);

        editOperations.createFace();

        verify(sceneState).getSelectedEntity();
        verifyNoMoreInteractions(sceneState, entityEmpty, input);
    }

    @Test
    public void testCopyOneVertexGoodEntity() {
        IInput input = mock(IInput.class);
        SceneState sceneState = mock(SceneState.class);
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();

        VertexPosition v1 = new VertexPosition(new Vector3f(1f, 5f, 8f));
        v1.pick();
        pickedVertices.add(v1);

        EditOperations editOperations = new EditOperations(input, pickedVertices, sceneState);

        EntityModel entityModel = mock(EntityModel.class);
        Mesh mesh = mock(Mesh.class);

        when(sceneState.getSelectedEntity()).thenReturn(entityModel);
        when(entityModel.getMesh()).thenReturn(mesh);

        editOperations.copyVertices();

        verify(sceneState).getSelectedEntity();
        verify(entityModel).getMesh();
        verify(mesh, times(1)).addVertex(any());
        verifyNoMoreInteractions(sceneState, entityModel, mesh, input);

        assertThat(v1.isPicked()).isFalse();
        assertThat(pickedVertices).doesNotContain(v1);
        assertThat(pickedVertices.get(0).getValue()).isEqualTo(v1.getValue());
        assertThat(pickedVertices.size()).isEqualTo(1);
    }

    @Test
    public void testCopyManyVertices() {
        IInput input = mock(IInput.class);
        SceneState sceneState = mock(SceneState.class);
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();

        VertexPosition v1 = new VertexPosition(new Vector3f(1f, 5f, 8f));
        v1.pick();
        pickedVertices.add(v1);
        VertexPosition v2 = new VertexPosition(new Vector3f(1f, 5f, 8f));
        v2.pick();
        pickedVertices.add(v2);

        EditOperations editOperations = new EditOperations(input, pickedVertices, sceneState);

        EntityModel entityModel = mock(EntityModel.class);
        Mesh mesh = mock(Mesh.class);

        when(sceneState.getSelectedEntity()).thenReturn(entityModel);
        when(entityModel.getMesh()).thenReturn(mesh);

        editOperations.copyVertices();

        var result = pickedVertices.stream()
                .map(VertexPosition::getValue).toList();

        verify(sceneState).getSelectedEntity();
        verify(entityModel).getMesh();
        verify(mesh, times(2)).addVertex(any());
        verifyNoMoreInteractions(sceneState, entityModel, mesh, input);
        assertThat(v1.isPicked()).isFalse();
        assertThat(v2.isPicked()).isFalse();
        assertThat(pickedVertices).doesNotContain(v1, v2);
        assertThat(pickedVertices.size()).isEqualTo(2);
        assertThat(result).containsExactly(v1.getValue(), v2.getValue());
    }
}