package Scene;

import EntityTree.Entity;
import EntityTree.EntityEmpty;
import UtilsCommon.Camera;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SceneStateTest {
    @Test
    public void testRoot() {
        SceneState sceneState = new SceneState();
        Entity expected = new EntityEmpty();

        sceneState.setRoot(expected);
        Entity result = sceneState.getRoot();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testSelectedEntity() {
        SceneState sceneState = new SceneState();
        Entity expected = new EntityEmpty();

        sceneState.setMainSelectedEntity(expected);
        Entity result = sceneState.getMainSelectedEntity();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testWindowHeight() {
        SceneState sceneState = new SceneState();
        int expected = 800;

        int result = sceneState.getSceneWindowHeight();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testWindowWidth() {
        SceneState sceneState = new SceneState();
        int expected = 1200;

        int result = sceneState.getSceneWindowWidth();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testCamera() {
        SceneState sceneState = new SceneState();
        Camera camera = mock(Camera.class);

        sceneState.setCamera(camera);
        Camera result = sceneState.getCamera();

        assertThat(result).isEqualTo(camera);
    }
}