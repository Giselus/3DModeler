package UtilsCommon;

import EntityTree.EntityEmpty;
import EntityTree.EntityModel;
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

class PickerTest {
    @Test
    public void testKeyIsNotPressed() {
        IInput input = mock(IInput.class);
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();
        VertexPosition v1 = new VertexPosition(new Vector3f());
        v1.pick();
        pickedVertices.add(v1);
        SceneState sceneState = mock(SceneState.class);
        Camera camera = mock(Camera.class);

        Picker picker = new Picker(input, pickedVertices, sceneState);

        when(input.isKeyPressed(IInput.KeyCode.KEY_LEFT_CTRL)).thenReturn(false);
        when(sceneState.getCamera()).thenReturn(camera);
        when(camera.getRay(anyFloat(), anyFloat())).thenReturn(new Ray(new Vector3f(), new Vector3f()));
        when(sceneState.getRoot()).thenReturn(new EntityEmpty());

        picker.pickVertex();

        assertThat(v1.isPicked()).isFalse();
        assertThat(pickedVertices).doesNotContain(v1);
        assertThat(pickedVertices).isEmpty();
        verify(input).isKeyPressed(IInput.KeyCode.KEY_LEFT_CTRL);
    }

    @Test
    public void testKeyIsPressed() {
        IInput input = mock(IInput.class);
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();
        VertexPosition v1 = new VertexPosition(new Vector3f());
        v1.pick();
        pickedVertices.add(v1);

        SceneState sceneState = mock(SceneState.class);
        Camera camera = mock(Camera.class);
        Ray ray = mock(Ray.class);

        ArrayList<VertexPosition> newVertices = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            newVertices.add(new VertexPosition(new Vector3f()));
        }
        Face face = new Face(newVertices);

        ArrayList<Face> faces = new ArrayList<>();
        faces.add(face);
        Mesh mesh = new Mesh(newVertices, faces);

        EntityModel entityModel = new EntityModel(mesh, null);

        Picker picker = new Picker(input, pickedVertices, sceneState);

        when(input.isKeyPressed(IInput.KeyCode.KEY_LEFT_CTRL)).thenReturn(true);
        when(sceneState.getCamera()).thenReturn(camera);
        when(camera.getRay(anyFloat(), anyFloat())).thenReturn(ray);
        when(sceneState.getRoot()).thenReturn(entityModel);
        when(ray.getOrigin()).thenReturn(new Vector3f());
        when(ray.distanceFromSphere(any(), anyFloat())).thenReturn(0.5f, 0.2f, 4f);

        picker.pickVertex();

        assertThat(pickedVertices).containsExactlyInAnyOrder(v1, newVertices.get(1));
    }
}