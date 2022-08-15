package EntityTree;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class TransformTest {

    @Test
    void testGetLocalTranslation() {
        var transform = new Transform();
        Vector3fc expected = new Vector3f(0f, 0f, 0f);
        Vector3fc result = transform.getLocalTranslation();
        assertTrue(expected.equals(result, 0.001f));
    }

    @Test
    void testSetLocalTranslation() {
        var transform = new Transform();
        Vector3fc expected = new Vector3f(1f, 8f, 3f);
        transform.setLocalTranslation(new Vector3f(1f, 8f, 3f));
        Vector3fc result = transform.getLocalTranslation();
        assertTrue(expected.equals(result, 0.001f));
    }

    @Test
    void testGetLocalRotation() {
        var transform = new Transform();
        Vector3fc expected = new Vector3f(0f, 0f, 0f);
        Vector3fc result = transform.getLocalTranslation();
        assertTrue(expected.equals(result, 0.001f));
    }

    @Test
    void testSetLocalRotation() {
        var transform = new Transform();
        Vector3fc expected = new Vector3f(4f, 2f, 0f);
        transform.setLocalRotation(new Vector3f(4f, 2f, 0f));
        Vector3fc result = transform.getLocalRotation();
        assertTrue(expected.equals(result, 0.001f));
    }

    @Test
    void testGetLocalScale() {
        var transform = new Transform();
        Vector3fc expected = new Vector3f(1f, 1f, 1f);
        Vector3fc result = transform.getLocalScale();
        assertTrue(expected.equals(result, 0.001f));
    }

    @Test
    void testSetLocalScale() {
        var transform = new Transform();
        Vector3fc expected = new Vector3f(5f, 1f, 3f);
        transform.setLocalScale(new Vector3f(5f, 1f, 3f));
        Vector3fc result = transform.getLocalScale();
        assertTrue(expected.equals(result, 0.001f));
    }

    @Test
    void testGetGlobalModelMatrix() {
        var transform = new Transform();
        var expected = new Matrix4f().identity();
        var result = transform.getGlobalModelMatrix();
        assertTrue(expected.equals(result, 0.001f));
    }

    @Test
    void testUpdateGlobalModelMatrixNoArgs() {
        Transform transform = new Transform();
        transform.setLocalRotation(new Vector3f(90f, 90f, 0f));
        transform.setLocalTranslation(new Vector3f(42f, 2f, 3f));
        transform.setLocalScale(new Vector3f(4f, 5f, 6f));
        Matrix4fc expected = new Matrix4f(0f, 0f, -4f, 0f, 5f, 0f, 0f, 0f, 0f, -6f, 0f, 0f, 42f, 2f, 3f, 1f);

        transform.updateGlobalModelMatrix();
        Matrix4fc result = transform.getGlobalModelMatrix();

        assertTrue(result.equals(expected, 0.001f));
    }

    @Test
    void testUpdateGlobalModelMatrixWithArgs() {
        Transform transform = new Transform();
        transform.setLocalRotation(new Vector3f(90f, 90f, 0f));
        transform.setLocalTranslation(new Vector3f(42f, 2f, 3f));
        transform.setLocalScale(new Vector3f(4f, 5f, 6f));
        Matrix4fc expected = new Matrix4f(0f, 4f, 0f, 0f, 0f, 0f, 0f, -5f, 0f, 0f, 6f, 0f, -1f, -3f, -2f, -42f);
        Matrix4f argMatrix = new Matrix4f().zero();
        argMatrix.m30(-1f);
        argMatrix.m21(-1f);
        argMatrix.m12(-1f);
        argMatrix.m03(-1f);

        transform.updateGlobalModelMatrix(argMatrix);
        Matrix4fc result = transform.getGlobalModelMatrix();

        assertTrue(result.equals(expected, 0.001f));
    }
}