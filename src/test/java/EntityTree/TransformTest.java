package EntityTree;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.junit.jupiter.api.Test;

import java.nio.FloatBuffer;

import static org.junit.jupiter.api.Assertions.*;

class TransformTest {

    @Test
    void getLocalTranslation() {
        var transform = new Transform();
        Vector3fc expected = new Vector3f(0f, 0f, 0f);
        Vector3fc result = transform.getLocalTranslation();
        assertTrue(expected.equals(result, 0.001f));
    }

    @Test
    void setLocalTranslation() {
        var transform = new Transform();
        Vector3fc expected = new Vector3f(1f, 8f, 3f);
        transform.setLocalTranslation(new Vector3f(1f, 8f, 3f));
        Vector3fc result = transform.getLocalTranslation();
        assertTrue(expected.equals(result, 0.001f));
    }

    @Test
    void getLocalRotation() {
        var transform = new Transform();
        Vector3fc expected = new Vector3f(0f, 0f, 0f);
        Vector3fc result = transform.getLocalTranslation();
        assertTrue(expected.equals(result, 0.001f));
    }

    @Test
    void setLocalRotation() {
        var transform = new Transform();
        Vector3fc expected = new Vector3f(4f, 2f, 0f);
        transform.setLocalRotation(new Vector3f(4f, 2f, 0f));
        Vector3fc result = transform.getLocalRotation();
        assertTrue(expected.equals(result, 0.001f));
    }

    @Test
    void getLocalScale() {
        var transform = new Transform();
        Vector3fc expected = new Vector3f(1f, 1f, 1f);
        Vector3fc result = transform.getLocalScale();
        assertTrue(expected.equals(result, 0.001f));
    }

    @Test
    void setLocalScale() {
        var transform = new Transform();
        Vector3fc expected = new Vector3f(5f, 1f, 3f);
        transform.setLocalScale(new Vector3f(5f, 1f, 3f));
        Vector3fc result = transform.getLocalScale();
        assertTrue(expected.equals(result, 0.001f));
    }

    @Test
    void getGlobalModelMatrix() {
        var transform = new Transform();
        var expected = new Matrix4f().identity();
        var result = transform.getGlobalModelMatrix();
        assertTrue(expected.equals(result, 0.001f));
    }

    @Test
    void updateGlobalModelMatrix() {

    }

    @Test
    void testUpdateGlobalModelMatrix() {
    }


}