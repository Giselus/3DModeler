package UtilsModel;

import EntityTree.EntityEmpty;
import EntityTree.EntityModel;
import Scene.IInput;
import Scene.SceneState;
import UtilsCommon.Camera;
import UtilsCommon.Ray;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
        EntityEmpty entityEmpty = mock(EntityEmpty.class);

        EditOperations editOperations = new EditOperations(input, pickedVertices, sceneState);

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

    @Test
    public void deleteVertexBadEntity() {
        IInput input = mock(IInput.class);
        SceneState sceneState = mock(SceneState.class);
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();
        VertexPosition v1 = new VertexPosition(new Vector3f(1f, 8.3f, 2f));
        pickedVertices.add(v1);
        EntityEmpty entityEmpty = mock(EntityEmpty.class);

        EditOperations editOperations = new EditOperations(input, pickedVertices, sceneState);

        when(sceneState.getSelectedEntity()).thenReturn(entityEmpty);

        editOperations.deleteVertex();

        assertThat(pickedVertices).containsExactly(v1);
        verify(sceneState).getSelectedEntity();
        verifyNoMoreInteractions(sceneState, input);
    }

    @Test
    public void deleteVertexGoodEntity() {
        IInput input = mock(IInput.class);
        SceneState sceneState = mock(SceneState.class);
        EntityModel entityModel = mock(EntityModel.class);
        Mesh mesh = mock(Mesh.class);

        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();
        VertexPosition v1 = new VertexPosition(new Vector3f(5f, 3f, 2f));
        VertexPosition v2 = new VertexPosition(new Vector3f(2f, 3f, 1f));
        pickedVertices.add(v1);
        pickedVertices.add(v2);

        EditOperations editOperations = new EditOperations(input, pickedVertices, sceneState);

        when(sceneState.getSelectedEntity()).thenReturn(entityModel);
        when(entityModel.getMesh()).thenReturn(mesh);

        editOperations.deleteVertex();

        assertThat(pickedVertices).isEmpty();
        assertThat(v1.isPicked()).isFalse();
        assertThat(v2.isPicked()).isFalse();

        verify(sceneState).getSelectedEntity();
        verify(entityModel).getMesh();
        verify(mesh, times(2)).deleteVertex(any());
        verifyNoMoreInteractions(sceneState, entityModel, mesh, input);
    }

    @Test
    public void testVerticesDoesNotMoveWhenKeyIsNotPressed(){
        IInput input = mock(IInput.class);
        SceneState sceneState = mock(SceneState.class);
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();

        EditOperations editOperations = new EditOperations(input, pickedVertices, sceneState);

        when(input.isKeyPressed(IInput.KeyCode.KEY_X)).thenReturn(false);

        editOperations.movePoints(0.3f, 0.3f);

        verify(input).isKeyPressed(IInput.KeyCode.KEY_X);
        verifyNoMoreInteractions(input, sceneState);
    }

    @Test
    public void testVerticesMoveWhenKeyIsPressed(){
        IInput input = mock(IInput.class);
        SceneState sceneState = mock(SceneState.class);
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();
        VertexPosition v1 = new VertexPosition(new Vector3f(3f, 2f, 7f));
        pickedVertices.add(v1);
        Camera camera = mock(Camera.class);
        Ray ray = mock(Ray.class);

        Vector3f expectedPosition = new Vector3f(4.5f, 4f, 3.5f);
        EditOperations editOperations = new EditOperations(input, pickedVertices, sceneState);

        when(input.isKeyPressed(IInput.KeyCode.KEY_X)).thenReturn(true);
        when(input.getMouseX()).thenReturn(0.2f);
        when(input.getMouseY()).thenReturn(0.3f);
        when(sceneState.getCamera()).thenReturn(camera);
        when(camera.getPosition()).thenReturn(new Vector3f(2f, 1f, 1f));
        when(camera.getDirection()).thenReturn(new Vector3f(1f, 1f, 1f));
        when(camera.getRay(anyFloat(), anyFloat())).thenReturn(ray);
        when(ray.getDirection()).thenReturn(new Vector3f(0.5f, 0.8f, 0.3f), new Vector3f(0.1f, 0.2f, 0.5f));

        editOperations.movePoints(0.1f, -0.2f);

        assertThat(v1.getValue().x).isEqualTo(expectedPosition.x, withPrecision(0.001f));
        assertThat(v1.getValue().y).isEqualTo(expectedPosition.y, withPrecision(0.001f));
        assertThat(v1.getValue().z).isEqualTo(expectedPosition.z, withPrecision(0.001f));

        verify(input).isKeyPressed(IInput.KeyCode.KEY_X);
        verify(input).getMouseX();
        verify(input).getMouseY();
        verify(sceneState).getCamera();
        verify(camera).getPosition();
        verify(camera).getDirection();
        verify(camera, times(2)).getRay(anyFloat(), anyFloat());
        verify(ray, times(2)).getDirection();

        verifyNoMoreInteractions(input, sceneState, camera, ray);
    }
}