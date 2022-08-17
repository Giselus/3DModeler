package UtilsCommon;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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
    public void testDistanceFromSphereTooFar() {
        Vector3f origin = new Vector3f(1f, 2f, 3f);
        Vector3f direction = new Vector3f(4f, 5f, 6f);
        Ray ray = new Ray(origin, direction);

        float result = ray.distanceFromSphere(new Vector3f(7f, 8f, 9f), 0.1f);

        assertThat(result).isEqualTo(-1.0f);
    }

    @Test
    public void testDistanceFromSphereTooFar2() {
        Vector3f origin = new Vector3f(0f, 0f, 0f);
        Vector3f direction = new Vector3f(4f, 5f, 6f);
        Ray ray = new Ray(origin, direction);

        float result = ray.distanceFromSphere(new Vector3f(4.2f, 4.8f, 6f), 0.1f);

        assertThat(result).isEqualTo(8.6f, withPrecision(1f));
    }
}