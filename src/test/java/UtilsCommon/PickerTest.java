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
    public void testVertexCTRLKeyIsNotPressed() {
        IInput input = mock(IInput.class);
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();
        pickedVertices.add(new VertexPosition(new Vector3f()));
        pickedVertices.add(new VertexPosition(new Vector3f()));
        pickedVertices.add(new VertexPosition(new Vector3f()));
        ArrayList<Face> pickedFaces = new ArrayList<>();
        pickedFaces.add(new Face(pickedVertices));
        VertexPosition v1 = new VertexPosition(new Vector3f());
        v1.pick();
        pickedVertices.add(v1);
        SceneState sceneState = mock(SceneState.class);
        Camera camera = mock(Camera.class);
        ArrayList<VertexPosition> vertices = new ArrayList<>();
        vertices.add(new VertexPosition(new Vector3f(1f, 1f, 1f)));
        vertices.add(new VertexPosition(new Vector3f(2f, 2f, 2f)));
        vertices.add(new VertexPosition(new Vector3f(3f, 3f, 3f)));
        ArrayList<Face> faces = new ArrayList<>();
        faces.add(new Face(vertices));
        EntityModel entityModel = new EntityModel(new Mesh(vertices, faces), null);

        Picker picker = new Picker(input, pickedVertices, pickedFaces, sceneState);

        when(input.isKeyPressed(IInput.KeyCode.KEY_LEFT_CTRL)).thenReturn(false);
        when(sceneState.getCamera()).thenReturn(camera);
        when(camera.getRay(anyFloat(), anyFloat())).thenReturn(new Ray(new Vector3f(), new Vector3f()));
        when(sceneState.getRoot()).thenReturn(new EntityEmpty());
        when(sceneState.getMainSelectedEntity()).thenReturn(entityModel);

        picker.pick();

        assertThat(v1.isPicked()).isFalse();
        assertThat(pickedVertices).doesNotContain(v1);
        assertThat(pickedVertices).isEmpty();
        verify(input).isKeyPressed(IInput.KeyCode.KEY_LEFT_CTRL);
    }

    @Test
    public void testVertexCTRLKeyIsPressed() {
        IInput input = mock(IInput.class);
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();
        ArrayList<Face> pickedFaces = new ArrayList<>();
        VertexPosition v1 = new VertexPosition(new Vector3f());
        v1.pick();
        pickedVertices.add(v1);
        VertexPosition v2 = new VertexPosition(new Vector3f());
        v2.pick();
        pickedVertices.add(v2);

        SceneState sceneState = mock(SceneState.class);
        Camera camera = mock(Camera.class);
        Ray ray = mock(Ray.class);

        ArrayList<VertexPosition> newVertices = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            newVertices.add(new VertexPosition(new Vector3f()));
        }
        ArrayList<Face> faces = new ArrayList<>();
        faces.add(new Face(newVertices));
        ArrayList<VertexPosition> newVertices2 = new ArrayList<>();
        newVertices2.add(v1);
        newVertices2.add(v2);
        newVertices2.add(newVertices.get(1));
        faces.add(new Face(newVertices2));

        newVertices2.remove(2);
        newVertices.addAll(newVertices2);

        Mesh mesh = new Mesh(newVertices, faces);

        EntityModel entityModel = new EntityModel(mesh, null);

        Picker picker = new Picker(input, pickedVertices, pickedFaces, sceneState);

        when(input.isKeyPressed(IInput.KeyCode.KEY_LEFT_CTRL)).thenReturn(true);
        when(sceneState.getCamera()).thenReturn(camera);
        when(sceneState.getMainSelectedEntity()).thenReturn(entityModel);
        when(camera.getRay(anyFloat(), anyFloat())).thenReturn(ray);
        when(sceneState.getRoot()).thenReturn(entityModel);
        when(ray.getOrigin()).thenReturn(new Vector3f());
        when(ray.distanceFromSphere(any(), anyFloat())).thenReturn(0.5f, 0.1f, 0.2f, 5f, 42f);

        picker.pick();

        assertThat(pickedVertices).containsExactlyInAnyOrder(v1, v2, newVertices.get(1));
    }

    @Test
    public void testFaceCTRLKeyIsNotPressed() {
        IInput input = mock(IInput.class);
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();
        pickedVertices.add(new VertexPosition(new Vector3f()));
        pickedVertices.add(new VertexPosition(new Vector3f()));
        pickedVertices.add(new VertexPosition(new Vector3f()));
        ArrayList<Face> pickedFaces = new ArrayList<>();
        pickedFaces.add(new Face(pickedVertices));
        VertexPosition v1 = new VertexPosition(new Vector3f());
        v1.pick();
        pickedVertices.add(v1);
        SceneState sceneState = mock(SceneState.class);
        Camera camera = mock(Camera.class);
        ArrayList<VertexPosition> vertices = new ArrayList<>();
        vertices.add(new VertexPosition(new Vector3f(1f, 1f, 1f)));
        vertices.add(new VertexPosition(new Vector3f(2f, 2f, 2f)));
        vertices.add(new VertexPosition(new Vector3f(3f, 3f, 3f)));
        ArrayList<Face> faces = new ArrayList<>();
        faces.add(new Face(vertices));
        EntityModel entityModel = new EntityModel(new Mesh(vertices, faces), null);
        Ray ray = mock(Ray.class);
        Picker picker = new Picker(input, pickedVertices, pickedFaces, sceneState);

        when(input.isKeyPressed(IInput.KeyCode.KEY_LEFT_CTRL)).thenReturn(false);
        when(input.isKeyPressed(IInput.KeyCode.KEY_LEFT_SHIFT)).thenReturn(true);
        when(sceneState.getCamera()).thenReturn(camera);
        when(camera.getRay(anyFloat(), anyFloat())).thenReturn(new Ray(new Vector3f(), new Vector3f()));
        when(sceneState.getRoot()).thenReturn(new EntityEmpty());
        when(sceneState.getMainSelectedEntity()).thenReturn(entityModel);
        when(camera.getRay(anyFloat(), anyFloat())).thenReturn(ray);
        when(ray.distanceFromPlane(any())).thenReturn(2f, 3f);
        picker.pick();

        assertThat(pickedFaces).containsExactly(faces.get(0));
    }
}