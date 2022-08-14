package UtilsCommon;

import UtilsModel.VertexPosition;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalMatchers;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeometryMathTest {

    @Test
    void testMeanPoint() {
        ArrayList<VertexPosition> points = new ArrayList<>();
        points.add(new VertexPosition(new Vector3f(1f,2f,3f)));
        points.add(new VertexPosition(new Vector3f(4f,5f,6f)));
        points.add(new VertexPosition(new Vector3f(-0.5f,-1.5f,-2.5f)));
        points.add(new VertexPosition(new Vector3f(10f,20f,30f)));

        Vector3f expected = new Vector3f(3.625f, 6.375f,9.125f);
        Vector3f result = GeometryMath.meanPoint(points);

        assertThat(result.x).isEqualTo(expected.x, withPrecision(0.01f));
        assertThat(result.y).isEqualTo(expected.y, withPrecision(0.01f));
        assertThat(result.z).isEqualTo(expected.z, withPrecision(0.01f));
    }

    @Test
    void testNormalPlane() {
        Vector3f vector = new Vector3f(1f, 1f, 1f);
        Vector3f point = new Vector3f(5f, 6f, 7f);

        Vector4f expected = new Vector4f(1f, 1f, 1f, 18f);
        Vector4f result = GeometryMath.normalPlane(vector, point);

        assertThat(result.x).isEqualTo(expected.x, withPrecision(0.01f));
        assertThat(result.y).isEqualTo(expected.y, withPrecision(0.01f));
        assertThat(result.z).isEqualTo(expected.z, withPrecision(0.01f));
        assertThat(result.w).isEqualTo(expected.w, withPrecision(0.01f));
    }

    @Test
    void testIntersectionVectorPlane() {
        Vector3f startingPoint = new Vector3f(1f, 2f, 3f);
        Vector3f direction = new Vector3f(4f, 5f, 6f);
        Vector4f plane = new Vector4f(1f, 1f, 1f, 12f);

        Vector3f expected = new Vector3f(2.6f, 4f, 5.4f);
        Vector3f result = GeometryMath.intersectionVectorPlane(startingPoint, direction, plane);

        assertThat(result.x).isEqualTo(expected.x, withPrecision(0.01f));
        assertThat(result.y).isEqualTo(expected.y, withPrecision(0.01f));
        assertThat(result.z).isEqualTo(expected.z, withPrecision(0.01f));
    }

    @Test
    void testClickDirection() {
        float x = 0.5f;
        float y = 0.7f;
        float delta = 0.001f;

        Camera camera = mock(Camera.class);
        Ray ray = mock(Ray.class);

        when(camera.getRay(anyFloat(), anyFloat())).thenReturn(ray);

        GeometryMath.clickDirection(x, y, camera);

        verify(ray).getDirection();
        verify(camera).getRay(AdditionalMatchers.eq(0f, delta), AdditionalMatchers.eq(0.4f, delta));
    }

    @Test
    void testTranslatePoints() {
        ArrayList<VertexPosition> points = new ArrayList<>();
        points.add(new VertexPosition(new Vector3f(1f,2f,3f)));
        points.add(new VertexPosition(new Vector3f(4f,5f,6f)));
        points.add(new VertexPosition(new Vector3f(-0.5f,-1.5f,-2.5f)));
        points.add(new VertexPosition(new Vector3f(10f,20f,30f)));

        ArrayList<Vector3f> expected = new ArrayList<>();
        expected.add(new Vector3f(3.5f,0f,4f));
        expected.add(new Vector3f(6.5f,3f,7f));
        expected.add(new Vector3f(2f,-3.5f,-1.5f));
        expected.add(new Vector3f(12.5f,18f,31f));

        GeometryMath.translatePoints(points, new Vector3f(2.5f, -2f, 1f));
        assertThat(points).extracting(VertexPosition::getValue).hasSameElementsAs(expected);
    }
}