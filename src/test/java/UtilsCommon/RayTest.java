package UtilsCommon;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RayTest {
    @Test
    public void testGetDirection() {
        Vector3f origin = new Vector3f();
        Vector3f direction = new Vector3f();
        Ray ray = new Ray(origin, direction);

        Vector3f result = ray.getDirection();
        assertThat(result).isEqualTo(direction);
    }

    @Test
    public void testGetOrigin() {
        Vector3f origin = new Vector3f();
        Vector3f direction = new Vector3f();
        Ray ray = new Ray(origin, direction);

        Vector3f result = ray.getOrigin();
        assertThat(result).isEqualTo(origin);
    }

    @Test
    public void testSetDirection() {
        Vector3f origin = new Vector3f();
        Vector3f direction = new Vector3f();
        Ray ray = new Ray(origin, direction);
        Vector3f expected = new Vector3f(1f, 2f, 3f);

        ray.setDirection(expected);

        Vector3f result = ray.getDirection();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testSetOrigin() {
        Vector3f origin = new Vector3f();
        Vector3f direction = new Vector3f();
        Ray ray = new Ray(origin, direction);
        Vector3f expected = new Vector3f(4f, 2f, 0f);

        ray.setOrigin(expected);

        Vector3f result = ray.getOrigin();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testDistanceFromSphere() {

    }
}