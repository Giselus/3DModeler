package UtilsCommon;

import Scene.IInput;
import Scene.SceneState;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CameraTest {
    @Test
    public void testGetPosition() {
        SceneState sceneState = new SceneState();
        IInput input = mock(IInput.class);
        Camera camera = new Camera(input, sceneState);
        Vector3f expected = new Vector3f(15f, 0f, 0f);

        Vector3f result = camera.getPosition();

        assertThat(result.x).isEqualTo(expected.x, withPrecision(.001f));
        assertThat(result.y).isEqualTo(expected.y, withPrecision(.001f));
        assertThat(result.z).isEqualTo(expected.z, withPrecision(.001f));
    }

    @Test
    public void testGetZoom() {
        SceneState sceneState = new SceneState();
        IInput input = mock(IInput.class);
        Camera camera = new Camera(input, sceneState);
        float expected = 45f;

        float result = camera.getZoom();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testGetViewMatrix() {
        SceneState sceneState = new SceneState();
        IInput input = mock(IInput.class);
        Camera camera = new Camera(input, sceneState);
        Matrix4f expected = new Matrix4f().zero();
        expected.m02(1f);
        expected.m11(1f);
        expected.m20(-1f);
        expected.m32(-15f);
        expected.m33(1f);

        Matrix4f result = camera.getViewMatrix();

        assertThat(result.equals(expected, .001f)).isTrue();
    }

    @Test
    public void testGetProjectionMatrix() {
        SceneState sceneState = new SceneState();
        IInput input = mock(IInput.class);
        Camera camera = new Camera(input, sceneState);
        Matrix4f expected = new Matrix4f().zero();
        expected.m00(1.609f);
        expected.m11(2.414f);
        expected.m22(-1.001f);
        expected.m23(-1f);
        expected.m32(-.2001f);

        Matrix4f result = camera.getProjectionMatrix();

        assertThat(result.equals(expected, .001f)).isTrue();
    }

    @Test
    public void testGetDirection() {
        SceneState sceneState = new SceneState();
        IInput input = mock(IInput.class);
        Camera camera = new Camera(input, sceneState);
        Vector3f expected = new Vector3f(-1f, 0f, 0f);

        Vector3f result = camera.getDirection();

        assertThat(result.x).isEqualTo(expected.x, withPrecision(.001f));
        assertThat(result.y).isEqualTo(expected.y, withPrecision(.001f));
        assertThat(result.z).isEqualTo(expected.z, withPrecision(.001f));
    }

    @Test
    public void testGetRay() {
        SceneState sceneState = new SceneState();
        IInput input = mock(IInput.class);
        Camera camera = new Camera(input, sceneState);
        Vector3f expectedOrigin = new Vector3f(15f, 0f, 0f);
        Vector3f expectedDirection = new Vector3f(-.9792f, .2028f, 0f);

        Ray result = camera.getRay(0f, .5f);
        Vector3f resultOrigin = result.getOrigin();
        Vector3f resultDirection = result.getDirection();

        assertThat(resultOrigin.x).isEqualTo(expectedOrigin.x, withPrecision(.001f));
        assertThat(resultOrigin.y).isEqualTo(expectedOrigin.y, withPrecision(.001f));
        assertThat(resultOrigin.z).isEqualTo(expectedOrigin.z, withPrecision(.001f));

        assertThat(resultDirection.x).isEqualTo(expectedDirection.x, withPrecision(.001f));
        assertThat(resultDirection.y).isEqualTo(expectedDirection.y, withPrecision(.001f));
        assertThat(resultDirection.z).isEqualTo(expectedDirection.z, withPrecision(.001f));
    }

    @Test
    public void testProcessMouseScrollSmallNegativeOffset() {
        SceneState sceneState = new SceneState();
        IInput input = mock(IInput.class);
        Camera camera = new Camera(input, sceneState);
        Vector3f expectedPosition = new Vector3f(16.938f, 0f, 0f);

        camera.processMouseScroll(-2f);
        Vector3f resultPosition = camera.getPosition();

        assertThat(resultPosition.x).isEqualTo(expectedPosition.x, withPrecision(.001f));
        assertThat(resultPosition.y).isEqualTo(expectedPosition.y, withPrecision(.001f));
        assertThat(resultPosition.z).isEqualTo(expectedPosition.z, withPrecision(.001f));
    }

    @Test
    public void testProcessMouseScrollSmallPositiveOffset() {
        SceneState sceneState = new SceneState();
        IInput input = mock(IInput.class);
        Camera camera = new Camera(input, sceneState);
        Vector3f expectedPosition = new Vector3f(11.5f, 0f, 0f);

        camera.processMouseScroll(4f);
        Vector3f resultPosition = camera.getPosition();

        assertThat(resultPosition.x).isEqualTo(expectedPosition.x, withPrecision(.001f));
        assertThat(resultPosition.y).isEqualTo(expectedPosition.y, withPrecision(.001f));
        assertThat(resultPosition.z).isEqualTo(expectedPosition.z, withPrecision(.001f));
    }

    @Test
    public void testProcessMouseScrollBigNegativeOffset() {
        SceneState sceneState = new SceneState();
        IInput input = mock(IInput.class);
        Camera camera = new Camera(input, sceneState);
        Vector3f expectedPosition = new Vector3f(180f, 0f, 0f);

        camera.processMouseScroll(-200f);
        Vector3f resultPosition = camera.getPosition();

        assertThat(resultPosition.x).isEqualTo(expectedPosition.x, withPrecision(.001f));
        assertThat(resultPosition.y).isEqualTo(expectedPosition.y, withPrecision(.001f));
        assertThat(resultPosition.z).isEqualTo(expectedPosition.z, withPrecision(.001f));
    }

    @Test
    public void testProcessMouseScrollBigPositiveOffset() {
        SceneState sceneState = new SceneState();
        IInput input = mock(IInput.class);
        Camera camera = new Camera(input, sceneState);
        Vector3f expectedPosition = new Vector3f(0.1f, 0f, 0f);

        camera.processMouseScroll(400f);
        Vector3f resultPosition = camera.getPosition();

        assertThat(resultPosition.x).isEqualTo(expectedPosition.x, withPrecision(.001f));
        assertThat(resultPosition.y).isEqualTo(expectedPosition.y, withPrecision(.001f));
        assertThat(resultPosition.z).isEqualTo(expectedPosition.z, withPrecision(.001f));
    }

    @Test
    public void testProcessMousePositionNoMouseKeyPressed() {
        SceneState sceneState = new SceneState();
        IInput input = mock(IInput.class);
        when(input.isMouseKeyPressed(any())).thenReturn(false);
        Camera camera = new Camera(input, sceneState);
        Vector3f expectedPosition = new Vector3f(camera.getPosition());
        Vector3f expectedDirection = new Vector3f(camera.getDirection());
        Matrix4f expectedViewMatrix = new Matrix4f(camera.getViewMatrix());

        camera.processMousePosition(.3f, .7f);
        Vector3f resultPosition = camera.getPosition();
        Vector3f resultDirection = camera.getDirection();
        Matrix4f resultViewMatrix = camera.getViewMatrix();

        assertThat(resultPosition).isEqualTo(expectedPosition);
        assertThat(resultDirection).isEqualTo(expectedDirection);
        assertThat(resultViewMatrix).isEqualTo(expectedViewMatrix);
    }

    @Test
    public void testProcessMousePositionSmallOffset() {
        SceneState sceneState = new SceneState();
        IInput input = mock(IInput.class);
        when(input.isMouseKeyPressed(any())).thenReturn(true);
        Camera camera = new Camera(input, sceneState);
        Vector3f expectedPosition = new Vector3f(9.186f, -7.5f, 9.186f);
        Vector3f expectedDirection = new Vector3f(-.6124f, 0.5f, -.6124f);
        Matrix4f expectedViewMatrix = new Matrix4f(.707f, .353f, .612f, 0f, 0f, .866f, -.5f, 0f, -.707f, .353f, .612f, 0f, 0f, 0f, -15f, 1f);

        camera.processMousePosition(.3f, .2f);
        Vector3f resultPosition = camera.getPosition();
        Vector3f resultDirection = camera.getDirection();
        Matrix4f resultViewMatrix = camera.getViewMatrix();

        assertThat(resultPosition.x).isEqualTo(expectedPosition.x, withPrecision(.001f));
        assertThat(resultPosition.y).isEqualTo(expectedPosition.y, withPrecision(.001f));
        assertThat(resultPosition.z).isEqualTo(expectedPosition.z, withPrecision(.001f));
        assertThat(resultDirection.x).isEqualTo(expectedDirection.x, withPrecision(.001f));
        assertThat(resultDirection.y).isEqualTo(expectedDirection.y, withPrecision(.001f));
        assertThat(resultDirection.z).isEqualTo(expectedDirection.z, withPrecision(.001f));
        assertThat(resultViewMatrix.equals(expectedViewMatrix, .001f)).isTrue();
    }

    @Test
    public void testProcessMousePositionBigPositiveOffset() {
        SceneState sceneState = new SceneState();
        IInput input = mock(IInput.class);
        when(input.isMouseKeyPressed(any())).thenReturn(true);
        Camera camera = new Camera(input, sceneState);
        Vector3f expectedPosition = new Vector3f(-.13f, -14.997f, -.226f);
        Vector3f expectedDirection = new Vector3f(.008f, 1f, .015f);
        Matrix4f expectedViewMatrix = new Matrix4f(-.866f, -.5f, -.008f, 0f, 0f, .017f, -1f, 0f, .5f, -.865f, -.015f, 0f, 0f, 0f, -15f, 1f);

        camera.processMousePosition(100f, 200f);
        Vector3f resultPosition = camera.getPosition();
        Vector3f resultDirection = camera.getDirection();
        Matrix4f resultViewMatrix = camera.getViewMatrix();

        assertThat(resultPosition.x).isEqualTo(expectedPosition.x, withPrecision(.001f));
        assertThat(resultPosition.y).isEqualTo(expectedPosition.y, withPrecision(.001f));
        assertThat(resultPosition.z).isEqualTo(expectedPosition.z, withPrecision(.001f));
        assertThat(resultDirection.x).isEqualTo(expectedDirection.x, withPrecision(.001f));
        assertThat(resultDirection.y).isEqualTo(expectedDirection.y, withPrecision(.001f));
        assertThat(resultDirection.z).isEqualTo(expectedDirection.z, withPrecision(.001f));
        assertThat(resultViewMatrix.equals(expectedViewMatrix, .001f)).isTrue();
    }

    @Test
    public void testProcessMousePositionBigNegativeOffset() {
        SceneState sceneState = new SceneState();
        IInput input = mock(IInput.class);
        when(input.isMouseKeyPressed(any())).thenReturn(true);
        Camera camera = new Camera(input, sceneState);
        Vector3f expectedPosition = new Vector3f(-.13f, 14.997f, .226f);
        Vector3f expectedDirection = new Vector3f(.008f, -1f, -.015f);
        Matrix4f expectedViewMatrix = new Matrix4f(.866f, .5f, -.008f, 0f, 0f, .017f, 1f, 0f, .5f, -.865f, .015f, 0f, 0f, 0f, -15f, 1f);

        camera.processMousePosition(-400f, -300f);
        Vector3f resultPosition = camera.getPosition();
        Vector3f resultDirection = camera.getDirection();
        Matrix4f resultViewMatrix = camera.getViewMatrix();

        assertThat(resultPosition.x).isEqualTo(expectedPosition.x, withPrecision(.001f));
        assertThat(resultPosition.y).isEqualTo(expectedPosition.y, withPrecision(.001f));
        assertThat(resultPosition.z).isEqualTo(expectedPosition.z, withPrecision(.001f));
        assertThat(resultDirection.x).isEqualTo(expectedDirection.x, withPrecision(.001f));
        assertThat(resultDirection.y).isEqualTo(expectedDirection.y, withPrecision(.001f));
        assertThat(resultDirection.z).isEqualTo(expectedDirection.z, withPrecision(.001f));
        assertThat(resultViewMatrix.equals(expectedViewMatrix, .001f)).isTrue();
    }
}