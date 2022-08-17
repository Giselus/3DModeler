package UtilsCommon;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    @Test
    public void testDistanceFromPlaneParallel() {
        Vector3f origin = new Vector3f(0f, 2f, 0f);
        Vector3f direction = new Vector3f(4f, 0f, 2f);
        Ray ray = new Ray(origin, direction);
        ArrayList<Vector3f> vertices = new ArrayList<>();
        vertices.add(new Vector3f(0f, 0f, 0f));
        vertices.add(new Vector3f(3f, 0f, 6f));
        vertices.add(new Vector3f(8f, 0f, 1f));

        float expected = -1f;

        float result = ray.distanceFromPlane(vertices);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testDistanceFromPlaneBehind() {
        Vector3f origin = new Vector3f(0f, 2f, 0f);
        Vector3f direction = new Vector3f(42f, 12f, 12f);
        Ray ray = new Ray(origin, direction);
        ArrayList<Vector3f> vertices = new ArrayList<>();
        vertices.add(new Vector3f(-3f, -2f, -4f));
        vertices.add(new Vector3f(-5f, -6f, -4f));
        vertices.add(new Vector3f(-7f, -5f, -4f));

        float expected = -1f;

        float result = ray.distanceFromPlane(vertices);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testDistanceFromPlaneOutsideFace() {
        Vector3f origin = new Vector3f(1f, 2f, 3f);
        Vector3f direction = new Vector3f(0f, 0f, 2f);
        Ray ray = new Ray(origin, direction);
        ArrayList<Vector3f> vertices = new ArrayList<>();
        vertices.add(new Vector3f(10f, 38f, 9f));
        vertices.add(new Vector3f(0f, 25f, 9f));
        vertices.add(new Vector3f(-20f, -22f, 9f));

        float expected = -1f;

        float result = ray.distanceFromPlane(vertices);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testDistanceFromPlaneCorrect() {
        Vector3f origin = new Vector3f(1f, 2f, 3f);
        Vector3f direction = new Vector3f(0f, 0f, 2f);
        Ray ray = new Ray(origin, direction);
        ArrayList<Vector3f> vertices = new ArrayList<>();
        vertices.add(new Vector3f(20f, -22f, 9f));
        vertices.add(new Vector3f(0f, 25f, 9f));
        vertices.add(new Vector3f(-20f, -22f, 9f));

        float expected = 6f;

        float result = ray.distanceFromPlane(vertices);

        assertThat(result).isEqualTo(expected);
    }
}